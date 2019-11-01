package com.emadabel.moviesappdagger;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.emadabel.moviesappdagger.adapters.MoviesAdapter;
import com.emadabel.moviesappdagger.data.Resource;
import com.emadabel.moviesappdagger.data.local.MovieEntity;
import com.emadabel.moviesappdagger.model.Movie;
import com.emadabel.moviesappdagger.ui.MovieListViewModel;
import com.emadabel.moviesappdagger.ui.ViewModelFactory;
import com.emadabel.moviesappdagger.utils.NetworkUtils;
import com.emadabel.moviesappdagger.utils.TmdbJsonUtils;
import com.emadabel.moviesappdagger.utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    private static final String SPINNER_STATE_KEY = "spinner_state_key";
    private static final String MOVIES_LIST_KEY = "movies_list_key";

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.movies_list_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.error_message_tv)
    TextView mErrorMessageTv;
    @BindView(R.id.loading_indicator_pb)
    ProgressBar mLoadingIndicatorPb;

    private MovieListViewModel movieListViewModel;

    private MoviesAdapter moviesAdapter;

    private int mSpinnerState = -1;

    private ArrayList<Movie> moviesList;

    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        moviesAdapter = new MoviesAdapter();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(moviesAdapter);

        if (savedInstanceState != null) {
            moviesList = savedInstanceState.getParcelableArrayList(MOVIES_LIST_KEY);
            mSpinnerState = savedInstanceState.getInt(SPINNER_STATE_KEY);
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
        //getSupportLoaderManager().initLoader(TMDB_LOADER_ID, null, this);
    }

    private void handleErrorResponse() {

    }

    private void updateMoviesList(List<MovieEntity> movies) {

    }

    private void displayLoader() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (moviesList != null) {
            outState.putParcelableArrayList(MOVIES_LIST_KEY, moviesList);
        }
        outState.putInt(SPINNER_STATE_KEY, mSpinnerState);
    }

    /**
     * @param menu The options menu in which you place your items
     * @return boolean
     * reference: https://stackoverflow.com/questions/37250397/how-to-add-a-spinner-next-to-a-menu-in-the-toolbar
     * reference: https://developer.android.com/guide/topics/ui/controls/spinner.html
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        mSpinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);

        updateSpinner();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String value = "";

                //Clear movies list to retrieve the new list data
                if (pos != mSpinnerState) {
                    moviesList = null;
                    mSpinnerState = pos;
                }

                if (pos == 0) {
                    value = getString(R.string.pref_sort_popular);
                }

                if (pos == 1) {
                    value = getString(R.string.pref_sort_top_rated);
                }

                if (pos == 2) {
                    // query
                    //getSupportLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, MainActivity.this);
                    return;
                }

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.pref_sort_key), value);
                editor.apply();
                //getSupportLoaderManager().restartLoader(TMDB_LOADER_ID, null, MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }

    private void updateSpinner() {
        if (mSpinner != null) {
            String sortType = getSortType();

            if (sortType.equals(getString(R.string.pref_sort_popular))) {
                mSpinner.setSelection(0);
            }

            if (sortType.equals(getString(R.string.pref_sort_top_rated))) {
                mSpinner.setSelection(1);
            }

            if (mSpinnerState != -1) {
                mSpinner.setSelection(mSpinnerState);
            }
        }
    }

    private String getSortType() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String keyForSort = getString(R.string.pref_sort_key);
        String defaultSortType = getString(R.string.pref_sort_popular);
        return prefs.getString(keyForSort, defaultSortType);
    }

    private void showMoviesData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTv.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(boolean isFavoriteError) {
        if (!isFavoriteError) {
            mErrorMessageTv.setText(R.string.error_message);
        } else {
            mErrorMessageTv.setText(R.string.favorites_error_message);
        }
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

//    @Override
//    public void onClick(int movieId) {
//        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//        intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movieId);
//        startActivity(intent);
//    }
}
