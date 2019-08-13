package com.pirdad.guestlogixtest.providers;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixservice.domain.Episode;
import com.pirdad.guestlogixtest.Repository;
import com.pirdad.guestlogixtest.RepositoryProvider;
import com.pirdad.guestlogixtest.repositories.DefaultCharacterRepository;
import com.pirdad.guestlogixtest.repositories.DefaultEpisodeRepository;

import java.util.HashMap;
import java.util.Map;

public class DefaultRepositoryProvider implements RepositoryProvider {

    private final Map<Class, Repository> repositories;

    public DefaultRepositoryProvider() {
        repositories = new HashMap<>();
        init();
    }

    private void init() {
        repositories.put(Character.class, new DefaultCharacterRepository());
        repositories.put(Episode.class, new DefaultEpisodeRepository());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Repository<T> getRepository(Class<T> cls) {
        return repositories.get(cls);
    }
}
