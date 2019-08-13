package com.pirdad.guestlogixtest.character;

import com.pirdad.guestlogixservice.CharactersRequest;
import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixservice.domain.Episode;
import com.pirdad.guestlogixtest.Repository;
import com.pirdad.guestlogixtest.episode.EpisodesListPresenter;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CharactersListPresenter {

    private static final String SORT_KEY_ALIVE = "alive";
    private static final String SORT_KEY_DEAD = "dead";
    private static final String SORT_KEY_UNKNOWN = "unknown";

    public static final int ITEM_TYPE_HEADING = 0;
    public static final int ITEM_TYPE_CHARACTER = 1;

    // view
    private CharactersListView view;
    // navigation
    private CharacterToDetailNavigation navigateToDetail;
    // model
    private Repository<Character> characterRepository;
    private Repository<Episode> episodeRepository;
    private final List<Object> characters = new ArrayList<>();

    private long episodeId;
    private boolean isLoading;

    public void setView(CharactersListView view) {
        this.view = view;
    }

    public void setCharacterRepository(Repository<Character> characterRepository) {
        this.characterRepository = characterRepository;
    }

    public void setEpisodeRepository(Repository<Episode> episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    public void setCharacterToDetailNavigationHandler(CharacterToDetailNavigation characterToDetailNavigation) {
        this.navigateToDetail = characterToDetailNavigation;
    }

    public void onLoad() {
        if (episodeRepository == null) {
            throw new IllegalStateException("Episode Repository must be set for CharactersListPresenter.");
        }
        if (characterRepository == null) {
            throw new IllegalStateException("Character Repository must be set for CharactersListPresenter.");
        }
        if (isLoading) {
            return;
        }
        isLoading = true;
        if (view != null) {
            view.showLoading();
        }
        new Thread(runLoad).start();
    }

    private final Runnable runLoad = new Runnable() {
        @Override
        public void run() {
            try {
                Episode episode = episodeRepository.get(episodeId);
                setTitle(episode);
                List<Character> characters = new CharactersRequest(parseCharacterIds(episode.getCharacters())).execute();
                addCharactersToRepository(characters);
                synchronized (CharactersListPresenter.this.characters) {
                    CharactersListPresenter.this.characters.clear();
                    CharactersListPresenter.this.characters.addAll(characters);
                    sortAndHeaders(CharactersListPresenter.this.characters);
                    if (view != null) {
                        view.onDataLoaded();
                        view.dismissLoading();
                    }
                    isLoading = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                isLoading = false;
                showExceptionError(e);
            }
        }
    };

    private void sortAndHeaders(List<Object> characters) {
        characters.add(6, "Unknown");
        characters.add("Alive");
        characters.add(0, "Dead");
        Collections.sort(characters, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Character && o2 instanceof Character) {
                    return sortBothCharacters((Character) o1, (Character) o2);
                } else if (o1 instanceof String && o2 instanceof String) {
                    return sortBothHeaders((String) o1, (String) o2);
                } else {
                    return sortDifferentTypes(o1, o2);
                }
            }
        });
    }

    private int sortBothCharacters(Character ch1, Character ch2) {
        if (ch1.getStatus().toLowerCase().equalsIgnoreCase(ch2.getStatus().toLowerCase())) {
            // both same status
            return ch1.getName().compareTo(ch2.getName());
        } else {
            return ch1.getStatus().toLowerCase().compareTo(ch2.getStatus().toLowerCase());
        }
    }

    private int sortBothHeaders(String h1, String h2) {
        return h1.toLowerCase().compareTo(h2.toLowerCase());
    }

    private int sortDifferentTypes(Object o1, Object o2) {
        if (o1 instanceof String) {
            // o1: String, o2: Character
            String hd = (String) o1;
            Character ch = (Character) o2;
            int result = hd.toLowerCase().compareTo(ch.getStatus().toLowerCase());
            return (result == 0) ? -1 : result;
        } else {
            // o1: Character, o2: String
            Character ch = (Character) o1;
            String hd = (String) o2;
            int result = ch.getStatus().toLowerCase().compareTo(hd.toLowerCase());
            return (result == 0) ? 1 : result;
        }
    }

    private void showExceptionError(IOException e) {
        String errorMessage = "And error occurred. Pleas try again later.";
        if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            errorMessage = "Please check your network connection and try again.";
        }
        if (view != null) {
            view.dismissLoading();
            view.showSoftError(errorMessage);
        }
    }

    private void addCharactersToRepository(List<Character> characters) {
        for (Character character : characters) {
            characterRepository.add(character);
        }
    }

    private void setTitle(Episode episode) {
        if (view != null) {
            view.setTitle(episode.getEpisode() + " : " + episode.getName());
        }
    }

    private long[] parseCharacterIds(String[] characters) {
        List<Long> idsList = new ArrayList<>();
        String characterSubStr = "character/";
        for (String character : characters) {
            if (character == null) {
                continue;
            }
            String idSubstring = null;
            try {
                idSubstring = character.substring(character.lastIndexOf(characterSubStr) + characterSubStr.length());
            } catch (StringIndexOutOfBoundsException e) {}
            //System.out.println("idSubstring: " + idSubstring);
            Long id = null;
            if (idSubstring != null && !idSubstring.isEmpty()) {
                try {
                    id = Long.parseLong(idSubstring);
                } catch (NumberFormatException e) {}
            }
            if (id != null) {
                idsList.add(id);
            }
        }
        long[] ids = new long[idsList.size()];
        for (int i = 0; i < idsList.size(); i++) {
            Long id = idsList.get(i);
            ids[i] = id;
        }
        return ids;
    }

    public int getCharacterCount() {
        return characters.size();
    }

    public int getItemTypeAt(int index) {
        if (index < 0 || index >= characters.size()) {
            return -1;
        }
        Object item = characters.get(index);
        return (item instanceof String) ? ITEM_TYPE_HEADING : ITEM_TYPE_CHARACTER;
    }

    public void onLoadHeaderAt(int index, HeaderView view) {
        if (index < 0 || index >= characters.size()) {
            return;
        }
        Object item = characters.get(index);
        if (item instanceof Character) {
            return;
        }
        if (view != null) {
            view.setHeading((String) item);
        }
    }

    public void onLoadCharacterAt(int index, CharacterView view) {
        if (index < 0 || index >= characters.size()) {
            return;
        }
        Object item = characters.get(index);
        if (item instanceof String) {
            return;
        }
        if (view != null) {
            view.setPicture(((Character) item).getImage());
            view.setName(((Character) item).getName());
            view.setStatus(getValueOrUnknown(((Character) item).getStatus()));
            view.setGender(getValueOrUnknown(((Character) item).getGender()));
        }
    }

    private String getValueOrUnknown(String value) {
        return value == null || value.isEmpty() ? "Unknown" : value;
    }

    public void onCharacterClicked(int index) {
        if (navigateToDetail == null) {
            return;
        }
        if (index < 0 || index >= characters.size()) {
            view.showSoftError("Can't show character detail at the moment. Please try again later.");
            return;
        }
        Object item = characters.get(index);
        if (item instanceof String) {
            return;
        }
        navigateToDetail.setCharacterId(((Character) item).getId());
        navigateToDetail.execute();
    }

    public void setEpisodeId(long episodeId) {
        this.episodeId = episodeId;
    }
}
