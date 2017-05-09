package nik.uniobuda.hu.galambo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adam on 2017. 05. 05..
 */

public class StepCounterLogAdapter extends BaseAdapter
{
    private List<StepCounterLog> items;

    public StepCounterLogAdapter(List<StepCounterLog> items) { this.items = items; }
    @Override
    public int getCount() { return items == null ? 0 : items.size();}

    @Override
    public Object getItem(int position) {  return items == null ? null : items.get(position);  }

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderStepCounterLogAdapter holder;

        if (convertView == null)
        {
            convertView = View.inflate(parent.getContext(), R.layout.stepcounterlog, null);
            holder= new ViewHolderStepCounterLogAdapter();
            holder.minutes= (TextView) convertView.findViewById(R.id.time);
            holder.steps= (TextView) convertView.findViewById(R.id.step);
            convertView.setTag(holder);
        }
        else
            holder= (ViewHolderStepCounterLogAdapter) convertView.getTag();

        //Elemek elérése
        TextView timetextview = (TextView) convertView.findViewById(R.id.time);
        TextView steptextview = (TextView) convertView.findViewById(R.id.step);

        timetextview.setText(String.valueOf(items.get(position).getTimeInFormat()));
        steptextview.setText(String.valueOf(items.get(position).getStepCount())+ " lépés");

        return convertView;
    }
}
