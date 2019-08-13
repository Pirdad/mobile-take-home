package com.pirdad.guestlogixtest.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixtest.R;
import com.pirdad.guestlogixtest.character.CharacterDetailPresenter;
import com.pirdad.guestlogixtest.character.CharacterView;
import com.pirdad.guestlogixtest.helpers.ImageLoader;

public class CharacterDetailActivity extends BaseActivity implements CharacterView {
    public static final String KEY_ID = "ID";

    private CharacterDetailPresenter presenter;
    private ImageView image;
    private TextView name;
    private TextView subHeading;
    private TextView status;
    private TextView gender;
    private TextView species;
    private TextView origin;
    private TextView location;

    private String originText;
    private String speciesText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        init();
    }

    private void init() {
        presenter = new CharacterDetailPresenter();
        presenter.setId(loadId());
        presenter.setView(this);
        presenter.setRepository(getRepositoryProvider().getRepository(Character.class));

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        subHeading = findViewById(R.id.subHeading);
        status = findViewById(R.id.status);
        gender = findViewById(R.id.gender);
        species = findViewById(R.id.species);
        origin = findViewById(R.id.origin);
        location = findViewById(R.id.location);
    }

    private long loadId() {
        return getIntent().getLongExtra(KEY_ID, -1);
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
    public void setPicture(final String imageUrl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageLoader.getInstance().loadImage(image, imageUrl);
            }
        });
    }

    @Override
    public void setName(final String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharacterDetailActivity.this.name.setText(name);
            }
        });
    }

    @Override
    public void setStatus(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharacterDetailActivity.this.status.setText(status);
            }
        });
    }

    @Override
    public void setGender(final String gender) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharacterDetailActivity.this.gender.setText(gender);
            }
        });
    }

    @Override
    public void setSpecies(final String species) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharacterDetailActivity.this.speciesText = species;
                CharacterDetailActivity.this.species.setText(species);
                updateSubHeading();
            }
        });
    }

    private void updateSubHeading() {
        StringBuilder sb = new StringBuilder();
        boolean validSpecies = !TextUtils.isEmpty(speciesText) && !speciesText.equalsIgnoreCase("unknown");
        boolean validOrigin = !TextUtils.isEmpty(originText) && !originText.equalsIgnoreCase("unknown");
        if (validSpecies) {
            sb.append(speciesText);
        }
        if (validSpecies && validOrigin) {
            sb.append(" from ");
        }
        if (validOrigin) {
            sb.append(originText);
        }
        subHeading.setText(sb.toString());
    }

    @Override
    public void setType(final String type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharacterDetailActivity.this.species.setText(CharacterDetailActivity.this.species.getText().toString() + ", " + type);
            }
        });
    }

    @Override
    public void setOrigin(final String origin) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharacterDetailActivity.this.originText = origin;
                CharacterDetailActivity.this.origin.setText(origin);
                updateSubHeading();
            }
        });
    }

    @Override
    public void setLocation(final String origin) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharacterDetailActivity.this.location.setText(origin);
            }
        });
    }

    @Override
    public void showSoftError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CharacterDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void dismiss() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }
}
