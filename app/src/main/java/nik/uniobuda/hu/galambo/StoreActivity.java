package nik.uniobuda.hu.galambo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private final List<Food> foods = Store.getCikkek();
    private int jatekospenze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store);


        Feltolt();


        jatekospenze = getIntent().getExtras().getInt("penz");
        TextView jatekospenzview = (TextView) findViewById(R.id.jatekospenze);
        Object seged = jatekospenze;
        jatekospenzview.setText(seged.toString());

    }

    public boolean Vásárlás(Food melyiketvalasztotta)
    {
        if(jatekospenze >= melyiketvalasztotta.getAr())
            return true;
        else
            return false;
    }


    private void Feltolt()
    {
        KajaAdapter adapter = new KajaAdapter(foods);
        GridView lista = (GridView) findViewById(R.id.araslista);
        lista.setAdapter(adapter);
    }
}
