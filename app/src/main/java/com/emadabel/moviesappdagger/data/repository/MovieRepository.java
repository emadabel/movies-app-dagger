package com.emadabel.moviesappdagger.data.repository;

import com.emadabel.moviesappdagger.data.remote.MovieApiService;
import com.emadabel.moviesappdagger.data.local.MovieDao;
import com.emadabel.moviesappdagger.data.local.MovieEntity;

import java.util.List;

public class MovieRepository {

    private MovieDao mMovieDao;
    private MovieApiService mMovieApiService;

    public MovieRepository(MovieDao movieDao, MovieApiService movieApiService) {
        mMovieDao = movieDao;
        mMovieApiService = movieApiService;
    }

    public List<MovieEntity> loadMoviesByType() {
        return null;
    }
}
