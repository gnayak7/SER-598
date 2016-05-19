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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
 *
 * Purpose: The purpose of this project is to demonstrate
 *
 * 1) The consumption of JSON RPC server service to populate and display movie information which
 * are available through JSON RPC Server.
 *
 * 2) To demonstrate media player and to fetch media from a server asynchronously and to display it.
 *
 * 3) To search for a movie in OMDB server and fetch results asynchronously
 *
 * <p>
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version April 2016
 */
public class MainActivity extends AppCompatActivity {
    MovieDatabaseHandler movieDatabaseHandler;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManger;
    private ArrayList<MovieCell> arrayListMovieCell;
    private Context context;
    private String IP_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.IP_ADDRESS = getString(R.string.IP_ADDRESS);

        /* Set the context */
        context = this;

        /* Initialize the model */
        this.arrayListMovieCell = new ArrayList<MovieCell>();

        /* Get recycler view and fix its size to improve performance */
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setHasFixedSize(true);

        /* Set the layout manager for recycler view layout. */
        recyclerViewLayoutManger = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManger);

        /* Set recycler view adapter data model and the custom child click listener */
        recyclerViewAdapter = new RecyclerViewAdapter(arrayListMovieCell, new RecyclerViewAdapter.ViewClickMethodDelegate() {
            @Override
            public void posterClicked(int position) {
                String selectedMovieId = arrayListMovieCell.get(position).getId();
                String mediaName = movieDatabaseHandler.getMediaForMovie(selectedMovieId);
                if (mediaName != null && !mediaName.equals("No Media")) {
                    Intent intent = new Intent(context, VideoStreamer.class);
                    Log.w("media name is : ", mediaName);
                    intent.putExtra("mediaName", mediaName);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Media Doesn't Exist", Toast.LENGTH_SHORT).show();
                }
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

        /* Fetching values from Database and Initializing the Data Model */
        movieDatabaseHandler = new MovieDatabaseHandler(this);

        /* Get data from server and add it to database
         * Call these only once when initially launching the application
         */
        getMoviesListFromRPCServer();

        /* Populate data from db to display on the recycler view. */

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

            if (resultCode == MovieConstants.ADDITION_SUCCESSFUL) {
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
        movieDatabaseHandler.clearDatabase();
        getMoviesListFromRPCServer();
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

    /**
     * Gets a list of movies stored in the JSON RPC Server
     */
    public void getMoviesListFromRPCServer() {
        final ArrayList<String> moviesArrayList = new ArrayList<>();
        RPCRequestAndResponse rpcRequestAndResponse
                = new RPCRequestAndResponse(IP_ADDRESS, MovieConstants.GET_TITLES,
                new RPCRequestAndResponse.RPCResponseDelegate() {
                    @Override
                    public void processResult(MethodInformation result) {
                        extractMovieTitles(result, moviesArrayList);
                    }
                });
    }

    /**
     * Extracts movie title from the json response returned from the server.
     *
     * @params result MethodInformation which is returned from the server.
     */
    public void extractMovieTitles(MethodInformation result, ArrayList<String> moviesArrayList) {
        moviesArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(result.resultAsJson);
            JSONArray jsonArrayMovies = jsonObject.getJSONArray("result");

            int moviesCount = jsonArrayMovies.length();
            for (int i = 0; i < moviesCount; i++) {
                moviesArrayList.add(jsonArrayMovies.getString(i));
            }
            getMovieInformationFromRPCServer(moviesArrayList);
            Log.w(this.getClass().getSimpleName(), "Successfully extracted movie titles from the json data sent from the server.");
        } catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "Not able to extract movie titles from the json data sent from the server.");
        }
    }

    /**
     * Gets the movie information from the server
     */
    public void getMovieInformationFromRPCServer(ArrayList<String> moviesArrayList) {
        Log.w(this.getClass().getSimpleName(), "getMovieInformationFromRPCServer");
        if (moviesArrayList != null) {
            Log.w(this.getClass().getSimpleName(), "moviesArrayList is not Null");
            int moviesCount = moviesArrayList.size();
            boolean isDatabaseCreated = movieDatabaseHandler.checkIfDatabaseExists();
            for (int i = 0; i < moviesCount; i++) {
                Log.w(this.getClass().getSimpleName(), "Movie Information from server");
                // check if movie is present in the database if not fetch it
                String movieTitle = moviesArrayList.get(i);
                /* The below 'OR' condition works because if database does not exist it will be created by second condition */
                if (!movieDatabaseHandler.isMoviePresentInDatabase(movieTitle)) {
                    // check if movie is present in database
                    Log.w("shitbrick: ", movieTitle);
                    RPCRequestAndResponse rpcRequestAndResponse = new RPCRequestAndResponse(IP_ADDRESS, MovieConstants.GET_MOVIE, new String[]{movieTitle},
                            new RPCRequestAndResponse.RPCResponseDelegate() {
                                @Override
                                public void processResult(MethodInformation result) {
                                    extractMovieDescription(result);
                                }
                            });
                }
            }
        }
    }

    /**
     * Extracts the movie information returned from the server
     *
     * @params result MethodInformation returned from the server.
     */
    public void extractMovieDescription(MethodInformation result) {
        try {
            JSONObject myJSONMovie = new JSONObject(result.resultAsJson);
            String movieJSONString = myJSONMovie.get("result").toString();
            Movie movie = new Movie(movieJSONString, true);
            boolean isMovieAdded = movieDatabaseHandler.addMovieToDatabase(movie);
            if (isMovieAdded) {
                arrayListMovieCell.add(new MovieCell(movie.getId(), movie.getTitle(), movie.getRated(), movie.getPoster()));
                //Toast.makeText(MainActivity.this, movie.getTitle() + " Movie Successfully Added To Database", Toast.LENGTH_SHORT).show();
                recyclerViewAdapter.notifyDataSetChanged();
            }
            Log.w(this.getClass().getSimpleName(), "Successfully extracted movie from the json data sent from the server.");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.w(this.getClass().getSimpleName(), "Not able to extract movie from the json data sent from the server.");
        }
    }
}

/**
 * References:
 * <p>
 * 1. http://developer.android.com/guide/topics/data/data-storage.html
 * 2. http://pooh.poly.asu.edu/Mobile/Assigns/LabsSpr2016/Assign9/assign9.html
 * 3. http://developer.android.com/training/building-connectivity.html
 * 4. http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
 * 5. http://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 */