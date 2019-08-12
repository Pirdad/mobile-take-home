package com.pirdad.guestlogixservice;

import com.pirdad.guestlogixservice.domain.Episode;
import com.pirdad.guestlogixservice.network.GetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EpisodesRequest extends GetRequest<List<Episode>> {

    private static final String URL = "https://rickandmortyapi.com/api/episode";

    public EpisodesRequest(int page) {
        setUrl(URL + "?page=" + page);
    }

    @Override
    public List<Episode> parse(String response) {
        List<Episode> episodes = null;
        try {
            JSONObject json = new JSONObject(response);
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject resultItem =  results.getJSONObject(i);
                Episode episode = parseItem(resultItem);
                if (episodes == null && episode != null) {
                    episodes = new ArrayList<>();
                }
                if (episode != null) {
                    episodes.add(episode);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return episodes;
    }

    private Episode parseItem(JSONObject resultItem) {
        if (resultItem == null) {
            return null;
        }
        Episode episode = new Episode();
        try {
            episode.setId(resultItem.getInt("id"));
            episode.setName(resultItem.getString("name"));
            episode.setAirDate(resultItem.getString("air_date"));
            episode.setCreated(resultItem.getString("created"));
            episode.setEpisode(resultItem.getString("episode"));
        } catch (JSONException e) {
            return null;
        }

        try {
            episode.setUrl(resultItem.getString("url"));
            episode.setCharacters(parseCharacters(resultItem.getJSONArray("characters")));
        } catch (JSONException e) { }

        return episode;
    }

    private String[] parseCharacters(JSONArray charactersJson) {
        if (charactersJson == null) {
            return null;
        }
        String[] characters = new String[charactersJson.length()];
        for (int i = 0; i < charactersJson.length(); i++) {
            try {
                String character = charactersJson.getString(i);
            } catch (JSONException e) {}
        }
        return characters;
    }
}
