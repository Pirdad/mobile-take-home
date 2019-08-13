package com.pirdad.guestlogixtest.repositories;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixtest.Repository;

import java.util.ArrayList;
import java.util.List;

public class DefaultCharacterRepository implements Repository<Character> {

    private final List<Character> characters = new ArrayList<>();

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
            for (int i = 0; i < characters.size(); i++) {
                Character character = characters.get(i);
                if (character.getId() == id) {
                    characters.remove(character);
                    return;
                }
            }
        }
    }

    @Override
    public List<Character> getAll() {
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
