package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vishaalagartha on 2/15/18.
 */

public class NetworkManager {
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    String url = "http://er-lab.cs.ucla.edu:80/mobile/login";

    public void login(final String email, final String password, final LoginCallback callback, Context mApplicationContext) {
        RequestQueue queue = Volley.newRequestQueue(mApplicationContext);
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        Log.d("TAG", r);
                        try {
                            JSONObject response = new JSONObject(r);
                            Log.d("TAG", response.toString());
                            callback.onLoginSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onLoginFailure(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        callback.onLoginFailure(e.getMessage());
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", password);
                params.put("email", email);

                return params;
            }
        };
        queue.add(strReq);
    }
}