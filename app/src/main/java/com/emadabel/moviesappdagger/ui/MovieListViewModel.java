package com.emadabel.moviesappdagger.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.emadabel.moviesappdagger.data.Resource;
import com.emadabel.moviesappdagger.data.local.MovieDao;
import com.emadabel.moviesappdagger.data.local.MovieEntity;
import com.emadabel.moviesappdagger.data.remote.MovieApiService;
import com.emadabel.moviesappdagger.data.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

public class MovieListViewModel extends ViewModel {

    private MovieRepository mMovieRepository;
    private MutableLiveData<Resource<List<MovieEntity>>> moviesLiveData = new MutableLiveData<>();

    @Inject
    public MovieListViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        mMovieRepository = new MovieRepository(movieDao, movieApiService);
    }

    public void loadMoreMovies() {
        mMovieRepository.loadMoviesByType()
                .subscribe(resource -> getMoviesLiveData().postValue(resource));
    }

    public MutableLiveData<Resource<List<MovieEntity>>> getMoviesLiveData() {
        return moviesLiveData;
    }
}
