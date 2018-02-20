package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


    public void login(final String email, final String password, final LoginCallback callback, final Context mApplicationContext) {
        final RequestQueue loginQueue = Volley.newRequestQueue(mApplicationContext);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, new URLs().getLoginUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String r) {
                        try {
                            final String cookie = r.split("\n")[0].split(";")[0];
                            JSONObject response = new JSONObject(r.split("\n")[1]);


                            Boolean success = (Boolean) response.getBoolean("success");

                            if(success) {
                                final String firstName = response.has("firstName") ?
                                        response.getString("firstName") : null;
                                final String lastName = response.has("lastName") ?
                                        response.getString("lastName") : null;
                                final Role role = response.has("role") && response.getString("role").equals("coach") ?
                                        Role.COACH : Role.ATHLETE;

                                if(response.has("token")) {
                                    final String token = response.getString("token");
                                    RequestQueue teamQueue = Volley.newRequestQueue(mApplicationContext);
                                    StringRequest teamRequest = new StringRequest(Request.Method.POST, new URLs().getTeamsListUrl(),
                                            new Response.Listener<String>()
                                            {
                                                @Override
                                                public void onResponse(String r){
                                                    // response
                                                    try {
                                                        JSONObject response = new JSONObject(r);
                                                        Boolean success = (Boolean) response.getBoolean("success");
                                                        if(success) {
                                                            ArrayList<Team> teams = new ArrayList<>();
                                                            JSONArray teamsArray = response.getJSONArray("access");
                                                            for(int i=0; i<teamsArray.length(); i++) {
                                                                JSONObject t = teamsArray.getJSONObject(i);
                                                                teams.add(new Team(t.getString("name"), t.getString("sport")));
                                                            }
                                                            User user = new User(role, email, password,
                                                                firstName, lastName, token, cookie, teams);
                                                            callback.onLoginSuccess(user);

                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
                                            },
                                            new Response.ErrorListener()
                                            {
                                                @Override
                                                public void onErrorResponse(VolleyError e) {
                                                    Log.d("TAG", e.getMessage());
                                                    callback.onLoginFailure(e.getMessage());
                                                }
                                            }
                                    ) {

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("Cookie", cookie);
                                            return params;
                                        }

                                    };
                                    teamQueue.add(teamRequest);
                                }
                            }

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
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String data = response.headers.get("Set-Cookie") + "\n" + new String(response.data);
                com.android.volley.Response<String> result = com.android.volley.Response.success(data,
                        HttpHeaderParser.parseCacheHeaders(response));
                return result;
            }
        };
        loginQueue.add(loginRequest);
    }


    public void request(final String url, final int method, final Map<String, String> params,
                        final Map<String, String> headers, final Context mApplicationContext) {
        final RequestQueue queue = Volley.newRequestQueue(mApplicationContext);

        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String r) {
                        Log.d("TAG", url + method + params.toString() + headers.toString());
                        Log.d("TAG", "got response" + r);
                        try {

                            JSONObject response = new JSONObject(r);
                            Boolean success = (Boolean) response.getBoolean("success");
                            if (success) {
                                Log.d("TAG", r);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        queue.add(request);
    }
}