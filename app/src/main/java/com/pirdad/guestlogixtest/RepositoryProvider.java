package com.pirdad.guestlogixtest;

public interface RepositoryProvider {

    <O> Repository<O> getRepository(Class<O> cls);
}
