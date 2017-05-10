package nik.uniobuda.hu.galambo;

import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adam on 2017. 04. 05..
 */

public class PropertyAdapter extends BaseAdapter {

    //első érték a value, második a property neve
    private List<ListItemDataModel> items;

    public PropertyAdapter(List<ListItemDataModel> items) {
        this.items = items;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderPropertyAdapter holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.listitem_adatok, null);
            holder = new ViewHolderPropertyAdapter();
            holder.arrow = (ImageView) convertView.findViewById(R.id.doveArrow);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.doveBar);
            convertView.setTag(holder);
        } else
            holder = (ViewHolderPropertyAdapter) convertView.getTag();

        TextView propertyTextView = (TextView) convertView.findViewById(R.id.property);
        ListItemDataModel tul = items.get(position);
        double mennyi = 0;

        if (tul.getValue() >= 100)
            mennyi = 99;
        else if (tul.getValue() <= -100)
            mennyi = -99;
        else
            mennyi = tul.getValue();

        mennyi = mennyi + 100;
        ImageView doveArrow = (ImageView) convertView.findViewById(R.id.doveArrow);
        android.view.ViewGroup.MarginLayoutParams marginParams = (android.view.ViewGroup.MarginLayoutParams) doveArrow.getLayoutParams();

        mennyi = mennyi - 10;
        marginParams.setMargins(dpToPx(mennyi), marginParams.topMargin, marginParams.rightMargin, marginParams.bottomMargin);
        RelativeLayout doveBar = (RelativeLayout) convertView.findViewById(R.id.doveBar);
        doveBar.requestLayout();
        //első érték a value, második a property neve
        propertyTextView.setText(tul.getProp().toString());
        return convertView;
    }

    public static int dpToPx(double dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
