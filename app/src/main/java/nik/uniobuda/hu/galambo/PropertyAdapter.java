package nik.uniobuda.hu.galambo;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
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
    private List<Object[]> items;

    public PropertyAdapter(List<Object[]> items) { this.items = items; }

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
            listItemView= View.inflate(parent.getContext(), R.layout.listitem_adatok,null);

        TextView propertyTextView = (TextView) listItemView.findViewById(R.id.property);


        Object [] tul = items.get(position);
        double mennyi = (double)tul[0];

        mennyi = mennyi + 100;
        ImageView doveArrow = (ImageView)listItemView.findViewById(R.id.doveArrow);
        android.view.ViewGroup.MarginLayoutParams marginParams = (android.view.ViewGroup.MarginLayoutParams) doveArrow.getLayoutParams();

        mennyi = mennyi - 10;

        marginParams.setMargins(dpToPx(mennyi), marginParams.topMargin, marginParams.rightMargin, marginParams.bottomMargin);

        RelativeLayout doveBar = (RelativeLayout) listItemView.findViewById(R.id.doveBar);
        doveBar.requestLayout();

        //első érték a value, második a property neve
        propertyTextView.setText(tul[1].toString());
        return listItemView;
    }

    public static int dpToPx(double dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
