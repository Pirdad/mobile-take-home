package com.pirdad.guestlogixservice.domain;

import java.util.Objects;

public class Episode {

    private long id;
    private String name;
    private String airDate;
    private String episode;
    private String[] characters;
    private String url;
    private String created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String[] getCharacters() {
        return characters;
    }

    public void setCharacters(String[] characters) {
        this.characters = characters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Episode)) {
            return false;
        }
        Episode episode = (Episode) o;
        return id == episode.id && Objects.equals(name, episode.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
