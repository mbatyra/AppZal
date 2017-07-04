package com.example.mikolaj.appzal;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor swiatlo;
    private Sensor magnetometr;
    private Sensor akcelerometr;

    private float[] wswiatla;
    private float[] wmagnetometr;
    private float[] wakcelometr;

    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;

    Button button;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = (TextView) findViewById(R.id.textView1);
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        magnetometr = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        swiatlo = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        akcelerometr = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String FILENAME ="pomiar.txt";
                String pomiary;

                String f1;
                String f2;
                String f3;

                f1 = String.format("%.0f", wakcelometr[0]);
                f2 = String.format("%.0f", wakcelometr[1]);
                f3 = String.format("%.0f", wakcelometr[2]);

                String m1;
                String m2;
                String m3;

                m1 = String.format("%.0f", wmagnetometr[0]);
                m2 = String.format("%.0f", wmagnetometr[1]);
                m3 = String.format("%.0f", wmagnetometr[2]);

                pomiary = "Akcelometr: X: "+f1+" Y: "+f2+" Z: "+f3 +"\nPole Magnetyczne: X:"+m1+" Y: "+m2+" Z: "+m3 + " μT"
                            + "\nOswietlenie: " +wswiatla[0]+" lx";

                FileOutputStream fos;
                try {
                    fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(pomiary.getBytes());
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Zapisano", Toast.LENGTH_LONG).show();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                StringBuilder text = new StringBuilder();
                try {
                    InputStream instream = openFileInput("pomiar.txt");
                    if (instream != null) {
                        InputStreamReader inputreader = new InputStreamReader(instream);
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line=null;
                        while (( line = buffreader.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }}}catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Wczytano", Toast.LENGTH_LONG).show();

                TextView tv = (TextView)findViewById(R.id.textView1);
                t4.setText(text);

            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();

        mSensorManager.registerListener(this, akcelerometr, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometr, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, swiatlo, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event){

       if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
           wakcelometr = new float[3];
           System.arraycopy(event.values, 0, wakcelometr, 0, 3);
           String f1;
           String f2;
           String f3;

           f1 = String.format("%.0f", wakcelometr[0]);
           f2 = String.format("%.0f", wakcelometr[1]);
           f3 = String.format("%.0f", wakcelometr[2]);

           t1.setText("Akcelometr: X:"+f1+" Y: "+f2+" Z: "+f3);

        }

        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            wmagnetometr= new float[3];
           System.arraycopy(event.values, 0, wmagnetometr, 0, 3);
           String m1;
           String m2;
           String m3;

           m1 = String.format("%.0f", wmagnetometr[0]);
           m2 = String.format("%.0f", wmagnetometr[1]);
           m3 = String.format("%.0f", wmagnetometr[2]);

           t2.setText("Pole Magnetyczne: X:"+m1+" Y: "+m2+" Z: "+m3 + " μT");

        }
        else if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            wswiatla=new float[1];
            System.arraycopy(event.values, 0, wswiatla, 0, 1);
            t3.setText("Oswietlenie: " +wswiatla[0]+" lx");


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
