package com.pirdad.guestlogixtest.character;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixservice.domain.Location;
import com.pirdad.guestlogixtest.Repository;

public class CharacterDetailPresenter {

    // view
    private CharacterView view;
    // model
    private Repository<Character> repository;

    private long id;
    private boolean loading;

    public void setView(CharacterView view) {
        this.view = view;
    }

    public void setRepository(Repository<Character> repository) {
        this.repository = repository;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void onLoad() {
        if (repository == null) {
            throw new IllegalStateException("The repository must be set for CharacterDetailPresenter.");
        }
        if (loading) {
            return;
        }
        loading = true;
        new Thread(runLoad).start();
    }

    private Runnable runLoad = new Runnable() {
        @Override
        public void run() {
            Character character = repository.get(id);
            if (view != null && character != null) {
                updateView(character);
            } else if (view != null) {
                view.showSoftError("Could not load this character at the moment. Try again later.");
                view.dismiss();
            }
            loading = false;
        }
    };

    private void updateView(Character character) {
        view.setPicture(character.getImage());
        view.setName(character.getName());
        view.setGender(getValueOrUnknown(character.getGender()));
        view.setStatus(getValueOrUnknown(character.getStatus()));
        view.setSpecies(getValueOrUnknown(character.getSpecies()));
        view.setType(getValueOrUnknown(character.getType()));
        view.setOrigin(getLocationOrUnknown(character.getOrigin()));
        view.setLocation(getLocationOrUnknown(character.getLocation()));
    }

    private String getLocationOrUnknown(Location location) {
        return location != null && location.getName() != null && !location.getName().isEmpty() ? location.getName() : "Unknown";
    }

    private String getValueOrUnknown(String value) {
        return value == null || value.isEmpty() ? "Unknown" : value;
    }
}