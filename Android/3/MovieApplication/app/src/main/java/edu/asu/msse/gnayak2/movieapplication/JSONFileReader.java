package edu.asu.msse.gnayak2.movieapplication;

import android.content.Context;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

//This class reads json file and returns array of json objects.
public class JSONFileReader {

    private Context parent;
    private JSONArray jsonArray;
    private String filename;

    JSONFileReader(Context parent, String filename) {
        this.parent = parent;
        this.filename = filename;
        buildJSONArray();
    }

    public JSONArray getJSONArray() {
        if (jsonArray == null) {
            buildJSONArray();
        }
        return jsonArray;
    }

    public void buildJSONArray() {
        InputStream is = parent.getResources().openRawResource(R.raw.movies);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            this.jsonArray = new JSONArray(br.readLine());
        } catch (Exception e) {

        }
    }
}
