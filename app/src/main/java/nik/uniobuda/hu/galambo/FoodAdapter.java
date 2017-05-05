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
        ViewHolderFoodAdapter holder;


        if (convertView == null)
        {
            convertView = View.inflate(parent.getContext(), R.layout.listitem_food, null);
            holder= new ViewHolderFoodAdapter();
            holder.FoodName= (TextView) convertView.findViewById(R.id.FoodNameTextBox);
            holder.FoodImage= (ImageView) convertView.findViewById(R.id.FoodImageView);
            convertView.setTag(holder);
        }
        else
            holder= (ViewHolderFoodAdapter) convertView.getTag();

        //Elemek elérése
        TextView nevTextView = (TextView) convertView.findViewById(R.id.FoodNameTextBox);
        ImageView foodImageView = (ImageView) convertView.findViewById(R.id.FoodImageView);
        Food food = items.get(position);
        Drawable image = appContext.getResources().getDrawable(food.getImageID(), null);

        //Értékek beállítása
        nevTextView.setText(food.getName());
        foodImageView.setImageDrawable(image);

        return convertView;
    }
}
