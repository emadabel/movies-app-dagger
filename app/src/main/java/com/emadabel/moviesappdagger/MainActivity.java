package com.emadabel.moviesappdagger;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emadabel.moviesappdagger.adapters.MoviesAdapter;
import com.emadabel.moviesappdagger.data.local.MovieEntity;
import com.emadabel.moviesappdagger.ui.MovieListViewModel;
import com.emadabel.moviesappdagger.ui.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.movies_list_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.error_message_tv)
    TextView mErrorMessageTv;
    @BindView(R.id.loading_indicator_pb)
    ProgressBar mLoadingIndicatorPb;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    private MovieListViewModel movieListViewModel;

    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        initialiseView();
        initialiseViewModel();
    }

    private void initialiseViewModel() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        movieListViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel.class);
        movieListViewModel.getMoviesLiveData().observe(this, resource -> {
            if (resource.isLoading()) {
                displayLoader();
            } else if (!resource.mData.isEmpty()) {
                updateMoviesList(resource.mData);
            } else handleErrorResponse();
        });

        movieListViewModel.loadMoreMovies();
    }

    private void initialiseView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        moviesAdapter = new MoviesAdapter();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(moviesAdapter);
    }

    private void handleErrorResponse() {
        hideLoader();
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessageTv.setText(R.string.error_message);
        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

    private void updateMoviesList(List<MovieEntity> movies) {
        hideLoader();
        mRecyclerView.setVisibility(View.VISIBLE);
        moviesAdapter.setMoviesData(movies);
    }

    private void hideLoader() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicatorPb.setVisibility(View.GONE);
    }

    private void displayLoader() {
        mLoadingIndicatorPb.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }
}
