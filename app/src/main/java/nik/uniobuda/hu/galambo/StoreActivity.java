package nik.uniobuda.hu.galambo;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private List<Food> foods;
    private Galamb galamb;
    private GridView FoodGridview;
    private TextView playerMoneyText;
    private TextView detailsTextview;
    ImageView goBackView;
    Button vasarlasButton;
    Food selectedFood; //A gridviewból éppen kiválasztott kaja

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //Elemek elérése
        playerMoneyText = (TextView) findViewById(R.id.playerMoneyTextView);
        FoodGridview = (GridView) findViewById(R.id.ListOfFoodsAndPics);
        goBackView = (ImageView) findViewById(R.id.goBackImageview);
        vasarlasButton = (Button) findViewById(R.id.BuySelectedFoodButton);
        detailsTextview = (TextView) findViewById(R.id.SelectedFoodDetailsTextview);

        //épp landscape vagy portrait mikor megnyitjuk a boltot?
        CheckScreenOrientation(getResources().getConfiguration().orientation);
        foods = Store.getFoods();

        //kaja lsita betöltés
        LoadFoodsIntoGrodView();

        galamb = (Galamb) getIntent().getExtras().get("galamb");
        HashMap FoodHashMap = (HashMap) getIntent().getExtras().get("dictionary");
        galamb.setKajamennyiseg(FoodHashMap);
        playerMoneyText.setText(String.valueOf(galamb.getPenz()));

        //Vissza nyílra ugyanazt csinálja mint a rendes vissza gombra
        goBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PauseOrBack();
            }
        });

        vasarlasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySelected(selectedFood);
                //Az aktuális mennyiség miatt!
                UpdateSelectedDetails(selectedFood);
            }
        });
    }

    //Ha landscape 4 oszlopban vannak a kaják, ha portrait akkor 2 oszlopban
    void CheckScreenOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FoodGridview.setNumColumns(4);
        } else {
            FoodGridview.setNumColumns(2);
        }
    }

    public void BuySelected(Food selected) {
        if (selected != null) {
            if (galamb.getPenz() >= selected.getCost())
            {
                galamb.KajaVasarlas(selected.getName());
                galamb.setPenz(-selected.getCost());
                playerMoneyText.setText(String.valueOf(galamb.getPenz()));
                Toast toast =  Toast.makeText(this,"Sikeres vásárlás!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,70);
                toast.show();
            }
            else {
                Toast toast =  Toast.makeText(this,"Nincs elég pénzed!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,70);
                toast.show();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CheckScreenOrientation(newConfig.orientation);
    }

    @Override
    public void onPause() {
        PauseOrBack();
        super.onPause();
    }

    //Vissza gomb nyomásra
    @Override
    public void onBackPressed() {
        PauseOrBack();
        super.onBackPressed();
    }

    //Csinál egy intentet, beleteszi a galambot, és az ételmennyiség hashsetet
    Intent CreateIntentForSave() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putParcelable("result", galamb);
        extras.putSerializable("dictionary", (Serializable) galamb.getKajamennyiseg());
        intent.putExtras(extras);
        return intent;
    }

    void PauseOrBack()
    {
        Intent intent = CreateIntentForSave();
        this.setResult(RESULT_OK, intent);
        finish();
    }

    private void LoadFoodsIntoGrodView() {
        FoodAdapter adapter = new FoodAdapter(foods, getApplicationContext());
        FoodGridview.setAdapter(adapter);
        FoodGridview.setClickable(true);
        //Ha rátoucholunk a kajára változik a kajinfó rész
        FoodGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectionChanged(foods.get(position));
            }
        });
    }

    void SelectionChanged(Food selected) {
        selectedFood = selected;
        UpdateSelectedDetails(selected);
    }

    private void UpdateSelectedDetails(Food selected) {
        if (selected != null) {
                String s = selected.getName() + "\t\t" + "Ára: " + String.valueOf(selected.getCost())
                        + "\n" + "Mennyiség: " + String.valueOf((int) galamb.getKajamennyiseg().get(selected.getName()))
                        + "\t\t" + "Tápanyagtartalom: " + String.valueOf(selected.getNutrient());
                detailsTextview.setText(s);
        }
    }
}
