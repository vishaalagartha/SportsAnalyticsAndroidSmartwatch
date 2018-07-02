package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.min;


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
    static AppManager appManager = null;


    private SensorManager mSensorManager;
    private Sensor mSensor;
    private List accelerationsList = new ArrayList<Double>();
    private List timestampsList = new ArrayList<Double>();

    private static final float GRAVITY_THRESHOLD = 7.0f;
    private static final long TIME_THRESHOLD_NS = 2000000000;
    private long mLastTime = 0;
    private double height = 0.0;

    private Button jumpButton;
    private TextView jumpTextView;
    private ProgressBar jumpProgress;

    private ImageButton jumpSubmitButton;
    private Boolean detect = false;


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
        appManager = (AppManager) getActivity().getApplication();
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
                if (!detect) {
                    detect = true;
                    Log.d("TAG", "Start Detecting");
                    jumpProgress.setVisibility(View.VISIBLE);
                    jumpButton.setText("STOP JUMP");
                } else {
                    Log.d("TAG", "End Detecting");
                    double accelerations[] = new double[accelerationsList.size()];
                    for(int i=0; i<accelerationsList.size(); i++) {
                        accelerations[i] = (double) accelerationsList.get(i);
                    }
                    List<Integer> max_indices = Peaks.findPeaks(accelerations, 50, 20.0);
                    int i1 = 0;
                    int i2 = 0;
                    double peak1 = 0;
                    double peak2 = 0;
                    // find two maxes in accelerations
                    for(int i=0; i<max_indices.size(); i++) {
                        double val = accelerations[max_indices.get(i)];
                        if (val > peak1)
                        {
                            peak2 = peak1;
                            i2 = i1;
                            peak1 = val;
                            i1 = max_indices.get(i);
                        }

                        else if (val > peak2
                                && val != peak1) {
                            peak2 = val;
                            i2 = max_indices.get(i);
                        }
                    }
                    Log.d("TAG", Integer.toString(i1) + " " + Integer.toString(i2));
                    if(i1>i2) {
                        int temp = i2;
                        i2 = i1;
                        i1 = temp;
                    }
                    Boolean prev_positive = true;
                    long t1 = 0;
                    long t2 = 0;
                    int min_width = 30;
                    // find where we cross x-axis
                    for(int i=0; i<accelerations.length; i++) {
                        if(i>i1 && i<i2) {
                            double val = accelerations[i];
                            // found first timestamp
                            if(prev_positive && val<9.807 && t1==0) {
                                t1 = (long) timestampsList.get(i);
                            }
                            // decrement min width
                            else if(t1!=0 && t2==0 && min_width>0){
                                min_width--;
                            }
                            // found second timestamp
                            else if(t1!=0 && t2==0 && val>9.807 && min_width==0){
                                t2 = (long) timestampsList.get(i);
                            }
                        }
                    }

                    Log.d("TAG", "Peak 1: " + peak1 + " " + i1 + " " + t1);
                    Log.d("TAG", "Peak 2: " + peak2 + " " + i2 + " " + t2);
                    Double fallTime = (t2 - t1)/(2.0*1E9);
                    height = 100.0/(2.0*2.54) * 9.807 * fallTime * fallTime;
                    Log.d("TAG", Double.toString(height) + " " + Double.toString(fallTime));
                    jumpTextView.setText(Double.toString(height).substring(0, min(4, Double.toString(height).length())) + " in");
                    reset();
                }

            }
        });


        jumpSubmitButton = (ImageButton) view.findViewById(R.id.submitJumpButton);
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
                params.put("timestamp", System.currentTimeMillis() / 1000);

                HashMap<String, String> emptyParams = new HashMap<>();

                HashMap<String, String> headers = new HashMap<>();

                appManager.getNetworkManager().jsonObjectRequest(new RequestInterface() {
                    @Override
                    public void onRequestSuccess(String response) {
                        Toast.makeText(getActivity().getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRequestFailure(String error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Failed to submit", Toast.LENGTH_SHORT).show();
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
        double accelX = sensorEvent.values[0];
        double accelY = sensorEvent.values[1];
        double accelZ = sensorEvent.values[2];
        Double v = norm(accelX, accelY, accelZ);
        if (detect) {
            accelerationsList.add(v);
            timestampsList.add(sensorEvent.timestamp);
        }
    }


    public void reset() {
        jumpButton.setText("JUMP");
        jumpProgress.setVisibility(View.GONE);
        accelerationsList.clear();
        timestampsList.clear();
        detect = false;
    }




    public Double norm(Double x, Double y, Double z) {
        return Math.sqrt(x*x + y*y + z*z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        return;
    }


}
