package com.pirdad.guestlogixtest.views;

import com.pirdad.guestlogixservice.domain.Episode;
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
import android.widget.Toast;

import com.pirdad.guestlogixtest.episode.EpisodeView;
import com.pirdad.guestlogixtest.episode.EpisodesListPresenter;
import com.pirdad.guestlogixtest.episode.EpisodesListView;
import com.pirdad.guestlogixtest.helpers.ListSpacingDecoration;
import com.pirdad.guestlogixtest.helpers.PaginationScrollListener;
import com.pirdad.guestlogixtest.navigation.EpisodeCharactersNavigationHandler;

public class EpisodesListActivity extends BaseActivity implements EpisodesListView {

    private EpisodesListPresenter presenter;
    private RecyclerView recycler;
    private View loader;
    private Adapter adapter;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        init();
    }

    private void init() {
        presenter = new EpisodesListPresenter();
        presenter.setView(this);
        presenter.setRepository(getRepositoryProvider().getRepository(Episode.class));
        presenter.setNavigationToCharacters(new EpisodeCharactersNavigationHandler(this));

        adapter = new Adapter();

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new ListSpacingDecoration(this, R.dimen.StandardMediumMargin));
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(paginationScrollListener);

        loader = findViewById(R.id.loader);
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

    @Override
    public void showSoftError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EpisodesListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void dismissLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.GONE);
            }
        });
    }

    private PaginationScrollListener paginationScrollListener = new PaginationScrollListener(layoutManager) {
        @Override
        protected void loadMoreItems() {
            if (presenter != null) {
                presenter.onLoadMore();
            }
        }

        @Override
        public boolean isLastPage() {
            return presenter != null && presenter.isLastPageReached();
        }

        @Override
        public boolean isLoading() {
            return presenter != null && presenter.isLoading();
        }
    };

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

    private class EpisodeVH extends RecyclerView.ViewHolder implements EpisodeView, View.OnClickListener {

        private TextView title;
        private TextView airDate;

        public EpisodeVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            if (v == itemView && presenter != null) {
                presenter.onEpisodeClicked(getAdapterPosition());
            }
        }
    }
}
