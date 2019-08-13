package com.pirdad.guestlogixtest.views;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixservice.domain.Episode;
import com.pirdad.guestlogixtest.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pirdad.guestlogixtest.character.CharacterView;
import com.pirdad.guestlogixtest.character.CharactersListPresenter;
import com.pirdad.guestlogixtest.character.CharactersListView;
import com.pirdad.guestlogixtest.character.HeaderView;
import com.pirdad.guestlogixtest.helpers.ImageLoader;
import com.pirdad.guestlogixtest.helpers.ListSpacingDecoration;
import com.pirdad.guestlogixtest.navigation.CharacterDetailNavigationHandler;

public class CharactersListActivity extends BaseActivity implements CharactersListView {

    public static final String KEY_EPISODE_ID = "KEY_EPISODE_ID";

    private CharactersListPresenter presenter;
    private RecyclerView recycler;
    private Toolbar toolbar;
    private View loader;
    private Adapter adapter;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);
        init();
    }

    private void init() {
        presenter = new CharactersListPresenter();
        presenter.setView(this);
        presenter.setCharacterRepository(getRepositoryProvider().getRepository(Character.class));
        presenter.setEpisodeRepository(getRepositoryProvider().getRepository(Episode.class));
        presenter.setCharacterToDetailNavigationHandler(new CharacterDetailNavigationHandler(this));
        presenter.setEpisodeId(loadEpisodeId());

        adapter = new Adapter();

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new ListSpacingDecoration(this, R.dimen.StandardMediumMargin));
        recycler.setAdapter(adapter);

        toolbar = findViewById(R.id.toolbar);
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
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.refresh();
        }
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.setView(null);
        }
        super.onDestroy();
    }

    private long loadEpisodeId() {
        return getIntent().getLongExtra(KEY_EPISODE_ID, -1);
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
                Toast.makeText(CharactersListActivity.this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void setTitle(final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharactersListActivity.this.toolbar.setTitle(title);
            }
        });
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public int getItemViewType(int position) {
            if (presenter != null) {
                return presenter.getItemTypeAt(position);
            }
            return super.getItemViewType(position);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == CharactersListPresenter.ITEM_TYPE_HEADING) {
                return new HeaderVH(getLayoutInflater().inflate(R.layout.item_header, parent, false));
            } else {
                return new CharacterVH(getLayoutInflater().inflate(R.layout.item_character, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (presenter != null) {
                if (holder instanceof HeaderVH) {
                    presenter.onLoadHeaderAt(position, (HeaderView) holder);
                } else {
                    presenter.onLoadCharacterAt(position, (CharacterView) holder);
                }
            }
        }

        @Override
        public int getItemCount() {
            return presenter != null ? presenter.getCharacterCount() : 0;
        }
    }

    private class CharacterVH extends RecyclerView.ViewHolder implements CharacterView, View.OnClickListener {

        private ImageView image;
        private TextView name;
        private TextView detail;

        public CharacterVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            detail = itemView.findViewById(R.id.detail);
        }

        @Override
        public void setPicture(String imageUrl) {
            image.setImageBitmap(null);
            ImageLoader.getInstance().loadImage(image, imageUrl);
        }

        @Override
        public void setName(String name) {
            this.name.setText(name);
        }

        @Override
        public void setStatus(String status) {
            detail.setText(status);
        }

        @Override
        public void setGender(String gender) {
            String currText = this.detail.getText().toString();
            if (!TextUtils.isEmpty(currText) && !TextUtils.isEmpty(gender)) {
                currText = currText + " - " + gender;
            } else {
                currText = gender;
            }
            this.detail.setText(currText);
        }

        @Override
        public void setSpecies(String species) { }

        @Override
        public void setType(String type) {}

        @Override
        public void setOrigin(String origin) {}

        @Override
        public void setLocation(String origin) {}

        @Override
        public void showSoftError(String message) {
            CharactersListActivity.this.showSoftError(message);
        }

        @Override
        public void dismiss() { }

        @Override
        public void showKillButton() { }

        @Override
        public void hideKillButton() { }

        @Override
        public void onClick(View v) {
            if (v == itemView && presenter != null) {
                presenter.onCharacterClicked(getAdapterPosition());
            }
        }
    }

    private class HeaderVH extends RecyclerView.ViewHolder implements HeaderView {

        private TextView header;

        public HeaderVH(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
        }

        @Override
        public void setHeading(String heading) {
            header.setText(heading);
        }
    }
}
