package nik.uniobuda.hu.galambo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

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
                            Toast.makeText(MainActivity.this, "Fasza!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alert.show();




            //String input = edittext.getText().toString();
/*
            if (!input.equals("")) {
                galamb = new Galamb(input);
                Toast.makeText(MainActivity.this, "Fasza!", Toast.LENGTH_LONG).show();
            }*/
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


    }

    void BoltActivityMegnyitas()
    {
        Intent intent = new Intent(MainActivity.this,StoreActivity.class);
        intent.putExtra("penz",galamb.getPenz());
        startActivity(intent);

    }

    private void Feltolt()
    {
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

        FileOutputStream   fos  = null;
        ObjectOutputStream oos  = null;
        try {
            fos = new FileOutputStream(FILENAME);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(galamb);
        } catch (Exception e) {
        } finally {
            try {
                if (oos != null)   oos.close();
                if (fos != null)   fos.close();
            } catch (Exception e) { }
        }
    }

    private void Betoltes()
    {

        FileInputStream fis = null;
        ObjectInputStream is = null;

        try {
            fis = new FileInputStream(FILENAME);
            is = new ObjectInputStream(fis);
            galamb = (Galamb) is.readObject();
        } catch(Exception e) {
            String val= e.getMessage();
        } finally {
            try {
                if (fis != null)   fis.close();
                if (is != null)   is.close();
            } catch (Exception e) { }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Mentes();
    }


    @Override //TODO állapotokat vissza kell majd tölteni
    protected void onResume() {
        super.onResume();


    }
}
