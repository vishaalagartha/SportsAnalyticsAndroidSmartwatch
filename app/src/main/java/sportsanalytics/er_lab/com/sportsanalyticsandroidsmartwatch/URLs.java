package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import org.json.JSONObject;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import android.util.Log;

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


    public String getWellnessURL() {
        return mScheme + "://" +  mHost + ":" + mPort + "/mobile/survey";

    }

    public String getSorenessURL() {
        return mScheme + "://" +  mHost + ":" + mPort + "/mobile/soreness";

    }

    public String getRPEUrl() {
        return mScheme + "://" +  mHost + ":" + mPort + "/mobile/rpe";

    }

    public String getJumpURL() {
        return mScheme + "://" +  mHost + ":" + mPort + "/mobile/jump";

    }

    static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    static String urlEncodeUTF8(Map<?,?> map) {
        Log.d("TAG", map.toString());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            if (entry.getValue() instanceof Map){
                Log.d("TAG", "HERE" + ((Map) entry.getValue()).toString());
                String key = (String) entry.getKey();
                Map<String, Object> nested = (Map<String, Object>) entry.getValue();
            }
            else {
                sb.append(String.format("%s=%s",
                        urlEncodeUTF8(entry.getKey().toString()),
                        urlEncodeUTF8(entry.getValue().toString())
                ));
            }
        }
        return sb.toString();
    }
}
