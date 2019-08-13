package com.pirdad.guestlogixtest.repositories;

import com.pirdad.guestlogixservice.domain.Episode;
import com.pirdad.guestlogixtest.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DefaultEpisodeRepository implements Repository<Episode> {

    private final Collection<Episode> episodeArrayList = new LinkedHashSet<>();

    public DefaultEpisodeRepository() { }

    @Override
    public void add(Episode item) {
        synchronized (episodeArrayList) {
            episodeArrayList.add(item);
        }
    }

    @Override
    public void update(Episode item) { }

    @Override
    public void delete(Episode item) {
        synchronized (episodeArrayList) {
            episodeArrayList.remove(item);
        }
    }

    @Override
    public void delete(long id) {
        synchronized (episodeArrayList) {
            while (episodeArrayList.iterator().hasNext()) {
                Episode item =  episodeArrayList.iterator().next();
                if (item.getId() == id) {
                    episodeArrayList.remove(item);
                    return;
                }
            }
        }
    }

    @Override
    public Collection<Episode> getAll() {
        // todo: load from server
        return episodeArrayList;
    }

    @Override
    public Episode get(long id) {
        synchronized (episodeArrayList) {
            for (Episode episode : episodeArrayList) {
                if (episode.getId() == id) {
                    return episode;
                }
            }
        }
        // todo: load from backend and store
        return null;
    }
}
