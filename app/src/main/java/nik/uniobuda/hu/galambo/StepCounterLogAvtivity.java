package nik.uniobuda.hu.galambo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class StepCounterLogAvtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter_log_avtivity);



        List<StepCounterLog> minutesandstepslist = (List<StepCounterLog>) getIntent().getSerializableExtra("StepCountList");


        StepCounterLogAdapter adapter = new StepCounterLogAdapter(minutesandstepslist);

        GridView MinutesandStepGrid = (GridView)findViewById(R.id.timeAndSteps);
        MinutesandStepGrid.setAdapter(adapter);
    }
}
