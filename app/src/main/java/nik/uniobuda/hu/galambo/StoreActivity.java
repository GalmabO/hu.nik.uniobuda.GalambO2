package nik.uniobuda.hu.galambo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Feltolt();


    }

    private void Feltolt()
    {
        List<Object[]> items = new ArrayList<>();

        Dictionary seged = Store.getCikkek();
        Enumeration s = seged.keys();

        Object[] tomb = new Object[seged.size()];
        int k =0;
        while(s.hasMoreElements())
        {
            tomb[k] = s.nextElement();
            k++;
        }

        for (int i = 0;i< seged.size();i++)
        {
            items.add(new Object[]{tomb[i],seged.get(tomb[i])});
        }

        KajaAdapter adapter = new KajaAdapter(items);
        GridView lista = (GridView) findViewById(R.id.araslista);
        lista.setAdapter(adapter);
    }
}
