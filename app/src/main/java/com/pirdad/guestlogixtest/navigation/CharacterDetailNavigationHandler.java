package com.pirdad.guestlogixtest.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pirdad.guestlogixtest.character.CharacterToDetailNavigation;
import com.pirdad.guestlogixtest.views.CharacterDetailActivity;

public class CharacterDetailNavigationHandler implements CharacterToDetailNavigation {

    private Bundle args = new Bundle();
    private Context context;

    public CharacterDetailNavigationHandler(Context context) {
        this.context = context;
    }

    @Override
    public void setCharacterId(long id) {
        args.putLong(CharacterDetailActivity.KEY_ID, id);
    }

    @Override
    public void execute() {
        Intent intent = new Intent(context, CharacterDetailActivity.class);
        intent.putExtras(args);
        context.startActivity(intent);
    }
}
