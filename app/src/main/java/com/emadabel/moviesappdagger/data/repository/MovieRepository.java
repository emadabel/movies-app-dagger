package com.emadabel.moviesappdagger.data.repository;

import android.support.annotation.NonNull;

import com.emadabel.moviesappdagger.data.NetworkBoundResource;
import com.emadabel.moviesappdagger.data.Resource;
import com.emadabel.moviesappdagger.data.local.MovieDao;
import com.emadabel.moviesappdagger.data.local.MovieEntity;
import com.emadabel.moviesappdagger.data.remote.MovieApiResponse;
import com.emadabel.moviesappdagger.data.remote.MovieApiService;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Singleton
public class MovieRepository {

    private MovieDao mMovieDao;
    private MovieApiService mMovieApiService;

    public MovieRepository(MovieDao movieDao, MovieApiService movieApiService) {
        mMovieDao = movieDao;
        mMovieApiService = movieApiService;
    }

    public Observable<Resource<List<MovieEntity>>> loadMoviesByType() {
        return new NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull MovieApiResponse item) {
                mMovieDao.insertMovies(item.getResults());
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @Override
            protected Flowable<List<MovieEntity>> loadFromDb() {
                List<MovieEntity> movieEntities = mMovieDao.getMoviesByPage();
                if (movieEntities == null || movieEntities.isEmpty()) {
                    return Flowable.empty();
                }
                return Flowable.just(movieEntities);
            }

            @Override
            protected Observable<Resource<MovieApiResponse>> createCall() {
                return mMovieApiService.fetchMovies()
                        .flatMap(movieApiResponse -> Observable.just(movieApiResponse == null
                                ? Resource.error("", new MovieApiResponse())
                                : Resource.success(movieApiResponse)));
            }
        }.getAsObservable();
    }
}
