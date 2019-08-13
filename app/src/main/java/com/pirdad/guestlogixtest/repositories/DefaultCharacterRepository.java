package com.pirdad.guestlogixtest.repositories;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixservice.domain.Episode;
import com.pirdad.guestlogixtest.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DefaultCharacterRepository implements Repository<Character> {

    private final Collection<Character> characters = new LinkedHashSet<>();

    public DefaultCharacterRepository() { }

    @Override
    public void add(Character item) {
        synchronized (characters) {
            characters.add(item);
        }
    }

    @Override
    public void update(Character item) { }

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
}
