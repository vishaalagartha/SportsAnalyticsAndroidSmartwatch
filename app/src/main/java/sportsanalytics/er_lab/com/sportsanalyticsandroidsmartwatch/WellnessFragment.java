package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.android.volley.Request;

import java.util.HashMap;


public class WellnessFragment extends Fragment {
    private static final String ARG_FIRST_NAME = "firstname";
    private static final String ARG_LAST_NAME = "lastname";
    private static final String ARG_TEAM = "teamname";


    private String mFirstName;
    private String mLastName;
    private Team mTeam;

    private Button wellnessSubmitButton;
    private SeekBar moodBar, fatigueBar, sorenessBar, stressBar, sleepBar;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wellness, container, false);
        moodBar = (SeekBar) view.findViewById(R.id.moodStatus);
        fatigueBar = (SeekBar) view.findViewById(R.id.fatigueStatus);
        sorenessBar = (SeekBar) view.findViewById(R.id.sorenessStatus);
        stressBar = (SeekBar) view.findViewById(R.id.stressStatus);
        sleepBar = (SeekBar) view.findViewById(R.id.sleepStatus);

        wellnessSubmitButton = (Button) view.findViewById(R.id.submitWellnessButton);
        wellnessSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("TAG", "submit wellness");
                HashMap<String, Object> params = new HashMap<>();

                HashMap<String, Object> data = new HashMap<>();
                data.put("mood", moodBar.getProgress());
                data.put("fatigue", fatigueBar.getProgress());
                data.put("soreness", sorenessBar.getProgress());
                data.put("stress", stressBar.getProgress());
                data.put("sleep", sleepBar.getProgress());

                params.put("data", data);
                params.put("team", mTeam.getmName());
                params.put("firstname", mFirstName);
                params.put("lastname", mLastName);
                params.put("timestamp", System.currentTimeMillis()/1000);

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
                }, new URLs().getWellnessURL(), params, headers, getActivity().getApplicationContext());

            }
        });
        return view;
    }

}
