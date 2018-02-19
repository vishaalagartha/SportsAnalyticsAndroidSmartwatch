package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

/**
 * Created by vishaalagartha on 2/19/18.
 */

public class URLs {
    private String mPort = "80";
    private String mHost = "er-lab.cs.ucla.edu";
    private String mScheme = "http";

    public String getLoginUrl() {
        return mScheme + "://" +  mHost + ":" + mPort + "/mobile/login";

    }

    public String getTeamsListUrl() {
        return mScheme + "://" +  mHost + ":" + mPort + "/mobile/teamlist";

    }

    public String getTeamImageUrl(String teamType) {
        return mScheme + "://" +  mHost + ":" + mPort + "/images/" + teamType + ".jpg";
    }
}
