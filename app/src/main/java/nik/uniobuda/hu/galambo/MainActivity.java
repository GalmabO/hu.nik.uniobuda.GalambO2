package nik.uniobuda.hu.galambo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Galamb galamb;
    private final String FILENAME = "GalambPeldany";


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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) { }

            @Override
            public void onDrawerOpened(View drawerView) {
                TevekenysegValtas(galamb.getMitcsinal());
                SvipehezBeallitas();
            }
            @Override
            public void onDrawerClosed(View drawerView) { }

            @Override
            public void onDrawerStateChanged(int newState) { }
        });

        Betoltes();

        if(galamb == null)
        {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Galamb létrehozás");
                alert.setMessage("Add meg a galambod nevét!");


                final EditText edittext = new EditText(this);

                alert.setView(edittext);

                alert.setPositiveButton("Létrehozás", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String input = edittext.getText().toString();
                        if (!input.equals("")) {
                            galamb = new Galamb(input);
                            TextView text = (TextView) findViewById(R.id.aa) ;
                            text.setText(galamb.getNev());
                            Toast.makeText(MainActivity.this, "Sikeres létrehozás!", Toast.LENGTH_LONG).show();
                            Feltolt(); // Kristóf: ezt azért írtam ide, mert különben csak frissítő gomb nyomására látszik a swipe lista meg a galamb
                            Mentes();
                        }
                    }
                });
                alert.show();
        }
        else
        {
            Feltolt();
        }


        Button button = (Button) findViewById(R.id.gomb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                galamb.setPenz(100);
            }
        });

        Button boltgomb = (Button) findViewById(R.id.bolt);
        boltgomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            BoltActivityMegnyitas();
            }
        });

        Button kajagomb = (Button) findViewById(R.id.kaja);
        kajagomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KajaDialog();
            }
        });

        Button tevekenyseggomb = (Button) findViewById(R.id.tevekenyseg);
        tevekenyseggomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TevekenysegDialog();
            }
        });

    }

    private void TevekenysegDialog()
    {
        final CharSequence[] tevekenysegradio= new CharSequence[Galamb.ezeketcsinalhatja.length];
        for ( int i = 0; i < Galamb.ezeketcsinalhatja.length; i++) {
            tevekenysegradio[i]=Galamb.ezeketcsinalhatja[i];
        }
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Tevékenység kiválasztás");
        alt_bld.setSingleChoiceItems(tevekenysegradio, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(),
                        "A kiválasztott tevékenység: "+tevekenysegradio[item], Toast.LENGTH_SHORT).show();
                TextView tevekenysegTextView = (TextView) findViewById(R.id.kivalasztottTevekenyseg);
                if(item >= 0)
                {
                    TevekenysegValtas(item);

                }

                tevekenysegTextView.setText(galamb.ezeketcsinalhatja[galamb.getMitcsinal()]);
                dialog.dismiss();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void TevekenysegValtas(int TevekenysegID)
    {
        if(galamb.getMitcsinal() == 1) //itt még nincs átállítva a tevékenyésg a kiválasztottra, ezért ha eddig a "mozgáson" volt akkor belép
        {

            galamb.Mozgas(Calendar.getInstance().getTime(),step);
            step=0;
        }
        if(TevekenysegID ==1)
        {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                stepcounterIsRunning=true;
                step=0;
                sensorManager.registerListener(MainActivity.this, countSensor, SensorManager.SENSOR_DELAY_UI);
            }
            else
            {
                sensorManager = null;
                countSensor=null;
                stepcounterIsRunning=false;
            }
        }
        else
        {
            sensorManager = null;
            countSensor=null;
            stepcounterIsRunning=false;
            galamb.DoveActivityChange(TevekenysegID, Calendar.getInstance().getTime());
        }
        galamb.setMitcsinal(TevekenysegID);
        KepValtas();
    }

    private void KajaDialog()
    {
        List<String> seged = new ArrayList<>();
        for ( int i = 0; i < Store.getCikkek().size(); i++)
        {
            if((int)galamb.getKajamennyiseg().get(Store.getCikkek().get(i).getNev()) != 0)
                // megnézi hogy a galamb kajadictionaryjében az adott kajából van-e neki. Erre a store cikkek nevét használja kulcsnak. (Mindkét helyen ugyanazok a kaják szerepelnek.)
            {
                seged.add(Store.getCikkek().get(i).getNev());
            }
        }

        final CharSequence[] kajaradio= new CharSequence[seged.size()];
        for ( int i = 0; i < seged.size(); i++) {
            kajaradio[i]=seged.get(i);
        }


        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Étel kiválasztás");
        alt_bld.setSingleChoiceItems(kajaradio, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                for (int i= 0; i<Store.getCikkek().size();i++)
                {
                    if(Store.getCikkek().get(i).getNev().equals(kajaradio[item]))
                    {

                        galamb.setSelectedFood(i);
                        break;
                    }
                }
                //TextView kivalasztottkajaText=(TextView)findViewById(R.id.kivalasztottEtel);
                ImageView kajaKep = (ImageView) findViewById(R.id.kajakep);
                if(galamb.getSelectedFood() != -1)
                {
                    //kivalasztottkajaText.setText(Store.getCikkek().get(galamb.getSelectedFood()).getNev().toString());
                    //kivalasztottkajaText.setText("");
                    //Drawable image = getResources().getDrawable(Store.getCikkek().get(galamb.getSelectedFood()).getKepID(),null);
                    //kivalasztottkajaText.setCompoundDrawablesWithIntrinsicBounds(null,null,image,null);
                    kajaKep.setImageResource(Store.getCikkek().get(galamb.getSelectedFood()).getKepID());
                }

                dialog.dismiss();

            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();



    }

    private void BoltActivityMegnyitas()
    {
        Intent intent = new Intent(MainActivity.this,StoreActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable("galamb", galamb);
        extras.putSerializable("dictionary", (Serializable) galamb.getKajamennyiseg());
        intent.putExtras(extras);
        this.startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                galamb = (Galamb)data.getParcelableExtra("result");
                galamb.setKajamennyiseg((HashMap) data.getSerializableExtra("dictionary"));
                Mentes();
            }
        }
    }

    private void Feltolt()
    {
        TextView nev = (TextView) findViewById(R.id.aa);
        nev.setText(galamb.getNev());

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        KepValtas();

        PropertyAdapter adapter = ListaFeltoltesEsAdapterreKonvertalas();
        ListView lista = (ListView) findViewById(R.id.left_drawer);
        lista.setAdapter(adapter);

        SvipehezBeallitas();

        //TextView kivalasztottkajaText=(TextView)findViewById(R.id.kivalasztottEtel);
        ImageView kajaKep = (ImageView) findViewById(R.id.kajakep);
        if(0<=galamb.getSelectedFood() && galamb.getSelectedFood()<=Store.getCikkek().size())
        {

            //kivalasztottkajaText.setText(Store.getCikkek().get(galamb.getSelectedFood()).getNev().toString());
            //Drawable image = getResources().getDrawable(Store.getCikkek().get(galamb.getSelectedFood()).getKepID(),null);
            //kivalasztottkajaText.setCompoundDrawablesWithIntrinsicBounds(null,null,image,null);
            kajaKep.setImageResource(Store.getCikkek().get(galamb.getSelectedFood()).getKepID());
        }
        else
        {
            //kivalasztottkajaText.setText("Nincs kiválasztva étel!");
            //kivalasztottkajaText.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

        }

    }

    private void KepValtas()
    {
        ImageView galambKep = (ImageView) findViewById(R.id.galamb_kep);
        galambKep.setClickable(true);

        if (galamb.getMitcsinal() == (0))
        {
            galamb.setKepId(R.drawable.alszik);
        }
        else if(galamb.getMitcsinal() == 1)
        {
            galamb.setKepId(R.drawable.sportol);
        }
        else if(galamb.getMitcsinal()== 2)
        {
            galamb.setKepId(R.drawable.tanul);
        }
        else if(galamb.getMitcsinal() == 3 )
        {
            galamb.setKepId(R.drawable.telefonozik);
        }
        else if(galamb.getMitcsinal() == 4)
        {
            galamb.setKepId(R.drawable.olvas);
        }
        else if(galamb.getMitcsinal() == 5)
        {
            galamb.setKepId(R.drawable.lazul);
        }
        else if(galamb.getMitcsinal() == 6)
        {
            galamb.setKepId(R.drawable.zenethallgat);
        }
        else if(galamb.getMitcsinal() == 7)
        {
            galamb.setKepId(R.drawable.dolgozas);
        }
        else
        {
            galamb.setKepId(R.drawable.sima_galamb_3);
        }
        galambKep.setImageResource(galamb.getKepId());
        galambKep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etetes();
            }
        });
    }

    private void Etetes()
    {
        if(0<=galamb.getSelectedFood() && galamb.getSelectedFood()<=Store.getCikkek().size())
        {
            Food kaja = Store.getCikkek().get(galamb.getSelectedFood());
            if((int)galamb.getKajamennyiseg().get(kaja.getNev())>0)
            {
                galamb.Eves(kaja.getTapanyagmennyiseg());
                galamb.KajaFogyasztas(kaja.getNev());
                Toast.makeText(getApplicationContext(),"nyamm", Toast.LENGTH_SHORT).show();

                if(((int)galamb.getKajamennyiseg().get(kaja.getNev())==0))
                {
                    TextView kajatextview = (TextView)findViewById(R.id.kivalasztottEtel);
                    kajatextview.setText("Nincs kiválasztva étel!");
                    kajatextview.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    galamb.setSelectedFood(-1);
                    Toast.makeText(getApplicationContext(),"Az étel elfogyott", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private PropertyAdapter ListaFeltoltesEsAdapterreKonvertalas()
    {
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

    private void SvipehezBeallitas()
    {
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

    private void Mentes()
    {

        FileOutputStream fos = null;
        ObjectOutputStream oos  = null;
        try {
            fos = openFileOutput(FILENAME, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(galamb);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void Betoltes()
    {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            Galamb teszt = (Galamb) is.readObject();
            if(teszt!= null && !teszt.getNev().equals(""))
                galamb=teszt;
            is.close();
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
        Mentes();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Betoltes();


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

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    // --------------------------stepcounter -----------------------------------
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(stepcounterIsRunning)
        {
            step++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

