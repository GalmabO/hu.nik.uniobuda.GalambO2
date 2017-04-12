package nik.uniobuda.hu.galambo;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private static final List<Food> foods = Store.getCikkek();
    private static Galamb galamb;
    private GridView grid;
    private KajaAdapter adapter;
    private static TextView jatekospenzview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store);


        Feltolt();

        galamb = (Galamb)getIntent().getExtras().get("galamb");



        jatekospenzview = (TextView) findViewById(R.id.jatekospenze);
        jatekospenzview.setText(String.valueOf(galamb.getPenz()));

    }


    public static boolean Vasarlas(String melyiketvalasztotta)
    {
        Food valasztott = null;
        
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
            {
                if(galamb.getPenz() >= valasztott.getAr())
                {
                    galamb.KajaVasarlas(melyiketvalasztotta);
                    galamb.setPenz(-valasztott.getAr());

                    jatekospenzview.setText(String.valueOf(galamb.getPenz()));
                    return true;
                }
            }
        }
        return false;
    }


    private void Feltolt()
    {
        adapter = new KajaAdapter(foods);
        grid = (GridView) findViewById(R.id.araslista);
        grid.setAdapter(adapter);

    }



    @Override
    public void onPause() {

        Intent intent = new Intent();
        intent.putExtra("result", (Parcelable) galamb);
        this.setResult(RESULT_OK, intent);
        finish();

        super.onPause();

    }
}
