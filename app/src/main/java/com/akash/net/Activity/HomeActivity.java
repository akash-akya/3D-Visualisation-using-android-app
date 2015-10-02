package com.akash.net.Activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.akash.net.R;

import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {

    private ArrayList<Integer> sensorList  = new ArrayList<>();
//    private ArrayList<Integer> sensorCBId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayout featuresTable = (LinearLayout) findViewById(R.id.sensor_list);

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)){

            CheckBox sensorCheckBox = new CheckBox(this);
            sensorCheckBox.setText(sensor.getName());
            sensorCheckBox.setId(sensor.getType());
            sensorCheckBox.setPadding(5,5,5,5);
            featuresTable.addView(sensorCheckBox);

            sensorList.add(sensor.getType());
//            sensorCBId.add(sensorCheckBox.getId());
        }

        findViewById(R.id.send_button).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.send_button)
        {
            String address = ((EditText) findViewById(R.id.address_box)).getText().toString();

            if (!address.isEmpty())
            {
                Intent intent = new Intent(HomeActivity.this, DataTransfer.class);
                ArrayList<Integer> sensorsName = new ArrayList<>();

//                int i = 0;
//                Log.w("CUBOID", sensorCBId.toString());
                for (int cbid : sensorList){
                    CheckBox cb = ((CheckBox) findViewById(cbid));
                    if (cb.isChecked()) {
                        sensorsName.add(cbid);
                        Log.w("CUBOID", cb.getText().toString());
                    }
//                    i++;
                }

                intent.putExtra("SENSORS", sensorsName);
                intent.putExtra("ADDR", address);
                startActivity(intent);
            }
        }
    }
}
