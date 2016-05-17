package edu.asu.msse.gnayak2.movieapplication;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
 * Purpose: This is an adapter for Recycler View.
 *
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieViewHolder> {
    private ArrayList<MovieCell> arrayListMovieCell;

    /*
        This is a custom onChildChildListener.
     */
    private ViewClickMethodDelegate viewClickMethodDelegate;

    public RecyclerViewAdapter(ArrayList<MovieCell> arrayListMovieCell, ViewClickMethodDelegate viewClickMethodDelegate) {
        this.arrayListMovieCell = arrayListMovieCell;
        this.viewClickMethodDelegate = viewClickMethodDelegate;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView movieCardView;
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieRating;
        ViewClickMethodDelegate viewClickMethodDelegate;
        int position;

        public MovieViewHolder(View view, ViewClickMethodDelegate viewClickMethodDelegate) {
            super(view);
            this.viewClickMethodDelegate = viewClickMethodDelegate;
            movieCardView = (CardView) view.findViewById(R.id.movie_card_view);
            movieTitle = (TextView) view.findViewById(R.id.movie_title);
            moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
            movieRating = (TextView) view.findViewById(R.id.movie_rating);
            moviePoster.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ImageView) {
                viewClickMethodDelegate.posterClicked(position);
            } else {
                viewClickMethodDelegate.movieDescriptionClicked(position);
            }
        }
    }

    public interface ViewClickMethodDelegate {
        void posterClicked(int position);
        void movieDescriptionClicked(int position);
    }

    // Called by Adapter
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View movieCellLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cell_layour, parent, false);
        RecyclerViewAdapter.MovieViewHolder viewHolder = new MovieViewHolder(movieCellLayout, this.viewClickMethodDelegate);
        return viewHolder;
    }

    // Invoked by Layout Manager
    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.position = position;
        holder.moviePoster.setImageResource(R.drawable.movieicon);
        holder.movieTitle.setText(arrayListMovieCell.get(position).getTitle());
        holder.movieRating.setText(arrayListMovieCell.get(position).getRating());
    }

    public int getItemCount() {
        return this.arrayListMovieCell.size();
    }
}
