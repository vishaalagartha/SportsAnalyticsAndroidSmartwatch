package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
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

    private CookieManager manager = new CookieManager();

    NetworkManager() {
        CookieHandler.setDefault(manager);
    }


    public void login(final String email, final String password, final LoginInterface callback, final Context mApplicationContext) {
        final RequestQueue loginQueue = Volley.newRequestQueue(mApplicationContext);
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest loginRequest = new JsonObjectRequest(new URLs().getLoginUrl(), params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

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
                                                                firstName, lastName, token, teams);
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
                                                    callback.onLoginFailure(e.getMessage());
                                                }
                                            }
                                    ) {

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String> headers = new HashMap<String, String>();
                                            headers.put("Authorization", token);
                                            //headers.put("Cookie", cookie);
                                            return headers;
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
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        loginQueue.add(loginRequest);
    }


    public void request(final RequestInterface requestInterface, final String url, final int method, final Map<String, String> params,
                        final Map<String, String> headers, final Context mApplicationContext) {
        final RequestQueue queue = Volley.newRequestQueue(mApplicationContext);
        final String token = User.getToken();
        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        Log.d("TAG", url + " " + params + " " + headers);
                        requestInterface.onRequestSuccess(r);
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
                headers.put("Authorization", token);
                return headers;
            }
        };
        queue.add(request);
    }

    public void jsonObjectRequest(final RequestInterface requestInterface, final String url, final HashMap<String, Object> params,
                        final Map<String, String> headers, final Context mApplicationContext) {
        final RequestQueue queue = Volley.newRequestQueue(mApplicationContext);
        final String token = User.getToken();
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject r) {
                        requestInterface.onRequestSuccess(r.toString());
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Authorization", token);
                return headers;
            }
        };
        queue.add(request);
    }
}