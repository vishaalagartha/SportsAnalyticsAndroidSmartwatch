package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.media.Image;
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
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.HashMap;


public class WellnessFragment extends Fragment {
    private static final String ARG_FIRST_NAME = "firstname";
    private static final String ARG_LAST_NAME = "lastname";
    private static final String ARG_TEAM = "teamname";


    private String mFirstName;
    private String mLastName;
    private Team mTeam;
    static AppManager appManager = null;

    private ImageButton wellnessSubmitButton;
    private Button moodButton, fatigueButton, sorenessButton, stressButton, sleepButton;


    public WellnessFragment() {
        // Required empty public constructor
    }

    public static WellnessFragment newInstance(String firstname, String lastname, Team team) {
        WellnessFragment fragment = new WellnessFragment();
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
        View view = inflater.inflate(R.layout.fragment_wellness, container, false);
        moodButton = (Button) view.findViewById(R.id.moodStatus);
        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentMood = Integer.parseInt(moodButton.getText().toString());
                if(currentMood==5)
                    moodButton.setText(0);
                else
                    moodButton.setText(currentMood+1);
            }
        });
        fatigueButton = (Button) view.findViewById(R.id.fatigueStatus);
        fatigueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentFatigue = Integer.parseInt(fatigueButton.getText().toString());
                if(currentFatigue==5)
                    fatigueButton.setText("0");
                else
                    fatigueButton.setText(Integer.toString(currentFatigue+1));
            }
        });
        sorenessButton = (Button) view.findViewById(R.id.sorenessStatus);
        sorenessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentSoreness = Integer.parseInt(sorenessButton.getText().toString());
                if(currentSoreness==5)
                    sorenessButton.setText("0");
                else
                    sorenessButton.setText(Integer.toString(currentSoreness+1));
            }
        });
        stressButton = (Button) view.findViewById(R.id.stressStatus);
        stressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentStress = Integer.parseInt(stressButton.getText().toString());
                if(currentStress==5)
                    stressButton.setText("0");
                else
                    stressButton.setText(Integer.toString(currentStress+1));
            }
        });
        sleepButton = (Button) view.findViewById(R.id.sleepStatus);
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentSleep = Integer.parseInt(sleepButton.getText().toString());
                if(currentSleep==5)
                    sleepButton.setText("0");
                else
                    sleepButton.setText(Integer.toString(currentSleep+1));
            }
        });

        wellnessSubmitButton = (ImageButton) view.findViewById(R.id.submitWellnessButton);
        wellnessSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("TAG", "submit wellness");
                HashMap<String, Object> params = new HashMap<>();

                HashMap<String, Object> data = new HashMap<>();
                data.put("mood", moodButton.getText());
                data.put("fatigue", fatigueButton.getText());
                data.put("soreness", sorenessButton.getText());
                data.put("stress", stressButton.getText());
                data.put("sleep", sleepButton.getText());

                params.put("data", data);
                params.put("team", mTeam.getmName());
                params.put("firstname", mFirstName);
                params.put("lastname", mLastName);
                params.put("timestamp", System.currentTimeMillis()/1000);

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
                }, new URLs().getWellnessURL(), params, headers, getActivity().getApplicationContext());

            }
        });
        return view;
    }

}
