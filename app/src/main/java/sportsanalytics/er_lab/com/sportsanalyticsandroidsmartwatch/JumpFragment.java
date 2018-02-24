package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link JumpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JumpFragment extends Fragment implements SensorEventListener{
    private static final String ARG_FIRST_NAME = "firstname";
    private static final String ARG_LAST_NAME = "lastname";
    private static final String ARG_TEAM = "teamname";

    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;

    private String mFirstName;
    private String mLastName;
    private Team mTeam;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorType;
    private long mShakeTime = 0;


    public JumpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment JumpFragment.
     */
    public static JumpFragment newInstance(String firstname, String lastname, Team team) {
        JumpFragment fragment = new JumpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FIRST_NAME, firstname);
        args.putString(ARG_LAST_NAME, lastname);
        args.putSerializable(ARG_TEAM, team);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFirstName = getArguments().getString(ARG_FIRST_NAME);
            mLastName = getArguments().getString(ARG_LAST_NAME);
            mTeam = (Team) getArguments().getSerializable(ARG_TEAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        return inflater.inflate(R.layout.fragment_jump, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        long now = System.currentTimeMillis();

        if((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            double x = sensorEvent.values[0];
            double y = sensorEvent.values[1];
            double z = sensorEvent.values[2];

            Log.d("TAG", String.valueOf(x) + " " + String.valueOf(y) + " " + String.valueOf(z));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        return;
    }
}
