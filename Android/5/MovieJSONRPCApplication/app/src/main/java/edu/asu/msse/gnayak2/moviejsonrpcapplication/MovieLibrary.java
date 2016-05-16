package edu.asu.msse.gnayak2.moviejsonrpcapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
 *
 * Purpose: The purpose of this project is to demonstrate the consumption
 * of JSON RPC server service to populate and display movie information
 *
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */

// Library which holds all the movie information.
public class MovieLibrary {
    private LinkedHashMap<String, ArrayList<String>> model; // <engre, title of all the movies belonging to this genre>
    private LinkedHashMap<String, Movie> movies;

    MovieLibrary(JSONArray jsonArray) {
        int length = jsonArray.length();
        model = new LinkedHashMap<String, ArrayList<String>>();

        try {
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Movie mo = new Movie(jsonObject.toString());
                if (mo != null) {
                    this.movies.put(mo.getTitle(), mo);
                }
                if (this.model.containsKey(mo.getGenre())) {
                    (this.model.get(mo.getGenre())).add(mo.getTitle());
                } else {
                    ArrayList<String> s = new ArrayList<String>();
                    s.add(mo.getTitle());
                    this.model.put(mo.getGenre(), s);
                }
            }
        } catch (Exception e) {

        }
    }

    MovieLibrary(LinkedHashMap<String, ArrayList<String>> model) {
        this.model = model;
        this.movies = new LinkedHashMap<String, Movie>();
    }

    public int getGenreCount() {
        return model.size();
    }

    public int getMoviesCount(String genre) {
        return (model.get(genre)).size();
    }

    public String getGenreAt(int groupPosition) {
        String[] keys = model.keySet().toArray(new String[]{});
        return keys[groupPosition];
    }

    public String getMovieNameAt(int groupPosition, int position) {
        String genre = getGenreAt(groupPosition);
        ArrayList<String> moviesList = model.get(genre);
        return moviesList.get(position);
    }

    public Movie getMovie(String movieName) {
        if (movies.containsKey(movieName)) {
            return movies.get(movieName);
        } else {
            return null;
        }
    }

    public void updateMovie(Movie movie) {
        movies.put(movie.getTitle(), movie);
    }

    public void addMovie(Movie movie) {
        updateModel(movie);
        movies.put(movie.getTitle(), movie);
    }

    public void updateModel(Movie movie) {
        if (model.containsKey(movie.getGenre())) {
            (model.get(movie.getGenre())).add(movie.getTitle());
        } else {
            ArrayList<String> a = new ArrayList<String>();
            a.add(movie.getTitle());
            model.put(movie.getGenre(), a);
        }
    }

    public void deleteMovie(Movie movie) {
        String genre = movie.getGenre();
        if (model.containsKey(genre)) {
            ArrayList<String> a = model.get(movie.getGenre());
            a.remove(movie.getTitle());
            if (a.size() == 0) {
                model.remove(genre);
            }
        }
        movies.remove(movie.getTitle());
    }

    public String getMovieGenre(String movieName) {
        return movies.get(movieName).getGenre();
    }

    public String getMovieReleasedDate(String movieName) {
        return movies.get(movieName).getReleased();
    }

    public void addMovieToDictionary(Movie movie) {
        this.movies.put(movie.getTitle(), movie);
    }
}