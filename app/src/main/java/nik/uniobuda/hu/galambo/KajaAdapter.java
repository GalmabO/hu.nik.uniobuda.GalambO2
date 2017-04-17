package nik.uniobuda.hu.galambo;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adam on 2017. 04. 06..
 */

public class KajaAdapter extends BaseAdapter {

    //Első a név, második az ár
    private List<Food> items;

    public KajaAdapter(List<Food> items) { this.items = items; }

    @Override
    public int getCount()  {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position)  {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
            listItemView= View.inflate(parent.getContext(), R.layout.listitem_food,null);

        TextView nevTextView = (TextView) listItemView.findViewById(R.id.nev);
        TextView tapa = (TextView) listItemView.findViewById(R.id.tapanyagmennyiseg);

        TextView arTextView = (TextView) listItemView.findViewById(R.id.ar);

        TextView ures = (TextView) listItemView.findViewById(R.id.uressav);
        //Button vetelgomb = (Button) listItemView.findViewById(R.id.vetel);


        Food food = items.get(position);

        nevTextView.setText("Étel neve: "+ food.getNev());
        tapa.setText("Tápanyagtartalma: " + String.valueOf(food.getTapanyagmennyiseg()));
        arTextView.setText("Ár: "+ String.valueOf(food.getAr()));


        Drawable image = food.getKep();
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds( 0, 0, w, h );
        arTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,image);
        ures.setText(" ");
        //vetelgomb.setText("Vásárlás");
        //vetelgomb.setTag(items.get(position).getNev());

        /*
        vetelgomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = (String )v.getTag();
                StoreActivity.Vasarlas(a);
            }
        });
        */
        return listItemView;
    }
}
