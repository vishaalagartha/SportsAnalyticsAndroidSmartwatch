package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class SorenessFragment extends Fragment {
    private static final String ARG_FIRST_NAME = "firstname";
    private static final String ARG_LAST_NAME = "lastname";
    private static final String ARG_TEAM = "teamname";

    private String mFirstName;
    private String mLastName;
    private Team mTeam;

    ArrayList<String> musclesStrings = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private ListView musclesListView;
    private Button sorenessSubmitButton;


    public SorenessFragment() {
        // Required empty public constructor
    }

    public static SorenessFragment newInstance(String firstname, String lastname, Team team) {
        SorenessFragment fragment = new SorenessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FIRST_NAME, firstname);
        args.putString(ARG_LAST_NAME, lastname);
        args.putSerializable(ARG_TEAM, team);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_soreness, container, false);

        musclesStrings.add("Biceps");
        musclesStrings.add("Deltoids");
        musclesStrings.add("Gastrocnemuis");
        musclesStrings.add("Gluteals");
        musclesStrings.add("Hamstrings");
        musclesStrings.add("Latissiumus Dorsi");
        musclesStrings.add("Obliques");
        musclesStrings.add("Pectorals");
        musclesStrings.add("Quadriceps");
        musclesStrings.add("Rectus Abdominus");
        musclesStrings.add("Trapezius");
        musclesStrings.add("Triceps");
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, musclesStrings);

        musclesListView = (ListView) view.findViewById( R.id.musclesListView );
        musclesListView.setAdapter( listAdapter );

        musclesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int color = Color.TRANSPARENT;
                int ORANGE = Color.rgb(255,165,0);
                int YELLOW = Color.rgb(255,215,0);
                Drawable background = view.getBackground();
                if (background instanceof ColorDrawable)
                    color = ((ColorDrawable) background).getColor();
                if(color==Color.TRANSPARENT)
                    view.setBackgroundColor(YELLOW);
                else if (color==YELLOW)
                    view.setBackgroundColor(ORANGE);
                else if(color==ORANGE)
                    view.setBackgroundColor(Color.RED);
                else
                    view.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        sorenessSubmitButton = (Button) view.findViewById(R.id.submitSorenessButton);
        sorenessSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("TAG", "submit soreness");
                HashMap<String, Object> params = new HashMap<>();

                HashMap<String, Object> data = new HashMap<>();
                data.put("session", "practice");
                data.put("minutes", 4);
                data.put("RPE", 5);

                params.put("data", data);
                params.put("team", mTeam.getmName());
                params.put("firstname", mFirstName);
                params.put("lastname", mLastName);
                params.put("timestamp", 0);

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
                }, new URLs().getSorenessURL(), params, headers, getActivity().getApplicationContext());

            }
        });

        return view;
    }
}
