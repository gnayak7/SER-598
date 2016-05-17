package edu.asu.msse.gnayak2.movieapplication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Copyright 2016 Gowtham Ganesh Nayak,
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Purpose: This class holds all the information related to a movie.
 * <p>
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */

public class Movie implements Serializable {
    private String id;
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runTime;
    private String genre;
    private String actors;
    private String plot;
    private String poster;

    /**
     * Constructor
     *
     * @params title String
     * @params year String
     * @params rated String
     * @params released String
     * @params runtime String
     * @params genre String
     * @params actors String
     * @params plot String
     * @params poster String
     * @returns Object
     */
    public Movie(String title, String year, String rated, String released, String runtime, String genre, String actors, String plot, String poster) {
        this.id = UniqueIDGenerator.getUniqueId();
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runTime = runtime;
        this.genre = genre;
        this.actors = actors;
        this.plot = plot;
        this.poster = poster;
    }

    /**
     * Constructor
     *
     * @params id String
     * @params title String
     * @params year String
     * @params rated String
     * @params released String
     * @params runtime String
     * @params genre String
     * @params actors String
     * @params plot String
     * @params poster String
     * @returns Object
     */
    public Movie(String id, String title, String year, String rated, String released, String runtime, String genre, String actors, String plot, String poster) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runTime = runtime;
        this.genre = genre;
        this.actors = actors;
        this.plot = plot;
        this.poster = poster;
    }

    /**
     * Constructor
     *
     * @param jsonString JSON string.
     * @returns Ojbect
     */

    public Movie(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            this.title = jsonObj.getString("Title");
            this.year = jsonObj.getString("Year");
            this.rated = jsonObj.getString("Rated");
            this.released = jsonObj.getString("Released");
            this.runTime = jsonObj.getString("Runtime");
            this.actors = jsonObj.getString("Actors");
            this.plot = jsonObj.getString("Plot");
            this.genre = jsonObj.getString("Genre");
            this.poster = jsonObj.getString("Poster");
            this.id = UniqueIDGenerator.getUniqueId();
            Log.w(this.getClass().getSimpleName(), "Successfully extracrted all the information from JSON String");
        } catch (JSONException e) {
            Log.w("Json String: ", jsonString);
            Log.w(this.getClass().getSimpleName(), "Error extracting the information from JSON String");
        }
    }

    public String getTitle() {
        return this.title;
    }

    public String getYear() {
        return this.year;
    }

    public String getRated() {
        return this.rated;
    }

    public String getReleased() {
        return this.released;
    }

    public String getRunTime() {
        return this.runTime;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getActors() {
        return this.actors;
    }

    public String getPlot() {
        return this.plot;
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPoster() {
        return this.poster;
    }

    /**
     * Converts a movie object to JSON String
     *
     * @return String
     */

    public String toJsonString() {
        String ret = "";
        try {
            JSONObject jo = new JSONObject();
            jo.put("Title", this.title);
            jo.put("Year", this.year);
            jo.put("Rated", this.rated);
            jo.put("Released", this.released);
            jo.put("Runtime", this.runTime);
            jo.put("Actors", this.actors);
            jo.put("Genre", this.genre);
            jo.put("Plot", this.plot);
            ret = jo.toString();
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return ret;
    }
}
