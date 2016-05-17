package edu.asu.msse.gnayak2.movieapplication;

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
 * Purpose: A contract class which holds database table information.
 *
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */
public class MovieDatabaseContract {
    MovieDatabaseContract() {

    }

    public static abstract class MovieEntry {
        public static final String TABLE_NAME = "movie_description_table";
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_YEAR = "year";
        public static final String MOVIE_RATED = "rated";
        public static final String MOVIE_RELEASED = "released";
        public static final String MOVIE_RUNTIME = "runtime";
        public static final String MOVIE_GENRE = "genre";
        public static final String MOVIE_ACTORS = "actors";
        public static final String MOVIE_PLOT = "plot";
        public static final String MOVIE_POSTER = "poster";
    }
}
