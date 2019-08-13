package com.pirdad.guestlogixtest.repositories;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixservice.domain.Episode;
import com.pirdad.guestlogixtest.Repository;
import com.pirdad.guestlogixtest.character.CharacterRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DefaultCharacterRepository implements CharacterRepository {

    private final Collection<Character> characters = new LinkedHashSet<>();
    private final Set<Character> killList = new HashSet<>();

    public DefaultCharacterRepository() { }

    @Override
    public void add(Character item) {
        synchronized (characters) {
            for (Character character : killList) {
                if (item.equals(character)) {
                    item.setStatus("Dead");
                    return;
                }
            }
            characters.add(item);
        }
    }

    @Override
    public void update(Character item) {
        synchronized (characters) {
            characters.add(item);
        }
    }

    @Override
    public void delete(Character item) {
        synchronized (characters) {
            characters.remove(item);
        }
    }

    @Override
    public void delete(long id) {
        synchronized (characters) {
            while (characters.iterator().hasNext()) {
                Character item =  characters.iterator().next();
                if (item.getId() == id) {
                    characters.remove(item);
                    return;
                }
            }
        }
    }

    @Override
    public Collection<Character> getAll() {
        // todo: load from server
        return characters;
    }

    @Override
    public Character get(long id) {
        synchronized (characters) {
            for (Character character : characters) {
                if (character.getId() == id) {
                    return character;
                }
            }
        }
        // todo: load from backend and store
        return null;
    }

    @Override
    public void addToKillList(Character character) {
        synchronized (killList) {
            killList.add(character);
        }
    }
}
