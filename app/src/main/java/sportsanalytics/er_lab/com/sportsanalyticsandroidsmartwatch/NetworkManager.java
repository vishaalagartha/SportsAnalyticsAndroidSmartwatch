package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    public static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final Context mApplicationContext;
        public String mUser = null;


        UserLoginTask(String email, String password, Context applicationContext) {
            mEmail = email;
            mPassword = password;
            mApplicationContext = applicationContext;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
                RequestQueue queue = Volley.newRequestQueue(mApplicationContext);
                String url = "http://er-lab.cs.ucla.edu:80/mobile/login";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("TAG", response);
                                mUser = response;
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("TAG", error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("password", mPassword);
                        params.put("email", mEmail);

                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(postRequest);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        public String getUser() {
            return mUser;
        }
    }
}
