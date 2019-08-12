package com.pirdad.guestlogixservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EpisodesModel {

    private void loadEpisode(int page) throws IOException, JSONException {
        //GetRequest request = new GetRequest("https://rickandmortyapi.com/api/episode?page=" + page, METHOD.GET);

        String url = "https://rickandmortyapi.com/api/episode";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        JSONObject json = new JSONObject(response.toString());
        //System.out.println(response.toString());
    }
}
