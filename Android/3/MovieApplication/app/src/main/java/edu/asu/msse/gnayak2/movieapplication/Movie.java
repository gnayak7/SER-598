package edu.asu.msse.gnayak2.movieapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Copyright 2016 Gowtham Ganesh Nayak,
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose:The purpose of this application is to demonstrate expandable list view adapter. Data is stored ina
 * a json file in raw folder. Additionally the applicaiton demonstrates use of picker.
 * <p/>
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version February 15 2016
 */
/*
This class extracts all the fields from the json string and
 * store in member variables. It is then used to populate the infromation
 * on the screen. Additonally it converts a json string.
 */
public class Movie implements Serializable {
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runTime;
    private String genre;
    private String actors;
    private String plot;

    public Movie (String jsonString) {
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
                    "error extracting the information");
        }
    }

    public Movie(String title, String year, String rated, String released, String runtime, String genre, String actors, String plot) {
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runTime = runtime;
        this.genre = genre;
        this.actors = actors;
        this.plot = plot;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setPlot(String plot) {
        this.plot = plot;
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
