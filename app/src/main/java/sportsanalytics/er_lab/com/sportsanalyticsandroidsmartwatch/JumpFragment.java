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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;


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


    private static final float GRAVITY_THRESHOLD = 7.0f;
    private static final long TIME_THRESHOLD_NS = 2000000000;
    private long mLastTime = 0;
    private boolean mUp = false;
    private double height = 0.0;

    private Button jumpButton;
    private Boolean isJumping = false;
    private TextView jumpTextView;
    private ProgressBar jumpProgress;

    private Button jumpSubmitButton;

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

        View view = inflater.inflate(R.layout.fragment_jump, container, false);
        jumpButton = (Button) view.findViewById(R.id.jumpButton);
        jumpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!isJumping) {
                    onResume();
                    jumpProgress.setVisibility(View.VISIBLE);
                    jumpButton.setText("JUMPING");
                }
                else {
                    onPause();
                    jumpProgress.setVisibility(View.GONE);

                    jumpButton.setText("JUMP");
                }

            }
        });

        jumpSubmitButton = (Button) view.findViewById(R.id.submitJumpButton);
        jumpSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "submit jump");
                HashMap<String, Object> params = new HashMap<>();

                HashMap<String, Object> data = new HashMap<>();
                data.put("height", height);

                params.put("data", data);
                params.put("team", mTeam.getmName());
                params.put("firstname", mFirstName);
                params.put("lastname", mLastName);
                params.put("timestamp", System.currentTimeMillis()/1000);

                HashMap<String, String> emptyParams = new HashMap<>();

                HashMap<String, String> headers = new HashMap<>();

                new NetworkManager().jsonObjectRequest(new RequestInterface() {
                    @Override
                    public void onRequestSuccess(String response) {
                        Log.d("TAG", "response: " + response);
                    }

                    @Override
                    public void onRequestFailure(String error) {
                        Log.e("ERROR: ", error);
                    }
                }, new URLs().getJumpURL(), params, headers, getActivity().getApplicationContext());
            }
        });

        jumpProgress = (ProgressBar) view.findViewById(R.id.jumpProgressBar);
        jumpProgress.setVisibility(View.GONE);
        jumpTextView = (TextView) view.findViewById(R.id.jumpHeightText);
        jumpTextView.setText("0");
        return view;
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
        detectJump(sensorEvent.values[0], sensorEvent.timestamp);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        return;
    }

    private void detectJump(float xValue, long timestamp) {
        if ((Math.abs(xValue) > GRAVITY_THRESHOLD)) {
            if(timestamp - mLastTime < TIME_THRESHOLD_NS && mUp != (xValue > 0)) {
                double jumpTime = (timestamp-mLastTime)/1.0e9;
                height = 100 * 1.0 / 8.0 * 9.807 * jumpTime * jumpTime / 2.54;
                jumpTextView.setText(Double.toString(height).substring(0, 5) + " in");
                jumpButton.setText("JUMP");
                jumpProgress.setVisibility(View.GONE);
                onJumpDetected(!mUp);
                onPause();
            }
            mUp = xValue > 0;
            mLastTime = timestamp;
        }
    }

    private void onJumpDetected(boolean up) {
        // we only count a pair of up and down as one successful movement
        if (up) {
            Log.d("TAG", "up");
            return;
        }
        Log.d("TAG", "down");

    }
}
