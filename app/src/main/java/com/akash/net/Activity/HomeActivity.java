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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayout availableSensors = (LinearLayout) findViewById(R.id.sensor_list);

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)){

            CheckBox sensorCheckBox = new CheckBox(this);
            sensorCheckBox.setText(sensor.getName());
            sensorCheckBox.setId(sensor.getType());
            sensorCheckBox.setTextAppearance(this, R.style.Base_TextAppearance_AppCompat_Medium);
            sensorCheckBox.setPadding(5, 5, 5, 5);
            availableSensors.addView(sensorCheckBox);

            sensorList.add(sensor.getType());
        }

        findViewById(R.id.send_button).setOnClickListener(this);
        findViewById(R.id.reset_button).setOnClickListener(this);
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
            String delay = ((EditText) findViewById(R.id.time)).getText().toString();
            long interval = 500;
            if (!delay.isEmpty())
                interval = Long.parseLong(delay);

            Intent intent = new Intent(HomeActivity.this, DataTransfer.class);
            ArrayList<Integer> sensorsName = new ArrayList<>();

            for (int cbid : sensorList){
                CheckBox cb = ((CheckBox) findViewById(cbid));
                if (cb.isChecked()) {
                    sensorsName.add(cbid);
                    Log.w("CUBOID", cb.getText().toString());
                }
            }

            intent.putExtra("SENSORS", sensorsName);
            intent.putExtra("TIME", interval);
            intent.putExtra("ADDR", address);
            startActivity(intent);

        }

        if(id == R.id.reset_button){
            ((EditText) findViewById(R.id.address_box)).setText("");
            ((EditText) findViewById(R.id.time)).setText("");

            for (int cbid : sensorList){
                ((CheckBox) findViewById(cbid)).setChecked(false);
            }
        }
    }
}
