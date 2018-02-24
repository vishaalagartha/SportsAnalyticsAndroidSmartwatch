package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


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

    public String getAthletesUrl(String teamName) {
        try {
            return mScheme + "://" +  mHost + ":" + mPort + "/data/getPlayers?team=" + URLEncoder.encode(teamName, StandardCharsets.UTF_8.toString().replace("+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRPEUrl(Map<?, ?> params) {
        return mScheme + "://" +  mHost + ":" + mPort + "/mobile/rpe?" + urlEncodeUTF8(params);

    }

    static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }
}
