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

// This class enables addition of new movie to the movie library
public class NewMovieHandler extends AppCompatActivity {

    static EditText releasedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie_handler);
        releasedDate = (EditText) findViewById(R.id.new_movie_editText_released);
    }

    public void saveAndExit(View v) {
        EditText movieTitle = (EditText) findViewById(R.id.new_movie_editText_title);
        String title = movieTitle.getText().toString();
        EditText movieYear = (EditText) findViewById(R.id.new_movie_editText_year);
        String year = movieYear.getText().toString();
        EditText movieRated = (EditText) findViewById(R.id.new_movie_editText_rated);
        String rated = movieRated.getText().toString();
        EditText movieReleased = (EditText) findViewById(R.id.new_movie_editText_released);
        String released = movieReleased.getText().toString();
        EditText movieRuntime = (EditText) findViewById(R.id.new_movie_editText_runtime);
        String runtime = movieRuntime.getText().toString();
        EditText movieGenre = (EditText) findViewById(R.id.new_movie_editText_genre);
        String genre = movieGenre.getText().toString();
        EditText movieActors = (EditText) findViewById(R.id.new_movie_editText_actor);
        String actors = movieActors.getText().toString();
        EditText moviePlot = (EditText) findViewById(R.id.new_movie_editText_plot);
        String plot = moviePlot.getText().toString();

        Movie newMovie = new Movie(title, year, rated, released, runtime, genre, actors, plot);

        Intent i = new Intent();
        i.putExtra("movie", newMovie);
        setResult(RESULT_OK, i);
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
            String monthString = new DateFormatSymbols().getMonths()[month-1];
            releasedDate.setText( day + " " + monthString.substring(0,3)  + " " + year);
        }
    }
}
