package edu.asu.msse.gnayak2.movieapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

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
 *
 * Purpose: This application demonstrates the usage of SQLite Database.
 * 1) How to initialize an application database by copying a prebuilt database
 *    from raw directory to application data space.
 * 2) How to asynchronously fetch movie description from OMDB server by making
 *    a HTTP call to OMDB.
 * 3) Store the fetched results in the database.
 * 4) Display all the movies stored in database in a recycler view.
 * 5) Addition, Updation and Deletiong of moives from database.
 *
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */
public class MainActivity extends AppCompatActivity {
    MovieDatabaseHandler movieDatabaseHandler;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManger;
    private ArrayList<MovieCell> arrayListMovieCell;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the context
        context = this;

        //Initialize the model
        this.arrayListMovieCell = new ArrayList<MovieCell>();

        // Get recycler view and fix its size to improve performance
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setHasFixedSize(true);

        // Set the layout manager for recycler view layout.
        recyclerViewLayoutManger = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManger);

        // Set recycler view adapter data model and the custom child click listener
        recyclerViewAdapter = new RecyclerViewAdapter(arrayListMovieCell, new RecyclerViewAdapter.ViewClickMethodDelegate() {
            @Override
            public void posterClicked(int position) {
                Toast.makeText(MainActivity.this, "Place Holder for expanding image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void movieDescriptionClicked(int position) {
                Toast.makeText(MainActivity.this, "MovieDescription", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MovieDetailsActivityController.class);
                String selectedMovieId = arrayListMovieCell.get(position).getId();
                intent.putExtra("movieId", selectedMovieId);
                startActivityForResult(intent, MovieConstants.EDIT_OR_DELETE_MOVIE);
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);

        // Fetching values from Database and Initializing the Data Model
        movieDatabaseHandler = new MovieDatabaseHandler(this);
        try {
            movieDatabaseHandler.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        arrayListMovieCell = initializeArrayMoviesCellData();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public ArrayList<MovieCell> initializeArrayMoviesCellData() {
        return movieDatabaseHandler.getAllMovieCellInformationDatabase(this.arrayListMovieCell);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.w(this.getClass().getSimpleName(), " onCreateOptionsMenu.");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Log.w(this.getClass().getSimpleName(), " onOptionsItemSelected");

        if (menuItem.getItemId() == R.id.addicon) {
            Intent i = new Intent(this, NewMovieHandler.class);
            startActivityForResult(i, MovieConstants.ADD_NEW_MOVIE);

        } else if (menuItem.getItemId() == R.id.reseticon) {
            refresh();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.w(this.getClass().getSimpleName(), " Coming to  To Add Movie To Database");

        if (requestCode == MovieConstants.EDIT_OR_DELETE_MOVIE) {
            if (resultCode == MovieConstants.MOVIE_EDITED) {
                Movie editedMovie = (Movie) data.getSerializableExtra("movie");

                boolean isMovieUpdated = movieDatabaseHandler.updateMovieInformationInDatabase(editedMovie);

                if (isMovieUpdated) {
                    int movieIndex = getIndexOfMovie(editedMovie.getId());
                    MovieCell movieCell = arrayListMovieCell.get(movieIndex);
                    movieCell.setTitle(editedMovie.getTitle());
                    movieCell.setRating(editedMovie.getRated());
                    movieCell.setImageUrl(editedMovie.getPoster());
                    Toast.makeText(MainActivity.this, editedMovie.getTitle() + " Movie Successfully Updated In Database", Toast.LENGTH_SHORT).show();
                    recyclerViewAdapter.notifyDataSetChanged();
                    Log.w(this.getClass().getSimpleName(), " Movie Successfully Updated In Database");
                } else {
                    Toast.makeText(MainActivity.this, editedMovie.getTitle() + " Failed To Updated Movie In Database", Toast.LENGTH_SHORT).show();
                    Log.w(this.getClass().getSimpleName(), " Failed To Updated Movie In Database");
                }
            }
        }

        if (requestCode == MovieConstants.EDIT_OR_DELETE_MOVIE) {
            if (resultCode == MovieConstants.MOVIE_DELETED) {
                String movieId = data.getStringExtra("movieId");

                boolean isMovieDeleted = movieDatabaseHandler.deleteMovieFromDatabase(movieId);

                if (isMovieDeleted) {
                    int movieIndex = getIndexOfMovie(movieId);
                    arrayListMovieCell.remove(movieIndex);
                    Toast.makeText(MainActivity.this, "Movie Successfully Deleted In Database", Toast.LENGTH_SHORT).show();
                    recyclerViewAdapter.notifyDataSetChanged();
                    Log.w(this.getClass().getSimpleName(), "Movie Successfully Deleted In Database");
                } else {
                    Toast.makeText(MainActivity.this, " Failed To Delete Movie In Database", Toast.LENGTH_SHORT).show();
                    Log.w(this.getClass().getSimpleName(), " Failed To Delete Movie In Database");
                }
            }
        }

        if (requestCode == MovieConstants.ADD_NEW_MOVIE) {
            Log.w(this.getClass().getSimpleName(), " Coming to  To Add Movie To Database");

            if (resultCode == MovieConstants.ADDITON_SUCCESSFUL) {
                Movie newMovie = (Movie) data.getSerializableExtra("movie");

                boolean isMovieAddedToDatabase = movieDatabaseHandler.addMovieToDatabase(newMovie);

                if (isMovieAddedToDatabase) {
                    arrayListMovieCell.add(new MovieCell(newMovie.getId(), newMovie.getTitle(), newMovie.getRated(), newMovie.getPoster()));
                    Toast.makeText(MainActivity.this, newMovie.getTitle() + " Movie Successfully Added To Database", Toast.LENGTH_SHORT).show();
                    recyclerViewAdapter.notifyDataSetChanged();
                    Log.w(this.getClass().getSimpleName(), " Movie Successfully Added To Database");
                } else {
                    Toast.makeText(MainActivity.this, newMovie.getTitle() + " Failed To Add Movie To Database", Toast.LENGTH_SHORT).show();
                    Log.w(this.getClass().getSimpleName(), " Failed To Add Movie To Database");
                }
            }
        }
    }

    public void refresh() {
        try {
            movieDatabaseHandler.copyDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        arrayListMovieCell = initializeArrayMoviesCellData();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Returns the index of the movie in the array list.
     *
     * @params movieId Takes a String as a parameter.
     * @returns Returns int.
     */
    public int getIndexOfMovie(String movieId) {
        int size = arrayListMovieCell.size();

        for (int i = 0; i < size; i++) {
            if (movieId.equals(arrayListMovieCell.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }
}

/**
 * References:
 *
 * 1. http://developer.android.com/guide/topics/data/data-storage.html
 * 2. http://pooh.poly.asu.edu/Mobile/Assigns/LabsSpr2016/Assign7/assign7.html
 * 3. http://developer.android.com/training/building-connectivity.html
 * 4. http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
 * 5. http://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 *
 */