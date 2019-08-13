package com.pirdad.guestlogixtest.episode;

public interface EpisodesListView {

    void onDataLoaded();

    void showSoftError(String message);

    void showLoading();

    void dismissLoading();
}
