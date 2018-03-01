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
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SorenessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SorenessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    private OnFragmentInteractionListener mListener;

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
        return view;
    }

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
}
