package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vishaalagartha on 2/16/18.
 */

enum Role {
    COACH,
    ATHLETE
}

final class Team implements  Serializable {
    String mName;
    String mSport;

    Team(String name, String sport) {
        mName = name;
        mSport = sport;
    }

    public String getmName() {
        return mName;
    }

    public String getmSport() {
        return mSport;
    }
}

public class User implements Serializable {
    static Role mRole;
    static String mUsername;
    static String mPassword;
    static String mFirstName;
    static String mLastName;
    static String mToken;
    static String mCookie;
    ArrayList<Team> mTeams;

    User(Role role, String username, String password, String firstName, String lastName, String token, String cookie, ArrayList<Team> teams){
        mRole = role;
        mUsername = username;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;
        mToken = token;
        mCookie = cookie;
        mTeams = teams;
    }

    User(Role role, String firstName, String lastName) {
        mRole = role;
        mFirstName = firstName;
        mLastName = lastName;
    }

    static String getToken() {
        return mToken;
    }

    static String getCookie() {
        return mCookie;
    }
}
