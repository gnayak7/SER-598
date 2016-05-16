package edu.asu.msse.gnayak2.moviejsonrpcapplication;

import android.os.AsyncTask;
import android.os.Looper;
import org.json.JSONArray;
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


// This is a class which extends AsyncTask to make an asynchronous HTTP call.
public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {

    private AsyncResponse delegate;

    public interface AsyncResponse {
        void doSomething(MethodInformation result);
    }

    AsyncCollectionConnect(AsyncResponse parent) {
        this.delegate = parent;
    }

    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = buildMethod(ja, aRequest);
            JSONRPCRequestViaHTTP conn = new JSONRPCRequestViaHTTP((new URL(aRequest[0].urlString))/*, aRequest[0].parent*/);
            String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = resultStr;
        }catch (Exception ex){
        }
        return aRequest[0];
    }

    public String buildMethod(JSONArray ja, MethodInformation... aRequest) {
        String request = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+",\"id\":3}";
        return request;
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        delegate.doSomething(res);
    }
}

