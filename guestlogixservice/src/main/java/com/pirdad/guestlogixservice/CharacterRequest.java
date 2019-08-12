package com.pirdad.guestlogixservice;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixservice.domain.Location;
import com.pirdad.guestlogixservice.network.GetRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CharacterRequest extends GetRequest<Character> {

    public CharacterRequest(String url) {
        setUrl(url);
    }

    @Override
    public Character parse(String response) {
        Character character = new Character();
        JSONObject json = null;
        try {
            json = new JSONObject(response);
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
            parseLocation(json.getJSONObject("origin"));
            parseLocation(json.getJSONObject("location"));
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
