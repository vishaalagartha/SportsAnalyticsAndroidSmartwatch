package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;

public class MainActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AppManager mManager = new AppManager();
        String email = mManager.loadUserEmail(this.getApplicationContext());
        String password = mManager.loadUserPassword(this.getApplicationContext());
        final Context context = getApplicationContext();

        if (email!=null && password!=null) {
            mManager.getNetworkManager().login(email, password, new LoginInterface() {
                @Override
                public void onLoginSuccess(User user) {
                    if (user != null) {
                        Log.d("TAG", "Login successfull");

                        mManager.setActiveUser(user);
                        mManager.saveActiveUser(context);
                        Intent teamsIntent = new Intent(context, TeamsActivity.class);
                        teamsIntent.putExtra("user", (Serializable) user);
                        startActivity(teamsIntent);
                    } else {
                        Log.d("TAG", "Login failure");

                        Intent loginIntent = new Intent(context, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                }

                @Override
                public void onLoginFailure(String error) {
                    Log.d("TAG", "Login failure");

                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }, getApplicationContext());
        } else {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            startActivity(loginIntent);
        }
    }
}
