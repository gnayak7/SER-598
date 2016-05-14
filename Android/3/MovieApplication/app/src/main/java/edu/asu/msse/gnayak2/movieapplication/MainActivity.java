package edu.asu.msse.gnayak2.movieapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

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
 * @version February 2016
 */

// This is the main activity class which controls the expandable list view.
public class MainActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter;
    private MovieLibrary movieLibrary;
    private Context ctx = this;
    private int EDIT_OR_DELETE = 1;
    private int NEW_MOVIE = 2;
    private int MOVIE_DELETED = 3;
    public ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAdapter();

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(ctx, MovieDetailsActivityController.class);
                String selectedMovieName = (String) movieAdapter.getChild(groupPosition, childPosition);
                Movie selectedMovie = movieLibrary.getMovie(selectedMovieName);
                intent.putExtra("movie", selectedMovie);
                startActivityForResult(intent, EDIT_OR_DELETE);
                return false;
            }
        });
    }

    public void setAdapter() {
        JSONFileReader jsonFileReader = new JSONFileReader(this, "movies.json");
        movieLibrary = new MovieLibrary(jsonFileReader.getJSONArray());
        movieAdapter = new MovieAdapter(this, movieLibrary);

        expandableListView = (ExpandableListView) findViewById(R.id.main_expandable_list_view);
        expandableListView.setAdapter(movieAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_OR_DELETE) {
            if (resultCode == RESULT_OK) {
                Movie editedMovie = (Movie) data.getSerializableExtra("movie");
                ;
                Movie originalMovie = (Movie) data.getSerializableExtra("orignal");
                boolean isMovieTitleSame = originalMovie.getTitle().equals(editedMovie.getTitle());
                boolean isGenreSame = originalMovie.getGenre().equals(editedMovie.getGenre());

                if (isMovieTitleSame && isGenreSame) {
                    movieLibrary.updateMovie(editedMovie);
                } else {
                    movieLibrary.deleteMovie(originalMovie);
                    movieLibrary.addMovie(editedMovie);
                }
                movieAdapter.notifyDataSetChanged();
                Log.w("edited movieplot", editedMovie.getPlot());
            }
        }

        if (requestCode == EDIT_OR_DELETE) {
            if (resultCode == MOVIE_DELETED) {
                Movie editedMovie = (Movie) data.getSerializableExtra("movie");
                movieLibrary.deleteMovie(editedMovie);
                movieAdapter.notifyDataSetChanged();
                Log.w("edited movieplot", editedMovie.getPlot());
            }
        }

        if (requestCode == NEW_MOVIE) {
            if (resultCode == RESULT_OK) {
                Movie newMovie = (Movie) data.getSerializableExtra("movie");
                movieLibrary.addMovie(newMovie);
                movieAdapter.notifyDataSetChanged();
                Log.w("new movieplot", newMovie.getPlot());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.w("inside inflater", "heyy");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Log.w("optionsItemSelected", "here");

        if (menuItem.getItemId() == R.id.addicon) {
            Intent i = new Intent(this, NewMovieHandler.class);
            startActivityForResult(i, NEW_MOVIE);

        } else if (menuItem.getItemId() == R.id.reseticon) {
            setAdapter();
        }
        return super.onOptionsItemSelected(menuItem);

    }
}
