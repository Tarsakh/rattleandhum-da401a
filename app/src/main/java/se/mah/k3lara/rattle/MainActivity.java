package se.mah.k3lara.rattle;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener, ValueEventListener {

    private static Firebase myFirebaseRef;
    private static final String TAG = "MainActivity";
    private static String id = "7";
    private SensorManager mSensorManager;
    private Sensor mAccelerolmeter;
    private static int triggervalue = 7;
    private static float maxvalue = triggervalue; //used to norm the output
    private static Vibrator mVibrator;
    private static Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get vibrator
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        /*Get sensors*/
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerolmeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        /*Firebase*/
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://blinding-heat-7399.firebaseio.com/"+id);
        myFirebaseRef.child("rattle").addValueEventListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerolmeter, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    //Accelerometer stuff
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float meanValue = Math.abs((event.values[0] + event.values[1] + event.values[2])/3);
            if (meanValue >triggervalue){
                if (maxvalue < meanValue){
                    maxvalue = meanValue;
                }
                //float vibrateTime = 100*((meanValue-triggervalue)/(maxvalue-triggervalue)); // 0-100ms
                //myFirebaseRef.child("rattle").setValue(Math.round(vibrateTime)); //send

                int i = rand.nextInt(5);
                myFirebaseRef.child("rattle").setValue(i);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    //Firebase stuff
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        int i = 0;
        try {
            i = Integer.parseInt(dataSnapshot.getValue().toString());
            //mVibrator.vibrate(i);
            }
         catch (Exception e) {
            e.printStackTrace();
        }
        PutColor(i);
    }




    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    public void PutColor(int i) {
        TextView backgroundColor = (TextView) findViewById(R.id.rattleBackground);
        switch (i) {
            case 0:
                backgroundColor.setBackgroundColor(Color.RED);
                break;
            case 1:
                backgroundColor.setBackgroundColor(Color.CYAN);
                break;
            case 2:
                backgroundColor.setBackgroundColor(Color.YELLOW);
                break;
            case 3:
                backgroundColor.setBackgroundColor(Color.BLUE);
                break;
            case 4:
                backgroundColor.setBackgroundColor(Color.BLACK);
                break;
            case 5:
                backgroundColor.setBackgroundColor(Color.LTGRAY);
                break;
            case 6:
                backgroundColor.setBackgroundColor(Color.rgb(10, 10, 10));
                break;
            case 7:
                backgroundColor.setBackgroundColor(Color.rgb(255, 255, 255));
                break;
            case 8:
                backgroundColor.setBackgroundColor(Color.rgb(100, 100, 100));
                break;
            case 9:
                backgroundColor.setBackgroundColor(Color.rgb(120, 120, 120));
                break;

            default:
                break;
        }
    }
}
