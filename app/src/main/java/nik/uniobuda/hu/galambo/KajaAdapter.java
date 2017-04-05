package nik.uniobuda.hu.galambo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adam on 2017. 04. 06..
 */

public class KajaAdapter extends BaseAdapter {

    //Első a név, második az ár
    private List<Object[]> items;

    public KajaAdapter(List<Object[]> items) { this.items = items; }

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
        TextView arTextView = (TextView) listItemView.findViewById(R.id.ar);

        //első érték a value, második a property neve
        Object [] tul = items.get(position);
        nevTextView.setText(tul[0].toString());
        arTextView.setText(tul[1].toString());

        return listItemView;
    }
}
