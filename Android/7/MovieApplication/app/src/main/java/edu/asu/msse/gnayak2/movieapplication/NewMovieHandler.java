package edu.asu.msse.gnayak2.movieapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;

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
 * Purpose: This class enables addition of new movie to the movie library.
 * It provides a search functionality to asynchronously search for a movie
 * in OMDB and fetch movie details if successful.
 * <p/>
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */


public class NewMovieHandler extends AppCompatActivity {

    static EditText releasedDate;
    private EditText movieTitle;
    private EditText movieYear;
    private EditText movieRated;
    private EditText movieReleased;
    private EditText movieRuntime;
    private EditText movieGenre;
    private EditText movieActors;
    private EditText moviePlot;
    private String poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie_handler);
        releasedDate = (EditText) findViewById(R.id.new_movie_editText_released);

        movieTitle = (EditText) findViewById(R.id.new_movie_editText_title);
        movieYear = (EditText) findViewById(R.id.new_movie_editText_year);
        movieRated = (EditText) findViewById(R.id.new_movie_editText_rated);
        movieReleased = (EditText) findViewById(R.id.new_movie_editText_released);
        movieRuntime = (EditText) findViewById(R.id.new_movie_editText_runtime);
        movieGenre = (EditText) findViewById(R.id.new_movie_editText_genre);
        movieActors = (EditText) findViewById(R.id.new_movie_editText_actor);
        moviePlot = (EditText) findViewById(R.id.new_movie_editText_plot);
        this.poster = "No Poster";
    }

    public void saveAndExit(View v) {
        String title = movieTitle.getText().toString();
        String year = movieYear.getText().toString();
        String rated = movieRated.getText().toString();
        String released = movieReleased.getText().toString();
        String runtime = movieRuntime.getText().toString();
        String genre = movieGenre.getText().toString();
        String actors = movieActors.getText().toString();
        String plot = moviePlot.getText().toString();
        Movie newMovie = new Movie(title, year, rated, released, runtime, genre, actors, plot, this.poster);

        Intent i = new Intent();
        i.putExtra("movie", newMovie);
        setResult(MovieConstants.ADDITON_SUCCESSFUL, i);
        this.finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private Calendar calendar;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String monthString = new DateFormatSymbols().getMonths()[month - 1];
            releasedDate.setText(day + " " + monthString.substring(0, 3) + " " + year);
        }
    }

    public void onSearchMovieMovieClicked(View v) {
        EditText movieTitle = (EditText) findViewById(R.id.new_movie_editText_title);
        String title = movieTitle.getText().toString();

        AsyncConnect asyncConnect = new AsyncConnect(new AsyncConnect.PostExecuteMethodDelegate() {
            @Override
            public void displayMovieDescription(String response) {
                // read response if response is true then set the fields of the edit texts
                //else throw a toast for error
                JSONObject jsonObj = null;
                String responseValue = "";
                try {
                    jsonObj = new JSONObject(response);
                    responseValue = jsonObj.getString("Response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (responseValue.equals("True")) {
                    Movie movie = new Movie(response);
                    populateFields(movie);
                } else {
                    Toast.makeText(getApplicationContext(), "Movie doesn't exist in OMDB", Toast.LENGTH_SHORT).show();
                }
            }
        });
        asyncConnect.execute(new String[]{title});
    }

    public void populateFields(Movie movie) {
        movieTitle.setText(movie.getTitle());
        movieYear.setText(movie.getYear());
        movieRated.setText(movie.getRated());
        movieReleased.setText(movie.getReleased());
        movieRuntime.setText(movie.getRunTime());
        movieGenre.setText(movie.getGenre());
        movieActors.setText(movie.getActors());
        moviePlot.setText(movie.getPlot());
        this.poster = movie.getPoster();
    }
}
