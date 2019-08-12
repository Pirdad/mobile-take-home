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
import android.widget.ImageView;
import android.widget.TextView;

import com.pirdad.guestlogixtest.character.CharacterView;
import com.pirdad.guestlogixtest.character.CharactersListPresenter;
import com.pirdad.guestlogixtest.character.CharactersListView;
import com.pirdad.guestlogixtest.helpers.ImageLoader;
import com.pirdad.guestlogixtest.helpers.ListSpacingDecoration;

public class CharactersListActivity extends Activity implements CharactersListView {

    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_CHARACTER_IDS = "CHARACTER_IDS";

    private CharactersListPresenter presenter;
    private RecyclerView recycler;
    private TextView title;
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
        presenter.setCharacterIds(loadCharacterIds());
        presenter.setTitle(loadTitle());

        adapter = new Adapter();

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new ListSpacingDecoration(this, R.dimen.StandardMediumMargin));
        recycler.setAdapter(adapter);

        title = findViewById(R.id.title);
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

    private String loadTitle() {
        return getIntent().getStringExtra(KEY_TITLE);
    }

    private long[] loadCharacterIds() {
        return getIntent().getLongArrayExtra(KEY_CHARACTER_IDS);
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

    private class Adapter extends RecyclerView.Adapter<CharacterVH> {
        @NonNull
        @Override
        public CharacterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CharacterVH(getLayoutInflater().inflate(R.layout.item_character, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CharacterVH holder, int position) {
            if (presenter != null) {
                presenter.onLoadCharacterAt(position, holder);
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
        public void onClick(View v) {
            if (v == itemView && presenter != null) {
                presenter.onCharacterClicked(getAdapterPosition());
            }
        }
    }
}
