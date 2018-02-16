package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import java.util.ArrayList;

/**
 * Created by vishaalagartha on 2/16/18.
 */

enum Role {
    COACH,
    ATHLETE
}

final class Team {
    String name;
    String sport;
}

public class User {
    Role mRole;
    String mUsername;
    String mPassword;
    String mFirstName;
    String mLastName;
    String mToken;
    ArrayList<Team> mTeams;

    User(Role role, String username, String password, String firstName, String lastName, String token, ArrayList<Team> teams){

    }
}
