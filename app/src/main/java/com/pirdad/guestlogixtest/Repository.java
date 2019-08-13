package com.pirdad.guestlogixtest;

import java.util.Collection;

public interface Repository<O> {

    void add(O item);

    void update(O item);

    void delete(O item);

    void delete(long id);

    Collection<O> getAll();

    O get(long id);
}
