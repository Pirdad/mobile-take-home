package com.pirdad.guestlogixtest.character;

import com.pirdad.guestlogixservice.domain.Character;
import com.pirdad.guestlogixtest.Repository;

public interface CharacterRepository extends Repository<Character> {

    void addToKillList(Character character);
}
