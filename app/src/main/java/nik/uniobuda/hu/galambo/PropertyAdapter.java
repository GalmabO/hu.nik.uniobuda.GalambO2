package nik.uniobuda.hu.galambo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        TextView valueTextView = (TextView) listItemView.findViewById(R.id.value);
        TextView propertyTextView = (TextView) listItemView.findViewById(R.id.property);

        //első érték a value, második a property neve
        Object [] tul = items.get(position);
        valueTextView.setText(tul[0].toString());
        propertyTextView.setText(tul[1].toString());

        return listItemView;
    }
}
