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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RPEFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RPEFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RPEFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_FIRST_NAME = "firstname";
    private static final String ARG_LAST_NAME = "lastname";
    private static final String ARG_TEAM = "teamname";


    private String mFirstName;
    private String mLastName;
    private Team mTeam;
    private Button submitButton;

    private OnFragmentInteractionListener mListener;

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
        submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //do nothing
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        Log.d("TAG", "submit");
/*
        new NetworkManager().request(new RequestInterface() {
            @Override
            public void onRequestSuccess(String response) {
                Log.d("TAG", "response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<String> athletesArray = new ArrayList<String>();
                    if (jsonArray != null) {
                        for (int i=0;i<jsonArray.length();i++){
                            athletesArray.add(jsonArray.getString(i));
                        }
                    }
                    Intent athletesIntent = new Intent(getActivity().getApplicationContext(), AthletesActivity.class);
                    athletesIntent.putExtra("team", team);
                    athletesIntent.putExtra("athletes", athletesArray);
                    startActivity(athletesIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRequestFailure(String error) {
                Log.e("ERROR: ", error);
            }
        }, new URLs().getAthletesUrl() + "?team=" + team.mName, Request.Method.POST, params, headers, getActivity().getApplicationContext());

    }*/
    }
}
