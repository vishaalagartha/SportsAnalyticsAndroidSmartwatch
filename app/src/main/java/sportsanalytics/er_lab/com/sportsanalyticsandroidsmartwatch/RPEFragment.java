package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


public class RPEFragment extends Fragment {
    private static final String ARG_FIRST_NAME = "firstname";
    private static final String ARG_LAST_NAME = "lastname";
    private static final String ARG_TEAM = "teamname";


    private String mFirstName;
    private String mLastName;
    private Team mTeam;
    static AppManager appManager = null;

    private EditText duration;
    private Button rpeScale;
    private CheckBox isPractice;
    private CheckBox isTraining;
    private CheckBox isCompetition;

    private ImageButton submitRPEButton;


    public RPEFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment RPEFragment.
     */
    public static RPEFragment newInstance(String firstname, String lastname, Team team) {
        RPEFragment fragment = new RPEFragment();
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
        View view =  inflater.inflate(R.layout.fragment_rpe, container, false);

        duration = (EditText) view.findViewById(R.id.duration);
        rpeScale = (Button) view.findViewById(R.id.RPEButton);
        rpeScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentRPE = Integer.parseInt(rpeScale.getText().toString());
                if(currentRPE==5)
                    rpeScale.setText("0");
                else
                    rpeScale.setText(Integer.toString(currentRPE+1));
            }
        });
        isPractice = (CheckBox) view.findViewById(R.id.practice);
        isTraining = (CheckBox) view.findViewById(R.id.training);
        isCompetition = (CheckBox) view.findViewById(R.id.competition);

        submitRPEButton = (ImageButton) view.findViewById(R.id.submitRPEButton);
        submitRPEButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do what you want to do when button is clicked
                Log.d("TAG", "submit rpe");
                HashMap<String, Object> params = new HashMap<>();

                HashMap<String, Object> data = new HashMap<>();
                data.put("RPE", rpeScale.getText());
                data.put("minutes", duration.getText());
                if(isPractice.isActivated()) data.put("session", "practice");
                else if(isTraining.isActivated()) data.put("session", "training");
                else if(isCompetition.isActivated()) data.put("session", "competition");

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
                }, new URLs().getRPEUrl(), params, headers, getActivity().getApplicationContext());

            }
        });

        return view;
    }

}
