package com.pirdad.guestlogixtest.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pirdad.guestlogixtest.episode.EpisodeToCharactersNavigation;
import com.pirdad.guestlogixtest.views.CharactersListActivity;

public class EpisodeCharactersNavigationHandler implements EpisodeToCharactersNavigation {

    private Context context;
    private Bundle args = new Bundle();

    public EpisodeCharactersNavigationHandler(Context context) {
        this.context = context;
    }

    @Override
    public void setEpisodeId(long id) {
        args.putLong(CharactersListActivity.KEY_EPISODE_ID, id);
    }

    @Override
    public void execute() {
        Intent intent = new Intent(context, CharactersListActivity.class);
        intent.putExtras(args);
        context.startActivity(intent);
    }
}
