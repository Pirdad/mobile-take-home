package com.pirdad.guestlogixtest;

import java.util.List;

public interface Repository<O> {

    void add(O item);

    void update(O item);

    void delete(O item);

    void delete(long id);

    List<O> getAll();

    O get(long id);
}
