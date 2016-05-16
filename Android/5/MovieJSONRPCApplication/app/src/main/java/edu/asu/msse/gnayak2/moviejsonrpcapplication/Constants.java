package edu.asu.msse.gnayak2.moviejsonrpcapplication;

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


// This is file which holds static constants values.
public class Constants {
    public static final String ADD_MOVIE = "add";
    public static final String UPDATE_MOVIE = "update";
    public static final String DELETE_MOVIE = "remove";
    public static final String GET_MODEL_INFORMATION = "getModelInformation";
    public static final String GET_MOVIE = "get";
    public static final String DELETE_AND_ADD = "deleteAndAdd";
    public static final String REFRESH = "resetFromJsonFile";
    public static final int EDIT_OR_DELETE = 1;
    public static final int NEW_MOVIE = 2;
    public static final int MOVIE_DELETED = 3;
}
