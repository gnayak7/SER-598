package edu.asu.msse.gnayak2.movieapplication;

import android.os.AsyncTask;

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
 * Purpose: This class makes an asynchronous HTTP connection to fetch movie details
 * from OMDB server. It takes a delegate as a call back which will be invoked
 * once the response is received from the server.
 *
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */
public class AsyncConnect extends AsyncTask<String, Integer, String> {
    private PostExecuteMethodDelegate postExecuteMethodDelegate;

    /**
     * Interface class which acts as a delegate.
     */
    public interface PostExecuteMethodDelegate {
        /**
         * Delegate method.
         *
         * @param response Takes a string url.
         * @returns Returns void
         */
        void displayMovieDescription(String response);
    }

    /**
     * Constructor
     *
     * @param postExecuteMethodDelegate Takes a PostExecuteMethodDelegate.
     * @return Returns an object.
     */
    AsyncConnect(PostExecuteMethodDelegate postExecuteMethodDelegate) {
        this.postExecuteMethodDelegate = postExecuteMethodDelegate;
    }

    /**
     * Overridden method which gets executed after background task finishes.
     *
     * @param response Takes a string url.
     * @return Returns void.
     */
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        this.postExecuteMethodDelegate.displayMovieDescription(response);
    }

    /**
     * Called in between the background task to update the progress.
     *
     * @param values Takes a variable number of Integers.
     * @return Returns void.
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Overridden method to make a non UI network call.
     *
     * @param params Takes variable number of string.
     * @return Returns String.
     */
    @Override
    protected String doInBackground(String... params) {
        String stringURL = buildUrlString(params[0]);
        HTTPConnection httpConnection = new HTTPConnection(stringURL);
        String jsonResponse = httpConnection.makeNetworkCall();
        return jsonResponse;
    }

    /**
     * Builds a url string by replacing spaces by "+".
     *
     * @param params Takes a string url.
     * @return Returns String.
     */
    public String buildUrlString(String params) {
        String[] words = params.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(words[0]);
        int wordsLength = words.length;
        for (int i = 1; i < wordsLength; i++) {
            stringBuilder.append("+");
            stringBuilder.append(words[i]);
        }

        String urlString = "http://www.omdbapi.com/?t=" + stringBuilder.toString() + "&y=&plot=short&r=json";
        return urlString;
    }
}
