package com.pirdad.guestlogixtest;

import android.app.Application;

import com.pirdad.guestlogixtest.providers.DefaultRepositoryProvider;

public class TakeHomeTestApplication extends Application {

    private RepositoryProvider repositoryProvider;

    public RepositoryProvider getRepositoryProvider() {
        return repositoryProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        repositoryProvider = new DefaultRepositoryProvider();
    }
}
