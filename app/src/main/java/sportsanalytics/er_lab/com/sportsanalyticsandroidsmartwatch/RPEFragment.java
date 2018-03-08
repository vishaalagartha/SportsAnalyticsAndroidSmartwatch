package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


public class RPEFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_FIRST_NAME = "firstname";
    private static final String ARG_LAST_NAME = "lastname";
    private static final String ARG_TEAM = "teamname";


    private String mFirstName;
    private String mLastName;
    private Team mTeam;
    private Button submitRPEButton;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_rpe, container, false);
        submitRPEButton = (Button) view.findViewById(R.id.submitRPEButton);
        submitRPEButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        Log.d("TAG", "submit rpe");
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> data = new HashMap<>();
        data.put("session", "practice");
        data.put("minutes", 4);
        data.put("RPE", 5);

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
        }, new URLs().getRPEUrl(), params, headers, getActivity().getApplicationContext());

    }

}
