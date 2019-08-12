package com.pirdad.guestlogixtest.episode;


import com.pirdad.guestlogixservice.EpisodesRequest;
import com.pirdad.guestlogixservice.domain.Episode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EpisodesListPresenter {

    // model
    private final List<Episode> episodes = new ArrayList<>();
    // view
    private EpisodesListView view;

    private int currPage = 0;
    private boolean lastPageReached = false;
    private boolean isLoading = false;

    public void setView(EpisodesListView view) {
        this.view = view;
    }

    public void onLoad() {
        lastPageReached = false;
        currPage = 1;
        isLoading = true;
        new Thread(runLoad).start();
    }

    public void onLoadMore() {
        if (lastPageReached || isLoading) {
            return;
        }
        if (episodes.isEmpty()) {
            onLoad();
            return;
        }
        isLoading = true;
        new Thread(runLoadMore).start();
    }

    private final Runnable runLoad = new Runnable() {
        @Override
        public void run() {
            try {
                List<Episode> episodes = new EpisodesRequest(currPage).execute();
                synchronized (EpisodesListPresenter.this.episodes) {
                    EpisodesListPresenter.this.episodes.clear();
                    EpisodesListPresenter.this.episodes.addAll(episodes);
                    if (view != null) {
                        view.onDataLoaded();
                    }
                    isLoading = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private final Runnable runLoadMore = new Runnable() {
        @Override
        public void run() {
            try {
                List<Episode> episodes = new EpisodesRequest(currPage+1).execute();
                synchronized (EpisodesListPresenter.this.episodes) {
                    EpisodesListPresenter.this.episodes.addAll(episodes);
                    if (view != null) {
                        view.onDataLoaded();
                    }
                    isLoading = false;
                    if (episodes.isEmpty()) {
                        lastPageReached = true;
                    } else {
                        currPage++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLastPageReached() {
        return lastPageReached;
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
