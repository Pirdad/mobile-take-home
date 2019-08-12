package com.pirdad.guestlogixtest.character;

import com.pirdad.guestlogixservice.CharactersRequest;
import com.pirdad.guestlogixservice.domain.Character;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharactersListPresenter {

    // view
    private CharactersListView view;
    // navigation
    private CharacterToDetailNavigation navigateToDetail;
    // model
    private final List<Character> characters = new ArrayList<>();

    private String title;
    private long[] characterIds;
    private boolean isLoading;

    public void setView(CharactersListView view) {
        this.view = view;
    }

    public void setCharacterToDetailNavigationHandler(CharacterToDetailNavigation characterToDetailNavigation) {
        this.navigateToDetail = characterToDetailNavigation;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCharacterIds(long[] characterIds) {
        this.characterIds = characterIds;
    }

    public void onLoad() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        new Thread(runLoad).start();
    }

    private final Runnable runLoad = new Runnable() {
        @Override
        public void run() {
            try {
                List<Character> characters = new CharactersRequest(characterIds).execute();
                synchronized (CharactersListPresenter.this.characters) {
                    CharactersListPresenter.this.characters.clear();
                    CharactersListPresenter.this.characters.addAll(characters);
                    if (view != null) {
                        view.onDataLoaded();
                    }
                    isLoading = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public int getCharacterCount() {
        return characters.size();
    }

    public void onLoadCharacterAt(int index, CharacterView view) {
        if (index < 0 || index >= characters.size()) {
            return;
        }
        if (view != null) {
            view.setPicture(characters.get(index).getImage());
            view.setName(characters.get(index).getName());
            view.setStatus(getValueOrUnknown(characters.get(index).getStatus()));
            view.setGender(getValueOrUnknown(characters.get(index).getGender()));
        }
    }

    private String getValueOrUnknown(String value) {
        return value == null || value.isEmpty() ? "Unknown" : value;
    }

    public void onCharacterClicked(int index) {

    }
}
