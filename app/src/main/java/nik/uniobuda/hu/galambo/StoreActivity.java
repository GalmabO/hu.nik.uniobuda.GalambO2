package nik.uniobuda.hu.galambo;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private final List<Food> foods = Store.getCikkek();
    private Galamb galamb;
    private GridView grid;
    private KajaAdapter adapter;
    private TextView jatekospenzview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store);


        Feltolt();

        galamb = (Galamb)getIntent().getExtras().get("galamb");
        HashMap a = (HashMap) getIntent().getExtras().get("dictionary");

        galamb.setKajamennyiseg((HashMap) getIntent().getExtras().get("dictionary"));

        jatekospenzview = (TextView) findViewById(R.id.jatekospenze);
        jatekospenzview.setText(String.valueOf(galamb.getPenz()));

    }


    public void Vasarlas(Food valasztott)
    {
        /*Food valasztott = null;
        
        if(melyiketvalasztotta!=null)
        {
            for (Food food: foods) {
                if(food.getNev().equals(melyiketvalasztotta))
                {
                    valasztott = food;
                    break;
                }

            }

            if(valasztott != null)
            {*/
                if(galamb.getPenz() >= valasztott.getAr())
                {
                    galamb.KajaVasarlas(valasztott.getNev());
                    galamb.setPenz(-valasztott.getAr());
                    Toast.makeText(this,"Sikeres vásárlás",Toast.LENGTH_LONG).show();
                    jatekospenzview.setText(String.valueOf(galamb.getPenz()));
                    TextView valasztottbolMeglevoMennyisegTextView = (TextView) findViewById(R.id.meglevoMennyisegErtek);
                    valasztottbolMeglevoMennyisegTextView.setText(String.valueOf((int)galamb.getKajamennyiseg().get(valasztott.getNev())));
                }
                else
                    Toast.makeText(this,"Nincs elég pénzed!",Toast.LENGTH_LONG).show();
            //}
        //}
    }


    private void Feltolt()
    {
        adapter = new KajaAdapter(foods,getApplicationContext());
        grid = (GridView) findViewById(R.id.araslista);
        grid.setAdapter(adapter);

        grid.setClickable(true);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Vasarlas(foods.get(position).getNev());
                InfoReszAdatFeltoltes(foods.get(position).getNev());
            }
        });
    }

    private void InfoReszAdatFeltoltes(String melyiketvalasztotta)
    {
        Food valasztott = null;

        if(melyiketvalasztotta!=null) {
            for (Food food : foods) {
                if (food.getNev().equals(melyiketvalasztotta)) {
                    valasztott = food;
                    break;
                }
            }
            if(valasztott != null)
            {
                final Food kivalasztott = valasztott;
                TextView valasztottNeveTextView = (TextView) findViewById(R.id.kajaneveErtek);
                TextView valasztottTapanyagtartalmaTextView = (TextView) findViewById(R.id.tapanyagmennyisegErtek);
                TextView valasztottAraTextView = (TextView) findViewById(R.id.arErtek);
                TextView valasztottbolMeglevoMennyisegTextView = (TextView) findViewById(R.id.meglevoMennyisegErtek);

                Button vasarlasButton = (Button)findViewById(R.id.vasarlas);

                valasztottNeveTextView.setText(kivalasztott.getNev());
                valasztottTapanyagtartalmaTextView.setText(String.valueOf(kivalasztott.getTapanyagmennyiseg()));
                valasztottAraTextView.setText(String.valueOf(kivalasztott.getAr()));
                valasztottbolMeglevoMennyisegTextView.setText(String.valueOf((int)galamb.getKajamennyiseg().get(kivalasztott.getNev())));

                vasarlasButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Vasarlas(kivalasztott);
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {

        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putParcelable("result", galamb);
        extras.putSerializable("dictionary", (Serializable) galamb.getKajamennyiseg());
        intent.putExtras(extras);

        //intent.putExtras("result", (Parcelable) galamb);
        this.setResult(RESULT_OK, intent);
        finish();

        super.onPause();

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putParcelable("result", galamb);
        extras.putSerializable("dictionary", (Serializable) galamb.getKajamennyiseg());
        intent.putExtras(extras);

        //intent.putExtras("result", (Parcelable) galamb);
        this.setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();

    }
}
