package edu.asu.msse.gnayak2.movieapplication;

import android.util.Log;

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
 * Purpose:This class is a wrapper for async task
 *
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version April 2016
 */

// This is a wrapper to make a JSON RPC request.
public class RPCRequestAndResponse {

    public RPCResponseDelegate mainActivityDelegate = null;

    public interface RPCResponseDelegate {
        void processResult(MethodInformation result);
    }

    RPCRequestAndResponse(String IP_ADDRESS, final String method,String[] params, RPCResponseDelegate delegate) {
        this.mainActivityDelegate = delegate;
        MethodInformation methodInformation = new MethodInformation(IP_ADDRESS, method, params);
        AsyncCollectionConnect asyncCollectionConnect = (AsyncCollectionConnect) new AsyncCollectionConnect(new AsyncCollectionConnect.AsyncResponse() {
            @Override
            public void doSomething(MethodInformation result) {
                Log.w(method, "  is successful.");
                mainActivityDelegate.processResult(result);
            }
        }).execute(methodInformation);
    }

    RPCRequestAndResponse(String IP_ADDRESS, final String method, RPCResponseDelegate delegate) {
        this.mainActivityDelegate = delegate;
        MethodInformation methodInformation = new MethodInformation(IP_ADDRESS, method, new String[] {});
        AsyncCollectionConnect asyncCollectionConnect = (AsyncCollectionConnect) new AsyncCollectionConnect(new AsyncCollectionConnect.AsyncResponse() {
            @Override
            public void doSomething(MethodInformation result) {
                Log.w(method, "  is successful.");
                mainActivityDelegate.processResult(result);
            }
        }).execute(methodInformation);
    }

    RPCRequestAndResponse(String IP_ADDRESS, final String method) {
        MethodInformation methodInformation = new MethodInformation(IP_ADDRESS, method, new String[]{});
        AsyncCollectionConnect asyncCollectionConnect = (AsyncCollectionConnect) new AsyncCollectionConnect(new AsyncCollectionConnect.AsyncResponse() {
            @Override
            public void doSomething(MethodInformation result) {
                Log.w(method, "  is successful.");
            }
        }).execute(methodInformation);
    }
    RPCRequestAndResponse(String IP_ADDRESS, final String method, String[] params) {
        MethodInformation methodInformation = new MethodInformation(IP_ADDRESS, method, params);
        AsyncCollectionConnect asyncCollectionConnect = (AsyncCollectionConnect) new AsyncCollectionConnect(new AsyncCollectionConnect.AsyncResponse() {
            @Override
            public void doSomething(MethodInformation result) {
                Log.w(method, "  is successful.");
            }
        }).execute(methodInformation);
    }
}
