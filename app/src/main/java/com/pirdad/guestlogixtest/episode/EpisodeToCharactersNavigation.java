package com.pirdad.guestlogixtest.episode;

import com.pirdad.guestlogixtest.Navigate;

public interface EpisodeToCharactersNavigation extends Navigate {

    void setEpisodeTitle(String title);

    void setCharacterIds(long[] ids);
}
