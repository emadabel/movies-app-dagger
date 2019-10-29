package com.emadabel.moviesappdagger.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.emadabel.moviesappdagger.ui.MovieListViewModel;
import com.emadabel.moviesappdagger.ui.ViewModelFactory;
import com.emadabel.moviesappdagger.ui.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel.class)
    protected abstract ViewModel movieListViewModel(MovieListViewModel movieListViewModel);
}
