package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface RequestInterface {
    void onRequestSuccess(String response);
    void onRequestFailure(String error);
}

public class TeamsActivity extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static User mUser;
    static AppManager appManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        mUser = (User) getIntent().getSerializableExtra("user");
        appManager = (AppManager) getApplication();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), mUser);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teams, menu);
        return true;
    }

    public static class TeamFragment extends Fragment implements View.OnClickListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public TeamFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TeamFragment newInstance(int sectionNumber, Team team) {
            TeamFragment fragment = new TeamFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable("team", team);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_teams, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            Team team = (Team) getArguments().getSerializable("team");
            textView.setText(team.mName + " " + team.mSport);

            ImageView imageView = (ImageView) rootView.findViewById(R.id.section_image);
            String imageUri = new URLs().getTeamImageUrl(team.mSport);
            Picasso.with(getContext()).load(imageUri).into(imageView);
            imageView.setOnClickListener(this);
            return rootView;
        }
        @Override
        public void onClick(View v) {
            final Team team = (Team) getArguments().getSerializable("team");
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String> headers = new HashMap<String, String>();

            appManager.getNetworkManager().request(new RequestInterface() {
                @Override
                public void onRequestSuccess(String response) {
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
            }, new URLs().getAthletesUrl(team.mName), Request.Method.GET, params, headers, getActivity().getApplicationContext());

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private User mUser;

        public SectionsPagerAdapter(FragmentManager fm, User user) {
            super(fm);
            mUser = user;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return TeamFragment.newInstance(position + 1, mUser.mTeams.get(position));
        }

        @Override
        public int getCount() {
            return mUser.mTeams.size();
        }
    }

}
