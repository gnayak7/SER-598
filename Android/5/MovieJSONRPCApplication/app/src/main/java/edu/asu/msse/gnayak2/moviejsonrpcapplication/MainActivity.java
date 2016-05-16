package edu.asu.msse.gnayak2.moviejsonrpcapplication;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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

// This is the main activity from where the application starts.
public class MainActivity extends AppCompatActivity {
    private MovieAdapter movieAdapter;
    private MovieLibrary movieLibrary;
    private Context ctx = this;
    public ExpandableListView expandableListView;
    private String IP_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.IP_ADDRESS = getString(R.string.IP_ADDRESS);
        getDataFromServerForAdapter();
    }

    public void getDataFromServerForAdapter() {
        RPCRequestAndResponse rpcRequestAndResponse = new RPCRequestAndResponse(IP_ADDRESS,Constants.GET_MODEL_INFORMATION,
                new RPCRequestAndResponse.RPCResponseDelegate() {
                    @Override
                    public void processResult(MethodInformation result) {
                        buildMovieLibrary(result);
                    }
                });
    }

    public void setAdapter(LinkedHashMap<String, ArrayList<String>> myModel) {
        this.movieLibrary = new MovieLibrary(myModel);
        movieAdapter = new MovieAdapter(this, movieLibrary);

        expandableListView = (ExpandableListView) findViewById(R.id.main_expandable_list_view);
        expandableListView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selectedMovieName = (String) movieAdapter.getChild(groupPosition, childPosition);
                getAndDisplayMovie(selectedMovieName);
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.EDIT_OR_DELETE) {
            if (resultCode == RESULT_OK) {
                final Movie editedMovie = (Movie) data.getSerializableExtra("movie");
                Movie originalMovie = (Movie) data.getSerializableExtra("orignal");
                boolean isMovieTitleSame = originalMovie.getTitle().equals(editedMovie.getTitle());
                boolean isGenreSame = originalMovie.getGenre().equals(editedMovie.getGenre());

                if (isMovieTitleSame && isGenreSame) {
                    movieLibrary.updateMovie(editedMovie);
                    movieAdapter.notifyDataSetChanged();
                    RPCRequestAndResponse rpcRequestAndResponse = new RPCRequestAndResponse(IP_ADDRESS, Constants.UPDATE_MOVIE, new String[]{editedMovie.toJsonString()});
                } else {
                    movieLibrary.deleteMovie(originalMovie);
                    movieLibrary.addMovie(editedMovie);
                    movieAdapter.notifyDataSetChanged();
                    RPCRequestAndResponse rpcRequestAndResponse = new RPCRequestAndResponse(IP_ADDRESS, Constants.DELETE_AND_ADD, new String[]{originalMovie.toJsonString(), editedMovie.toJsonString()});
                }
            }
        }

        if (requestCode == Constants.EDIT_OR_DELETE) {
            if (resultCode == Constants.MOVIE_DELETED) {
                final Movie editedMovie = (Movie) data.getSerializableExtra("movie");
                movieLibrary.deleteMovie(editedMovie);
                movieAdapter.notifyDataSetChanged();
                RPCRequestAndResponse rpcRequestAndResponse = new RPCRequestAndResponse(IP_ADDRESS, Constants.DELETE_MOVIE, new String[]{editedMovie.getTitle()});
            }
        }

        if (requestCode == Constants.NEW_MOVIE) {
            if (resultCode == RESULT_OK) {
                final Movie newMovie = (Movie) data.getSerializableExtra("movie");
                movieLibrary.addMovie(newMovie);
                movieAdapter.notifyDataSetChanged();
                Log.w("check", newMovie.toJsonString());
                RPCRequestAndResponse rpcRequestAndResponse = new RPCRequestAndResponse(IP_ADDRESS, Constants.ADD_MOVIE, new String[]{newMovie.toJsonString()});
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
        if (menuItem.getItemId() == R.id.addicon) {
            Intent i = new Intent(this, NewMovieHandler.class);
            startActivityForResult(i, Constants.NEW_MOVIE);

        } else if (menuItem.getItemId() == R.id.reseticon) {
            RPCRequestAndResponse rpcRequestAndResponse = new RPCRequestAndResponse(IP_ADDRESS, Constants.REFRESH, new RPCRequestAndResponse.RPCResponseDelegate() {
                @Override
                public void processResult(MethodInformation result) {
                    getDataFromServerForAdapter();
                }
            });
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void buildMovieLibrary(MethodInformation result) {
        LinkedHashMap<String, ArrayList<String>> myModel = new LinkedHashMap<>();
        try {
            JSONObject jo = new JSONObject(result.resultAsJson);
            JSONObject jsonModel = (JSONObject) jo.get("result");
            Iterator<String> keys = jsonModel.keys();
            ArrayList<String> tempArrayList;
            Log.w("Successful", jsonModel.toString());
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = (JSONArray) jsonModel.get(key);
                tempArrayList = new ArrayList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    tempArrayList.add((String) jsonArray.get(i));
                }
                myModel.put(key, tempArrayList);
            }
        } catch (Exception e) {
            Log.w("Exception", "Not able to process the json data sent from the server.");
        }
        setAdapter(myModel);
    }

    public void getAndDisplayMovie(String selectedMovieName) {
        final Intent intent = new Intent(ctx, MovieDetailsActivityController.class);
        final Movie selectedMovie = movieLibrary.getMovie(selectedMovieName);
        if (selectedMovie != null) {
            intent.putExtra("movie", selectedMovie);
            startActivityForResult(intent, Constants.EDIT_OR_DELETE);
        } else {
            RPCRequestAndResponse rpcRequestAndResponse = new RPCRequestAndResponse(IP_ADDRESS, Constants.GET_MOVIE, new String[]{selectedMovieName},
                    new RPCRequestAndResponse.RPCResponseDelegate() {
                        @Override
                        public void processResult(MethodInformation result) {
                            String movieJSONString = "";
                            try {
                                JSONObject myJSONMovie = new JSONObject(result.resultAsJson);
                                movieJSONString = myJSONMovie.get("result").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Movie selectedMovie = new Movie(movieJSONString);
                            movieLibrary.addMovieToDictionary(selectedMovie);
                            intent.putExtra("movie", selectedMovie);
                            startActivityForResult(intent, Constants.EDIT_OR_DELETE);
                        }
                    });
        }
    }


}

/*
1. pooh.poly.asu.edu
2. developer.google.com
*/