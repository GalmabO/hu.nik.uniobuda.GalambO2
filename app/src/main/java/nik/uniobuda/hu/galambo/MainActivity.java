package nik.uniobuda.hu.galambo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Galamb galamb;
    private final String FILENAME = "Galamb___Peldany";
    private final String FILENAME_DATE = "date";
    SparseIntArray ImagesToActivities;

    //Felület:
    TextView kivalasztottkajaText;
    ImageView FoodImageView;
    ImageView galambKep;

    // következők a swipe-barhoz tartozó cuccok
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    //---stepcounter--
    private SensorManager sensorManager;
    private boolean stepcounterIsRunning;
    private int step;
    Sensor countSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver2);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        //header
        View header = getLayoutInflater().inflate(R.layout.list_header, null);
        mDrawerList.addHeaderView(header);

        loadImagesToActivities();

        //Felület elemeinek elérése
        kivalasztottkajaText = (TextView) findViewById(R.id.selecetedFoodTextview);
        FoodImageView = (ImageView) findViewById(R.id.FoodImageview);
        galambKep = (ImageView) findViewById(R.id.galambImageView);
        galambKep.setClickable(true);

        //képre toutcholva etetünk
        galambKep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EatSomeFood();
            }
        });

        setupToolbar();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if(galamb.getMitcsinal() != 1) // ha nem mozog
                {
                    TevekenysegValtas(galamb.getMitcsinal());
                    SvipehezBeallitas();
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        //Tárhelyről betölti, ha ezután null akkor új galambot kell csinálni
        LoadFromStorage();

        //region Létrehozás ha nem létezik
        if (galamb == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Galamb létrehozás");
            alert.setMessage("Add meg a galambod nevét:");
            final EditText edittext = new EditText(this);
            //Egy edittext lesz a dialogban
            alert.setView(edittext);

            alert.setPositiveButton("Létrehozás", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    String input = edittext.getText().toString().trim();
                    if (!input.equals("")) {
                        galamb = new Galamb(input);
                        Toast.makeText(MainActivity.this, "Sikeres létrehozás!", Toast.LENGTH_SHORT).show();
                        LoadLayoutElementsOnStart();
                        SaveToStorage();
                    }
                }
            });
            alert.show();
        } else {
            LoadLayoutElementsOnStart();
        }
        //endregion

        Button boltgomb = (Button) findViewById(R.id.OpenStoreActivity);
        boltgomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoltActivityMegnyitas();
            }
        });

        Button kajagomb = (Button) findViewById(R.id.FoodSelectionButton);
        kajagomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KajaDialog();
            }
        });

        Button tevekenyseggomb = (Button) findViewById(R.id.SelectActivityButton);
        tevekenyseggomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TevekenysegDialog();
            }
        });



        Button OpenStepCounterLogActivitygomb = (Button) findViewById(R.id.OpenStepCounterLogActivity);
        OpenStepCounterLogActivitygomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenStepCounterLogActivitySelect();
            }
        });
    }

    private void loadImagesToActivities() {
        //mintha hashmap lenne csak optimálisabb
        ImagesToActivities = new SparseIntArray();
        ImagesToActivities.put(0, R.drawable.alszik);
        ImagesToActivities.put(1, R.drawable.sportol);
        ImagesToActivities.put(2, R.drawable.tanul);
        ImagesToActivities.put(3, R.drawable.telefonozik);
        ImagesToActivities.put(4, R.drawable.olvas);
        ImagesToActivities.put(5, R.drawable.lazul);
        ImagesToActivities.put(6, R.drawable.zenethallgat);
        ImagesToActivities.put(7, R.drawable.dolgozas);
    }

    private void OpenStepCounterLogActivitySelect()
    {
        Intent intent = new Intent(MainActivity.this, StepCounterLogAvtivity.class);
        intent.putExtra("StepCountList", (Serializable) galamb.getPreviousSteps());
        this.startActivityForResult(intent,2);
    }


    private void TevekenysegDialog() {
        final CharSequence[] activities = new CharSequence[Galamb.ezeketcsinalhatja.length];
        for (int i = 0; i < Galamb.ezeketcsinalhatja.length; i++) {
            activities[i] = Galamb.ezeketcsinalhatja[i];
        }
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Tevékenység kiválasztás");
        alt_bld.setSingleChoiceItems(activities, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(),
                        "A kiválasztott tevékenység: " + activities[item], Toast.LENGTH_SHORT).show();
                TextView tevekenysegTextView = (TextView) findViewById(R.id.selectActivity);
                if (item >= 0) {
                    TevekenysegValtas(item);
                }
                tevekenysegTextView.setText("Jelenlegi tevékenység: " + galamb.ezeketcsinalhatja[galamb.getMitcsinal()]);
                dialog.dismiss();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void TevekenysegValtas(int TevekenysegID) {
        if (galamb.getMitcsinal() == 1) //itt még nincs átállítva a tevékenyésg a kiválasztottra, ezért ha eddig a "mozgáson" volt akkor belép
        {
            galamb.Mozgas(DateTime.now(), step);
            step = 0;
        }
        if (TevekenysegID == 1) { //mozgás
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                stepcounterIsRunning = true;
                step = 0;
                sensorManager.registerListener(MainActivity.this, countSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                sensorManager = null;
                countSensor = null;
                stepcounterIsRunning = false;
            }
        } else {
            sensorManager = null;
            countSensor = null;
            stepcounterIsRunning = false;
            galamb.DoveActivityChange(TevekenysegID, DateTime.now());
        }
        galamb.setMitcsinal(TevekenysegID);
        KepValtas();
    }

    private void KajaDialog() {
        List<String> seged = new ArrayList<>();
        for (int i = 0; i < Store.getFoods().size(); i++) {
            if ((int) galamb.getKajamennyiseg().get(Store.getFoods().get(i).getName()) != 0)
            // megnézi hogy a galamb kajadictionaryjében az adott kajából van-e neki. Erre a store cikkek nevét használja kulcsnak. (Mindkét helyen ugyanazok a kaják szerepelnek.)
            {
                seged.add(Store.getFoods().get(i).getName() +
                        " (" + galamb.getKajamennyiseg().get(Store.getFoods().get(i).getName()) + ")");
            }
        }

        final CharSequence[] kajaradio = new CharSequence[seged.size()];
        for (int i = 0; i < seged.size(); i++) {
            kajaradio[i] = seged.get(i);
        }


        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Étel kiválasztás");
        alt_bld.setSingleChoiceItems(kajaradio, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //csúnya :(
                for (int i = 0; i < Store.getFoods().size(); i++) {
                    if (Store.getFoods().get(i).getName().equals(kajaradio[item].toString().split(" ")[0])) {

                        galamb.setSelectedFood(i);
                        break;
                    }
                }
                if (galamb.getSelectedFood() != -1) {
                    SelectedFoodShow();
                }

                dialog.dismiss();

            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();


    }

    private void BoltActivityMegnyitas() {
        Intent intent = new Intent(MainActivity.this, StoreActivity.class);
        intent.putExtra("galamb", galamb);
        this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                galamb = (Galamb) data.getSerializableExtra("galamb");
//                galamb.setKajamennyiseg(((Galamb)data.getSerializableExtra("galamb")).getKajamennyiseg());
                SaveToStorage();
            }
        }
        else if (requestCode == 2)
        {
            if(resultCode == RESULT_OK)
            {
                galamb.setPreviousSteps((List<StepCounterLog>) data.getSerializableExtra("lista"));
                SaveToStorage();
            }
        }
    }

    private void LoadLayoutElementsOnStart() {
        TextView nev = (TextView) findViewById(R.id.galambNameTextview);
        nev.setText(galamb.getNev());
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView actualActivity = (TextView) findViewById(R.id.selectActivity);
        actualActivity.setText("Jelenlegi tevékenység: " + galamb.ezeketcsinalhatja[galamb.getMitcsinal()]);
        KepValtas();
        SelectedFoodShow();
        PropertyAdapter adapter = ListaFeltoltesEsAdapterreKonvertalas();
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(adapter);
        SvipehezBeallitas();
    }

    //Kiválasztott étel megjelenítése
    void SelectedFoodShow() {
        if (0 <= galamb.getSelectedFood() && galamb.getSelectedFood() <= Store.getFoods().size()) {

            String nameOfFood = Store.getFoods().get(galamb.getSelectedFood()).getName();
            String amountOfFood = galamb.getKajamennyiseg().get(nameOfFood).toString();
            String s = "Kiváálasztott étel: " + nameOfFood + " (" + amountOfFood + ")";
            kivalasztottkajaText.setText(s);
            FoodImageView.setImageResource(Store.getFoods().get(galamb.getSelectedFood()).getImageID());
        } else {
            kivalasztottkajaText.setText("Nincs kiválasztva étel!");
            FoodImageView.setImageDrawable(null);
        }
    }

    private void KepValtas() {
        galamb.setKepId(ImagesToActivities.get(galamb.getMitcsinal()));
        galambKep.setImageResource(galamb.getKepId());
    }

    private void EatSomeFood() {
        if (0 <= galamb.getSelectedFood() && galamb.getSelectedFood() <= Store.getFoods().size()) {
            Food food = Store.getFoods().get(galamb.getSelectedFood());
            int amountOfSelectedFood = (int) galamb.getKajamennyiseg().get(food.getName());
            if (amountOfSelectedFood > 0) {
                galamb.Eves(food.getNutrient());
                galamb.KajaFogyasztas(food.getName());
                Toast.makeText(getApplicationContext(), "nyammm", Toast.LENGTH_SHORT).show();
                //Ha elfogyott
                if (amountOfSelectedFood == 1) {
                    galamb.setSelectedFood(-1);
                    Toast.makeText(getApplicationContext(), "Az étel elfogyott", Toast.LENGTH_SHORT).show();
                }
                SelectedFoodShow();
            }
        }
    }

    private PropertyAdapter ListaFeltoltesEsAdapterreKonvertalas() {
        List<ListItemDataModel> drawerItem = new ArrayList<>(Galamb.ENNYIPROPERTYVAN);


        for (int i = 0; i < Galamb.ENNYIPROPERTYVAN; i++) {
            double thisValue = 0;

            if (mNavigationDrawerItemTitles[i].equals("Egészség"))
                thisValue = galamb.getEgeszseg();
            else if (mNavigationDrawerItemTitles[i].equals("Jóllakottság"))
                thisValue = galamb.getJollakottsag();
            else if (mNavigationDrawerItemTitles[i].equals("Kipihentség"))
                thisValue = galamb.getKipihentseg();
            else if (mNavigationDrawerItemTitles[i].equals("Fittség"))
                thisValue = galamb.getFittseg();
            else if (mNavigationDrawerItemTitles[i].equals("Intelligencia"))
                thisValue = galamb.getIntelligencia();
            else if (mNavigationDrawerItemTitles[i].equals("Kedélyállapot"))
                thisValue = galamb.getKedelyallapot();

            drawerItem.add(new ListItemDataModel
                    (
                            mNavigationDrawerItemTitles[i],
                            thisValue
                    ));
        }
        return new PropertyAdapter(drawerItem);
    }

    private void SvipehezBeallitas() {
        mDrawerList.setAdapter(ListaFeltoltesEsAdapterreKonvertalas());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        mDrawerLayout.post(new Runnable() {

            @Override
            public void run() {
                mDrawerToggle.syncState();
            }

        });
    }

    //Jsont használ a mentés és betöltés így modell változás esetén nem vesznek el az adatok
    private void SaveToStorage() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(FILENAME, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
//            Gson gson = new Gson();
            Gson gson = Converters.registerDateTime(new GsonBuilder()).create();
            String str = gson.toJson(galamb);
            oos.writeObject(str);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadFromStorage() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String str = (String) ois.readObject();
//            Gson gson = new Gson();
            Gson gson = Converters.registerDateTime(new GsonBuilder()).create();
            Galamb test = gson.fromJson(str, Galamb.class);
            if (test != null && !test.getNev().equals(""))
                galamb = test;
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaveToStorage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadFromStorage();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    // --------------------------stepcounter -----------------------------------
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepcounterIsRunning) {
            step++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

