package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.content.Intent;

public class MainActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppManager mManager = new AppManager();

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        // Enables Always-on
        setAmbientEnabled();
    }
}
