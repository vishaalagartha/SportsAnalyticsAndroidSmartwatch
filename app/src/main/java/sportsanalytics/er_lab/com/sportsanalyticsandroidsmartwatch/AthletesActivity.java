package sportsanalytics.er_lab.com.sportsanalyticsandroidsmartwatch;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AthletesActivity extends Activity {

    Team mTeam;
    ArrayList<String> athletesArray;
    ArrayList<String> athletesStrings = new ArrayList<>();
    private ArrayAdapter<String> listAdapter ;
    private ListView mainListView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athletes);


        mTeam = (Team) getIntent().getSerializableExtra("team");
        athletesArray = getIntent().getStringArrayListExtra("athletes");

        JSONObject jObject = null;
        String keyString=null;
        for (int i = 0; i < athletesArray.size(); i++) {
            try {
                jObject = new JSONObject(athletesArray.get(i));
                athletesStrings.add(jObject.getString("firstName") + " " + jObject.getString("lastName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mainListView = (ListView) findViewById( R.id.mainListView );
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, athletesStrings);
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s = mainListView.getItemAtPosition(i).toString();
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(athletesArray.get(i));
                    Log.d("TAG", jObject.toString());
                    User athlete = new User(Role.ATHLETE, jObject.getString("firstName"), jObject.getString("lastName"));
                    Intent athleteTabIntent = new Intent(getApplicationContext(), AthleteTabActivity.class);
                    athleteTabIntent.putExtra("athlete", (Serializable) athlete);
                    athleteTabIntent.putExtra("team", (Serializable) mTeam);
                    startActivity(athleteTabIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
