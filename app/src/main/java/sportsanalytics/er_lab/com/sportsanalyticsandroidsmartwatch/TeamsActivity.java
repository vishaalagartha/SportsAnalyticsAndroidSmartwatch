package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public class TeamsActivity extends Activity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "In teams activity");

        setContentView(R.layout.activity_teams);
        //mUser = (User) getIntent().getParcelableExtra("User");
        //Log.d("in teams activitity with user", mUser.mToken);
    }
}
