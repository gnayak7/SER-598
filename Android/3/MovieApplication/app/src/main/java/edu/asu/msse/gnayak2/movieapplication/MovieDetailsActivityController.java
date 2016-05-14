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

import java.text.DateFormatSymbols;
import java.util.Calendar;

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
 * Purpose:The purpose of this application is to demonstrate expandable list view adapter. Data is stored ina
 * a json file in raw folder. Additionally the applicaiton demonstrates use of picker.
 * <p>
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version February 15 2016
 */

// Class which helps to edit an existing movie or to view it.
public class MovieDetailsActivityController extends AppCompatActivity {
    private Movie movie;
    static EditText releasedDate;
    private EditText movieTitle;
    private EditText movieYear;
    private EditText movieRated;
    private EditText movieReleased;
    private EditText movieRuntime;
    private EditText movieGenre;
    private EditText movieActors;
    private EditText moviePlot;
    private int RESULT_DELETED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_activity_controller);
        releasedDate = (EditText) findViewById(R.id.movie_details_editText_released);

        movie = (Movie) getIntent().getSerializableExtra("movie");

        movieTitle = (EditText) findViewById(R.id.movie_details_editText_title);
        movieTitle.setText(movie.getTitle());
        movieYear = (EditText) findViewById(R.id.movie_details_editText_year);
        movieYear.setText(movie.getYear());
        movieRated = (EditText) findViewById(R.id.movie_details_editText_rated);
        movieRated.setText(movie.getRated());
        movieReleased = (EditText) findViewById(R.id.movie_details_editText_released);
        movieReleased.setText(movie.getReleased());
        movieRuntime = (EditText) findViewById(R.id.movie_details_editText_runtime);
        movieRuntime.setText(movie.getRunTime());
        movieGenre = (EditText) findViewById(R.id.movie_details_editText_genre);
        movieGenre.setText(movie.getGenre());
        movieActors = (EditText) findViewById(R.id.movie_details_editText_actor);
        movieActors.setText(movie.getActors());
        moviePlot = (EditText) findViewById(R.id.movie_details_editText_plot);
        moviePlot.setText(movie.getPlot());
    }


    public void onSaveClick(View v) {
        String title = movieTitle.getText().toString();
        String year = movieYear.getText().toString();
        String rated = movieRated.getText().toString();
        String released = movieReleased.getText().toString();
        String runtime = movieRuntime.getText().toString();
        String genre = movieGenre.getText().toString();
        String actors = movieActors.getText().toString();
        String plot = moviePlot.getText().toString();

        Movie newMovie = new Movie(title, year, rated, released, runtime, genre, actors, plot);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        intent.putExtra("movie", newMovie);
        intent.putExtra("orignal", movie);
        this.finish();
    }

    public void onDelete(View v) {
        Intent intent = new Intent();
        setResult(RESULT_DELETED, intent);
        intent.putExtra("movie", movie);
        this.finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String monthString = new DateFormatSymbols().getMonths()[month-1];
            releasedDate.setText( day + " " + monthString.substring(0,3)  + " " + year);
        }
    }
}
/* References:
1) http://pooh.poly.asu.edu
2) http://developer.android.com/sdk/index.html
*/