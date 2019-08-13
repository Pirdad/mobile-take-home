package com.pirdad.guestlogixtest.views;

import android.app.Activity;

import com.pirdad.guestlogixtest.RepositoryProvider;
import com.pirdad.guestlogixtest.TakeHomeTestApplication;

public abstract class BaseActivity extends Activity {

    public RepositoryProvider getRepositoryProvider() {
        return ((TakeHomeTestApplication) getApplication()).getRepositoryProvider();
    }
}
