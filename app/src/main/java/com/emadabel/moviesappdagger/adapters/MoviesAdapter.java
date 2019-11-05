package com.emadabel.moviesappdagger.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.emadabel.moviesappdagger.R;
import com.emadabel.moviesappdagger.data.local.MovieEntity;
import com.emadabel.moviesappdagger.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<MovieEntity> mMovieList;

    public MoviesAdapter() {
        mMovieList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.movies_list, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder moviesViewHolder, int position) {
        moviesViewHolder.bindTo(getItem(position));
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) return 0;
        return mMovieList.size();
    }

    public void setMoviesData(List<MovieEntity> movieList) {
        int startPosition = mMovieList.size();
        mMovieList.addAll(movieList);
        notifyItemRangeChanged(startPosition, movieList.size());
    }

    public MovieEntity getItem(int position) {
        return mMovieList.get(position);
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        final ImageView mMovieImageView;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.movie_poster_iv);
        }

        public void bindTo(MovieEntity movie) {
            String posterUrl = NetworkUtils.buildPosterUrl(
                    movie.getPosterPath());

            Picasso.get().load(posterUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(mMovieImageView);
        }
    }
}
