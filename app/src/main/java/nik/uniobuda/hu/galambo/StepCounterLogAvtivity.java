package nik.uniobuda.hu.galambo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.joda.time.Period;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StepCounterLogAvtivity extends AppCompatActivity {

     List<StepCounterLog> minutesandstepslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter_log_avtivity);
        Button clearButton = (Button)findViewById(R.id.clearButton);

        minutesandstepslist= (List<StepCounterLog>) getIntent().getSerializableExtra("StepCountList");
        if (minutesandstepslist==null)
        {
            minutesandstepslist = new ArrayList<StepCounterLog>();
        }
//        minutesandstepslist.add(new StepCounterLog(5000,new Period(25,6,8,4))); TESZT
//        minutesandstepslist.add(new StepCounterLog(2656,new Period(22,6,8,4)));
//        minutesandstepslist.add(new StepCounterLog(333,new Period(0,0,8,4)));
        final StepCounterLogAdapter adapter = new StepCounterLogAdapter(minutesandstepslist);
        final GridView MinutesandStepGrid = (GridView)findViewById(R.id.timeAndSteps);
        MinutesandStepGrid.setAdapter(adapter);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minutesandstepslist != null)
                {
                    minutesandstepslist.clear();
                }else
                {
                    Toast.makeText(StepCounterLogAvtivity.this, "Nincs törlendő elem",Toast.LENGTH_SHORT).show();
                }
                MinutesandStepGrid.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("lista", (Serializable) minutesandstepslist);
        this.setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}
