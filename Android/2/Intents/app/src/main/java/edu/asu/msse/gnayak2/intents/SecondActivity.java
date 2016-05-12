package edu.asu.msse.gnayak2.intents;

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
 * Purpose: The purpose of this project is to demonstrate android
 * activity life cycle methods and to demonstrate the navigation between
 * two view using intents.
 *
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version January 15 2016
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

// Second activity which is displayed like an alert box.
// This class is called when the button is clicked on the main page.
// This class has an ok button which terminates the activity.
// This class overrides different activity life cycle methods and logs them on the console.
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.w(this.getClass().getSimpleName(), " called onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(this.getClass().getSimpleName(), " called onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(this.getClass().getSimpleName(), " called onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(this.getClass().getSimpleName(), " called onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(this.getClass().getSimpleName(), " called onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(this.getClass().getSimpleName(), " called onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(this.getClass().getSimpleName(), " called onDestroy");
    }

    public void onButtonClicked(View v) {
        Log.w(this.getClass().getSimpleName(), " called finish");
        this.finish();
    }
}
