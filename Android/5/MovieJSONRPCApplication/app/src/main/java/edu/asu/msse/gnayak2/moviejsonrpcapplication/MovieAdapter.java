package edu.asu.msse.gnayak2.moviejsonrpcapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

// Adapter for expandable list view
public class MovieAdapter extends BaseExpandableListAdapter  {
    private MainActivity parent;
    private MovieLibrary movieLibrary;
    private int lastIndex = -1;

    MovieAdapter(MainActivity parent, MovieLibrary movieLibrary) {
        this.parent = parent;
        this.movieLibrary = movieLibrary;
    }

    @Override
    public int getGroupCount() {
        return movieLibrary.getGenreCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String genre = movieLibrary.getGenreAt(groupPosition);
        return movieLibrary.getMoviesCount(genre);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return movieLibrary.getGenreAt(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return movieLibrary.getMovieNameAt(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String genre = (String) this.getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_genre, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.moive_genre_genre);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(genre);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String movie = (String) this.getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_title, null);
        }

        int id = R.drawable.defaultimage;

        if (movie.equals("Bourne")) {
            id = R.drawable.bourne;
        } else if (movie.equals("Hulk")) {
            id = R.drawable.hulk;
        } else if (movie.equals("Interview")) {
            id = R.drawable.interview;
        } else if (movie.equals("Minions")) {
            id = R.drawable.minions;
        } else if (movie.equals("Spectre")) {
            id = R.drawable.spectre;
        } else if (movie.equals("Deadpool")) {
            id = R.drawable.deadpool;
        } else if (movie.equals("Martian")) {
            id = R.drawable.martian;
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_drawable_image);
        imageView.setImageResource(id);

        TextView textViewTitle = (TextView) convertView.findViewById(R.id.movie_title_title);
        textViewTitle.setText(movie);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

