package com.emadabel.moviesappdagger.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        ApiModule.class,
        DbModule.class,
        ViewModelModule.class,
        ActivityModule.class,
        AndroidSupportInjectionModule.class})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(AppController appController);
}