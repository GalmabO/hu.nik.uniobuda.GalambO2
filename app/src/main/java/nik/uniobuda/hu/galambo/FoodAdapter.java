package nik.uniobuda.hu.galambo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adam on 2017. 04. 06..
 */

public class FoodAdapter extends BaseAdapter {

    private List<Food> items;
    private Context appContext;

    public FoodAdapter(List<Food> items, Context appContext) {
        this.items = items;
        this.appContext = appContext;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO újrahasznosítás
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = View.inflate(parent.getContext(), R.layout.listitem_food, null);

        //Elemek elérése
        TextView nevTextView = (TextView) listItemView.findViewById(R.id.FoodNameTextBox);
        ImageView foodImageView = (ImageView) listItemView.findViewById(R.id.FoodImageView);
        Food food = items.get(position);
        Drawable image = appContext.getResources().getDrawable(food.getImageID(), null);

        //Értékek beállítása
        nevTextView.setText(food.getName());
        foodImageView.setImageDrawable(image);

        return listItemView;
    }
}
