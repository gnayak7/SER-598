package edu.asu.msse.gnayak2.openmoviedatabase;

/**
 * Copyright 2016 Gowtham Ganesh Nayak,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * Purpose:This application demonstrates how to make a http request.Get
 * the response back and parse it to extract fields and store it in a container
 * and display it on the screen.
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version January 15 2016
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/*
This class is the main activity class and this is the starting point
of the application. This class will make a json api call and gets the
response back. It builds a class to extract all the fields and
display it on the screen.
 */

public class MainActivity extends AppCompatActivity {

    final Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button myButton = (Button) findViewById(R.id.mainPage_myButton_id);

        myButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://www.omdbapi.com/?t=Frozen&y=&plot=short&r=json";

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                MovieDescription myMovieClass = new MovieDescription(response.toString());
                                TextView movieTitle = (TextView) findViewById(R.id.main_activity_id_title);
                                TextView movieYear = (TextView) findViewById(R.id.main_activity_id_year);
                                TextView movieRated = (TextView) findViewById(R.id.main_activity_id_rated);
                                TextView movieReleased = (TextView) findViewById(R.id.main_activity_id_released);
                                TextView movieRunTime = (TextView) findViewById(R.id.main_activity_id_runtime);
                                TextView movieGenre = (TextView) findViewById(R.id.main_activity_id_genre);
                                TextView movieActors = (TextView) findViewById(R.id.main_activity_id_actors);
                                TextView moviePlot = (TextView) findViewById(R.id.main_activity_id_plot);

                                movieTitle.setText("Title: " + myMovieClass.getTitle());
                                movieYear.setText("Year:" + myMovieClass.getYear());
                                movieRated.setText("Rated:" + myMovieClass.getRated());
                                movieReleased.setText("Released:" + myMovieClass.getReleased());
                                movieRunTime.setText("Runtime:" + myMovieClass.getRunTime());
                                movieGenre.setText("Genre:" + myMovieClass.getGenre());
                                movieActors.setText("Actors:" + myMovieClass.getActors());
                                moviePlot.setText("Plot:" + myMovieClass.getPlot());
                                android.util.Log.i("Json to string: ", myMovieClass.toJsonString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                android.util.Log.w(this.getClass().getSimpleName(),
                                        "error");
                            }
                        });
                        Volley.newRequestQueue(mContext).add(jsonObjReq);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
References:
1) http://developer.android.com/training/volley/request.html
2) http://pooh.poly.asu.edu/Mobile/Assigns/LabsSpr2016/Assign1/assign1.html
 */
}
