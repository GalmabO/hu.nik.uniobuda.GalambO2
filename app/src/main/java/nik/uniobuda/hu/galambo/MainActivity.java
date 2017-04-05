package nik.uniobuda.hu.galambo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Galamb galamb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(galamb == null)
        {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Galamb létrehozás");
                alert.setMessage("Add meg a galambod nevét!");


                final EditText edittext = new EditText(this);

                alert.setView(edittext);

                alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
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

    @Override //TODO állapotokat le kell majd itt menteni
    protected void onPause() {
        super.onPause();
    }

    @Override //TODO állapotokat vissza kell majd tölteni
    protected void onResume() {
        super.onResume();
    }
}
