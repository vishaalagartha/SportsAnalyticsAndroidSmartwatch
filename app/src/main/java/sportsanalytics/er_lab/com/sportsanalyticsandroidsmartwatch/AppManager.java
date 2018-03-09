package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by vishaalagartha on 2/19/18.
 */

public class AppManager extends Application {

    private User mUser;
    private NetworkManager mNetworkManager = new NetworkManager();
    private static final String USER_DEFAULTS_KEY = "SportsAnalyticsAndroidSmartwatch";

    public NetworkManager getNetworkManager() { return mNetworkManager; }

    public User getActiveUser() {
        return mUser;
    }

    public void setActiveUser(User user) {
        mUser = user;
    }

    public void saveActiveUser(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DEFAULTS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", mUser.getUsername());
        editor.putString("password", mUser.getPassword());
        editor.commit();
    }

    public String loadUserEmail(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DEFAULTS_KEY, Context.MODE_PRIVATE);
        String email = preferences.getString("email", null);
        return email;
    }

    public String loadUserPassword(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DEFAULTS_KEY, Context.MODE_PRIVATE);
        String password = preferences.getString("password", null);
        return password;
    }
}
