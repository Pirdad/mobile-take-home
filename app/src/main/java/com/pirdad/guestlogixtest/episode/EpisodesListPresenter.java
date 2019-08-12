package com.pirdad.guestlogixtest.episode;


import com.pirdad.guestlogixservice.EpisodesRequest;
import com.pirdad.guestlogixservice.domain.Episode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EpisodesPresenter {

    // model
    private final List<Episode> episodes = new ArrayList<>();
    // view
    private EpisodesView view;

    public void setView(EpisodesView view) {
        this.view = view;
    }

    public void onLoad() {
        new Thread(runLoad).start();
    }

    private final Runnable runLoad = new Runnable() {
        @Override
        public void run() {
            try {
                List<Episode> episodes = new EpisodesRequest(1).execute();
                synchronized (EpisodesPresenter.this.episodes) {
                    EpisodesPresenter.this.episodes.clear();
                    EpisodesPresenter.this.episodes.addAll(episodes);
                    if (view != null) {
                        view.onDataLoaded();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Episode getTestEpisode(String title, String airdate) {
        Episode ep = new Episode();
        ep.setAirDate(airdate);
        ep.setName(title);
        return ep;
    }

    public void onEpisodeClicked() {

    }

    public void onLoadEpisodeAt(int index, EpisodeView view) {
        if (index < 0 || index >= episodes.size()) {
            return;
        }

        if (view != null) {
            view.setEpisode(episodes.get(index).getEpisode());
            view.setTitle(episodes.get(index).getName());
            view.setAirDate(episodes.get(index).getAirDate());
        }
    }

    public int getEpisodeCount() {
        return episodes.size();
    }
}
