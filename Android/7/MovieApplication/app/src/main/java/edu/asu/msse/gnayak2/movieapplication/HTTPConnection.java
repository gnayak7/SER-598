package edu.asu.msse.gnayak2.movieapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
 * Purpose: Makes a HTTP connection to the server.
 * <p>
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */
public class HTTPConnection {
    String urlString;
    HttpURLConnection httpConnection;
    private BufferedReader bufferedReader;

    /**
     * Constructor
     *
     * @params urlString. The url string which should be called.
     * @returns An object.
     */
    HTTPConnection(String urlString) {
        this.urlString = urlString;
    }

    /**
     * Method which makes a HTP Connection
     *
     * @returns Returns String response.
     */
    public String makeNetworkCall() {
        try {
            URL url = new URL(this.urlString);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.connect();

            int statusCode = httpConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Unknown Response Code " + statusCode);
            }
            InputStream inputStream = httpConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer stringBuffer = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
