package nik.uniobuda.hu.galambo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Galamb galamb;
    private final String FILENAME = "GalambPeldany";
    private static Context mContext;

    public static Context getContext()
    {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mContext = this;
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
                galamb.setJollakottsag(10);
                galamb.setPenz(100);
            }
        });

        Button frissitogomb = (Button) findViewById(R.id.feltolto);
        frissitogomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feltolt();
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

            }
        });

    }

    private void KajaDialog()
    {
         List<String> seged = new ArrayList<>();


        for ( int i = 0; i < Store.getCikkek().size(); i++)
        {
            if((int)galamb.getKajamennyiseg().get(Store.getCikkek().get(i).getNev()) != 0) // megnézi hogy a galamb kajadictionaryjében az adott kajából van-e neki. Erre a store cikkek nevét használja kulcsnak. (Mindkét helyen ugyanazok a kaják szerepelnek.)
            {
                seged.add(Store.getCikkek().get(i).getNev());
            }
        }

        final CharSequence[] colors_radio= new CharSequence[seged.size()];
        for ( int i = 0; i < seged.size(); i++) {
            colors_radio[i]=seged.get(i);
        }


        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Étel kiválasztás");
        alt_bld.setSingleChoiceItems(colors_radio, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(),
                        "Group Name = "+colors_radio[item], Toast.LENGTH_SHORT).show();
                dialog.dismiss();// dismiss the alertbox after chose option

            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();



    }

    private void BoltActivityMegnyitas()
    {
        Intent intent = new Intent(MainActivity.this,StoreActivity.class);
        intent.putExtra("galamb", (Parcelable) galamb);
        this.startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                galamb = (Galamb)data.getParcelableExtra("result");
                Mentes();
            }
        }
    }

    private void Feltolt()
    {
        TextView nev = (TextView) findViewById(R.id.aa);
        nev.setText(galamb.getNev());

        List<Object[]> items = new ArrayList<>();
        items.add(new Object[]{galamb.getEgeszseg(),"Egészség"});
        items.add(new Object[]{galamb.getKedelyallapot(),"Kedélyállapot"});
        items.add(new Object[]{galamb.getJollakottsag(),"Jóllakottság"});
        items.add(new Object[]{galamb.getIntelligencia(),"Intelligencia"});
        items.add(new Object[]{galamb.getFittseg(),"Fittség"});
        items.add(new Object[]{galamb.getKipihentseg(),"Kipihentség"});

        PropertyAdapter adapter = new PropertyAdapter(items);
        GridView lista = (GridView) findViewById(R.id.lista);
        lista.setAdapter(adapter);
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
}
