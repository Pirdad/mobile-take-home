package com.pirdad.guestlogixservice;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixservice.domain.Location;
import com.pirdad.guestlogixservice.network.GetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CharactersRequest extends GetRequest<List<Character>> {

    private static final String URL = "https://rickandmortyapi.com/api/character";

    public CharactersRequest() {
        setUrl(URL);
    }

    public CharactersRequest(long[] ids) {
        setUrl(URL + getIdsEndpoint(ids));
    }

    private String getIdsEndpoint(long[] ids) {
        String result = "";
        if (ids != null && ids.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (long id : ids) {
                sb.append(String.valueOf(id)).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        if (!result.isEmpty()) {
            result = "/" + result;
        }
        return result;
    }

    @Override
    public List<Character> parse(String response) {
        List<Character> characters = null;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            try {
                JSONObject json = new JSONObject(response);
                jsonArray = json.getJSONArray("results");
            } catch (JSONException e1) { }
        }
        if (jsonArray != null) {
            characters = parse(jsonArray);
        }
        return characters;
    }

    private List<Character> parse(JSONArray json) {
        if (json == null) {
            return null;
        }
        List<Character> characters = null;
        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject resultItem =  json.getJSONObject(i);
                Character character = parseCharacter(resultItem);
                if (characters == null && character != null) {
                    characters = new ArrayList<>();
                }
                if (character != null) {
                    characters.add(character);
                }
            } catch (JSONException e) {}
        }
        return characters;
    }

    private Character parseCharacter(JSONObject json) {
        Character character = new Character();
        try {
            character.setId(json.getLong("id"));
            character.setName(json.getString("name"));
        } catch (JSONException e) {
            return null;
        }
        try {
            character.setStatus(json.getString("status"));
            character.setSpecies(json.getString("species"));
            character.setType(json.getString("type"));
            character.setGender(json.getString("gender"));
            character.setImage(json.getString("image"));
            character.setUrl(json.getString("url"));
            character.setCreated(json.getString("created"));
            character.setOrigin(parseLocation(json.getJSONObject("origin")));
            character.setLocation(parseLocation(json.getJSONObject("location")));
        } catch (JSONException e) {}
        return character;
    }

    private Location parseLocation(JSONObject json) {
        if (json == null) {
            return null;
        }
        Location location = new Location();
        try {
            location.setName(json.getString("name"));
        } catch (JSONException e) {
            return null;
        }
        try {
            location.setUrl(json.getString("url"));
        } catch (JSONException e) { }
        return location;
    }
}
