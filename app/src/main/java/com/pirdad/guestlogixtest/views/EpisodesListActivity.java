package com.pirdad.guestlogixtest.views;

import com.pirdad.guestlogixtest.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pirdad.guestlogixtest.episode.EpisodeView;
import com.pirdad.guestlogixtest.episode.EpisodesPresenter;
import com.pirdad.guestlogixtest.episode.EpisodesView;
import com.pirdad.guestlogixtest.helpers.ListSpacingDecoration;

public class EpisodesActivity extends Activity implements EpisodesView {

    private RecyclerView recycler;
    private EpisodesPresenter presenter;
    private Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        init();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (presenter != null) {
            presenter.onLoad();
        }
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.setView(null);
        }
        super.onDestroy();
    }

    private void init() {
        presenter = new EpisodesPresenter();
        presenter.setView(this);

        adapter = new Adapter();

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new ListSpacingDecoration(this, R.dimen.StandardMediumMargin));
        recycler.setAdapter(adapter);
    }

    @Override
    public void onDataLoaded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private class Adapter extends RecyclerView.Adapter<EpisodeVH> {

        @NonNull
        @Override
        public EpisodeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EpisodeVH(getLayoutInflater().inflate(R.layout.item_episode, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull EpisodeVH holder, int position) {
            presenter.onLoadEpisodeAt(position, holder);
        }

        @Override
        public int getItemCount() {
            return (presenter != null) ? presenter.getEpisodeCount() : 0;
        }
    }

    private class EpisodeVH extends RecyclerView.ViewHolder implements EpisodeView {

        private TextView title;
        private TextView airDate;

        public EpisodeVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            airDate = itemView.findViewById(R.id.airdate);
        }

        @Override
        public void setEpisode(String episode) {
            this.title.setText(episode);
        }

        @Override
        public void setTitle(String title) {
            String currText = this.title.getText().toString();
            if (!TextUtils.isEmpty(currText) && !TextUtils.isEmpty(title)) {
                title = currText + " : " + title;
            }
            this.title.setText(title);
        }

        @Override
        public void setAirDate(String airDate) {
            this.airDate.setText(airDate);
        }
    }
}
