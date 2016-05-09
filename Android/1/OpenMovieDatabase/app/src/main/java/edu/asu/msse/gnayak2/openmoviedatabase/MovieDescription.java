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

import org.json.JSONException;
import org.json.JSONObject;

/*
This class extracts all the fields from the json string and
 * store in member variables. It is then used to populate the infromation
 * on the screen. Additonally it converts a json string.
 */
public class MovieDescription {
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runTime;
    private String genre;
    private String actors;
    private String plot;

    public MovieDescription(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            this.title = jsonObj.getString("Title");
            this.year = jsonObj.getString("Year");
            this.rated = jsonObj.getString("Rated");
            this.released = jsonObj.getString("Released");
            this.runTime = jsonObj.getString("Runtime");;
            this.actors = jsonObj.getString("Actors");
            this.plot = jsonObj.getString("Plot");
            this.genre = jsonObj.getString("Genre");
        } catch (JSONException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error extracting the iformation");
        }
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getRunTime() {
        return runTime;
    }

    public String getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String toJsonString() {
        String ret = "";
        try{
            JSONObject jo = new JSONObject();
            jo.put("Title",this.title);
            jo.put("Year",this.year);
            jo.put("Rated",this.rated);
            jo.put("Released",this.released);
            jo.put("Runtime",this.runTime);
            jo.put("Actors",this.actors);
            jo.put("Genre",this.genre);
            jo.put("Plot",this.plot);
            ret = jo.toString();
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return ret;
    }
}
