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

        mManager.getNetworkManager().login(email, password, new LoginInterface() {
            @Override
            public Boolean onLoginSuccess(User user) {
                if(user!=null) {
                    Log.d("TAG", "setting active user");
                    mManager.setActiveUser(user);
                    mManager.setActiveUser(user);
                    mManager.saveActiveUser(context);
                    Intent teamsIntent = new Intent(context, TeamsActivity.class);
                    teamsIntent.putExtra("user", (Serializable) user);
                    startActivity(teamsIntent);
                    return true;
                } else {
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    startActivity(loginIntent);

                }
                return false;
            }

            @Override
            public Boolean onLoginFailure(String error) {
                Intent loginIntent = new Intent(context, LoginActivity.class);
                startActivity(loginIntent);
                return false;
            }
        }, getApplicationContext());
    }
}
