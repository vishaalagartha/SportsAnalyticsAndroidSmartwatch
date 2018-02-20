package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.app.Application;

/**
 * Created by vishaalagartha on 2/19/18.
 */

public class AppManager extends Application {

    private User mUser;

    public User getActiveUser() {
        return mUser;
    }

    public void setActiveUser(User user) {
        mUser = user;
    }
}
