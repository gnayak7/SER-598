package edu.asu.msse.gnayak2.movieapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
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
 * Purpose: A database helper class which initializes database from a
 * database file present in the raw folder of the application. Additionally
 * it assists in addition, updation, deletion of movies from database.
 * <p>
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */
public class MovieDatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movie_description.db";
    private static final int DATABASE_VERSION = 1;
    private final String DATABASE_PATH = "/data/data/edu.asu.msse.gnayak2.movieapplication/databases/";
    private Context context;


    public MovieDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //getWritableDatabase();
    }

    /**
     * Overridden method to creates a database table.
     *
     * @param db The SQLite database
     * @return Returns void.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*try {

        } catch (IOException e) {
            Log.w(this.getClass().getSimpleName(), "Custome exception");
            e.printStackTrace();
        }*/
        Log.w(this.getClass().getSimpleName(), " OnCreate");
    }

    /**
     * Overridden method to upgrade a database table.
     *
     * @param db         The SQLite database
     * @param oldVersion Old Version Number
     * @param newVersion New Version Number
     * @return Returns void.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS " + MovieDatabaseContract.MovieEntry.TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);
    }

    /**
     * Adds the movie to the database.
     *
     * @param movie Movie Object.
     * @return Returns true if insertion is successful else returns false.
     */
    public boolean addMovieToDatabase(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getContentValues(movie, true);

        long responseValue = db.insert(MovieDatabaseContract.MovieEntry.TABLE_NAME, null, contentValues);
        db.close();

        if (responseValue == -1) {
            // Also it may fail because of unique ID. Handle this case.
            return false;
        } else {
            return true;
        }
    }

    /**
     * Update the movie description in the database.
     *
     * @param movie Movie Object.
     * @return Returns true if update is successful else returns false.
     */
    public boolean updateMovieInformationInDatabase(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getContentValues(movie, false);

        int rowCount = db.update(MovieDatabaseContract.MovieEntry.TABLE_NAME, contentValues, MovieDatabaseContract.MovieEntry.MOVIE_ID + " = ? ", new String[]{movie.getId()});
        db.close();

        if (rowCount == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Deletes the movie description from the database.
     *
     * @param movieId The Id of the movie.
     * @return Returns true if deletion is successful else returns false.
     */
    public boolean deleteMovieFromDatabase(String movieId) {
        SQLiteDatabase db = getWritableDatabase();

        int rowCount = db.delete(MovieDatabaseContract.MovieEntry.TABLE_NAME, MovieDatabaseContract.MovieEntry.MOVIE_ID + " = ? ", new String[]{movieId});
        db.close();

        if (rowCount == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Fetches the movie description from the database.
     *
     * @param movieId The Id of the movie.
     * @return Returns a Movie object if found else returns null.
     */

    public Movie getMovieInformationFromDatabase(String movieId) {
        SQLiteDatabase db = getWritableDatabase();

        String querySelection = "SELECT * FROM " + MovieDatabaseContract.MovieEntry.TABLE_NAME + " WHERE " + MovieDatabaseContract.MovieEntry.MOVIE_ID + " = ? ";
        Cursor cursor = db.rawQuery(querySelection, new String[]{movieId});

        if (cursor == null) {
            Log.w(this.getClass().getSimpleName(), " Cursor is null");
            db.close();
            return null;
        }

        // Close db only after cursor operation. Else cursor will be invalid.
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        } else {
            Log.w(this.getClass().getSimpleName(), " getMovieInformationFromDatabase(String movieId) Successful Query");
            cursor.moveToFirst();
            Movie movie = getMovieFromCursor(cursor);
            // Close the cursor first before closing the database. Else cursor will be invalid.
            cursor.close();
            db.close();
            return movie;
        }
    }

    /**
     * Fetches the movie cell information from the database.
     *
     * @return ArrayList of Movie
     */
    // Change this to be compatible with the getAllMovieCellInformationDatabase() method
    public ArrayList<Movie> getAllMovieInformationFromDatabase() {
        ArrayList<Movie> moviesArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String querySelection = "SELECT * FROM " + MovieDatabaseContract.MovieEntry.TABLE_NAME + " WHERE 1";
        Cursor cursor = db.rawQuery(querySelection, null);

        if (cursor == null) {
            Log.w(this.getClass().getSimpleName(), " getAllMovieInformationFromDatabase() Unsuccessful Query");
            db.close();
            return null;
        }

        // Close db only after cursor operation. Else cursor will be invalid.
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        } else {
            Log.w(this.getClass().getSimpleName(), " getAllMovieInformationFromDatabase() Successful Query");
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Movie movie = getMovieFromCursor(cursor);
                moviesArrayList.add(movie);
                cursor.moveToNext();
            }

            // Close the cursor first before closing the database. Else cursor will be invalid.
            cursor.close();
            db.close();
            return moviesArrayList;
        }
    }

    /**
     * Fetches the movie cell information from the database.
     *
     * @param moviesCellArrayList ArrayList of MovieCell
     * @return ArrayList of MovieCell
     */
    public ArrayList<MovieCell> getAllMovieCellInformationDatabase(ArrayList<MovieCell> moviesCellArrayList) {
        moviesCellArrayList.clear();
        SQLiteDatabase db = getWritableDatabase();

        String querySelection = "SELECT * FROM " + MovieDatabaseContract.MovieEntry.TABLE_NAME + " WHERE 1";
        Cursor cursor = db.rawQuery(querySelection, null);

        if (cursor == null) {
            Log.w(this.getClass().getSimpleName(), " Cursor is null");
            db.close();
            return moviesCellArrayList;
        }

        // Close db only after cursor operation. Else cursor will be invalid.
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return moviesCellArrayList;
        } else {
            Log.w(this.getClass().getSimpleName(), " Get Cell information Successful Query");
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String id = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_TITLE));
                String rated = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_RATED));
                String poster = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_POSTER));

                cursor.moveToNext();

                MovieCell movieCell = new MovieCell(id, title, rated, poster);
                moviesCellArrayList.add(movieCell);
            }

            // Close the cursor first before closing the database. Else cursor will be invalid.
            cursor.close();
            db.close();
            return moviesCellArrayList;
        }

    }

    private ContentValues getContentValues(Movie movie, boolean includeMovieId) {
        ContentValues contentValues = new ContentValues();

        if (includeMovieId) {
            contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_ID, movie.getId());
        }
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_YEAR, movie.getYear());
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_RATED, movie.getRated());
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_RELEASED, movie.getReleased());
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_RUNTIME, movie.getRunTime());
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_GENRE, movie.getGenre());
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_ACTORS, movie.getActors());
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_PLOT, movie.getPlot());
        contentValues.put(MovieDatabaseContract.MovieEntry.MOVIE_POSTER, movie.getPoster());
        return contentValues;
    }

    private Movie getMovieFromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_ID));
        String title = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_TITLE));
        String year = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_YEAR));
        String rated = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_RATED));
        String released = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_RELEASED));
        String runtime = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_RUNTIME));
        String genre = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_GENRE));
        String actors = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_ACTORS));
        String plot = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_PLOT));
        String poster = cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.MOVIE_POSTER));
        return new Movie(id, title, year, rated, released, runtime, genre, actors, plot, poster);
    }

    /**
     * Uses existing database. Else copies from raw folder.
     *
     * @throws IOException
     */
    public void createDatabase() throws IOException {
        Log.w(this.getClass().getSimpleName(), "crateDatabase() method called");
        boolean doesDBExist = checkIfDatabaseExists();
        if (!doesDBExist) {
            try {
                /*This is gonna create an empty database in systems default path
                 then we can overwrite this database with our database.*/
                this.getReadableDatabase();
                copyDatabase();
                Log.w(this.getClass().getSimpleName(), "Database copied from raw folder");
            } catch (IOException e) {
                Log.w(this.getClass().getSimpleName(), "Error Copying Database " + e.getMessage());
            }
        } else {
            Log.w(this.getClass().getSimpleName(), "Database already exists");
        }
    }

    /**
     * Checks if database exists.
     *
     * @return True if present else False.
     */
    public boolean checkIfDatabaseExists() {
        SQLiteDatabase sqLiteDatabase = null;
        String database = DATABASE_PATH + DATABASE_NAME;
        try {
            sqLiteDatabase = SQLiteDatabase.openDatabase(database, null, SQLiteDatabase.OPEN_READONLY);
            Log.w(this.getClass().getSimpleName(), "Database exists");
        } catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "Database doesn't exist");
        }

        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Copies database from raw to application data
     *
     * @throws IOException
     */
    public void copyDatabase() throws IOException {
        InputStream inputStream = context.getResources().openRawResource(R.raw.movie_description);
        String database = DATABASE_PATH + DATABASE_NAME;
        OutputStream outputStream = new FileOutputStream(database);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
