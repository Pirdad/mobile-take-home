package com.pirdad.guestlogixtest;

public interface RepositoryProvider {

    <T> Repository<T> getRepository(Class<T> cls);
}
