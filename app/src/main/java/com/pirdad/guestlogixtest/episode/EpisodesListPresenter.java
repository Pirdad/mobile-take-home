package com.pirdad.guestlogixtest.episode;

import com.pirdad.guestlogixservice.EpisodesRequest;
import com.pirdad.guestlogixservice.domain.Episode;
import com.pirdad.guestlogixtest.Repository;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class EpisodesListPresenter {

    // view
    private EpisodesListView view;
    // navigation
    private EpisodeToCharactersNavigation navigateToCharacters;
    // model
    private Repository<Episode> repository;
    private final List<Episode> episodes = new ArrayList<>();

    private int currPage = 0;
    private boolean lastPageReached = false;
    private boolean isLoading = false;

    public void setView(EpisodesListView view) {
        this.view = view;
    }

    public void setNavigationToCharacters(EpisodeToCharactersNavigation navToCharacters) {
        this.navigateToCharacters = navToCharacters;
    }

    public void setRepository(Repository<Episode> repository) {
        this.repository = repository;
    }

    public void onLoad() {
        if (repository == null) {
            throw new IllegalStateException("Repository must be set for EpisodeListPresenter.");
        }
        lastPageReached = false;
        currPage = 1;
        isLoading = true;
        if (view != null) {
            view.showLoading();
        }
        if (episodes.isEmpty()) {
            prefill();
        }
        new Thread(runLoad).start();
    }

    private void prefill() {
        episodes.addAll(repository.getAll());
        if (!episodes.isEmpty() && view != null) {
            view.onDataLoaded();
            view.dismissLoading();
            System.out.println("data pre-filled with " + episodes.size() + " items");
        }
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
                for (Episode episode : episodes) {
                    repository.add(episode);
                }
                synchronized (EpisodesListPresenter.this.episodes) {
                    EpisodesListPresenter.this.episodes.clear();
                    EpisodesListPresenter.this.episodes.addAll(episodes);
                    if (view != null) {
                        System.out.println(episodes.size() + " items loaded");
                        view.onDataLoaded();
                        view.dismissLoading();
                    }
                    isLoading = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                isLoading = false;
                String errorMessage = "And error occurred. Pleas try again later.";
                if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                    errorMessage = "Please check your network connection and try again.";
                }
                if (view != null) {
                    view.dismissLoading();
                    view.showSoftError(errorMessage);
                }
            }
        }
    };

    private final Runnable runLoadMore = new Runnable() {
        @Override
        public void run() {
            try {
                List<Episode> episodes = new EpisodesRequest(currPage+1).execute();
                for (Episode episode : episodes) {
                    repository.add(episode);
                }
                synchronized (EpisodesListPresenter.this.episodes) {
                    EpisodesListPresenter.this.episodes.addAll(episodes);
                    if (view != null) {
                        System.out.println(episodes.size() + " more items loaded");
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

    public void onEpisodeClicked(int index) {
        if (navigateToCharacters == null) {
            throw new IllegalStateException("It's required to EpisodeToCharactersNavigation handler.");
        }
        if (index < 0 || index >= episodes.size()) {
            view.showSoftError("Can't show episode detail at the moment. Please try again later.");
            return;
        }
        navigateToCharacters.setEpisodeId(episodes.get(index).getId());
        navigateToCharacters.execute();
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
