package com.pirdad.guestlogixtest.character;

public interface CharactersListView {

    void onDataLoaded();

    void showSoftError(String message);

    void showLoading();

    void dismissLoading();

    void setTitle(String title);
}
