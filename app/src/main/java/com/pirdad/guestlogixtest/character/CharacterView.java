package com.pirdad.guestlogixtest.character;

public interface CharacterView {

    void setPicture(String imageUrl);

    void setName(String name);

    void setStatus(String status);

    void setGender(String gender);

    void setSpecies(String species);

    void setType(String type);

    void setOrigin(String origin);

    void setLocation(String origin);

    void showSoftError(String message);

    void dismiss();

    void showKillButton();

    void hideKillButton();
}
