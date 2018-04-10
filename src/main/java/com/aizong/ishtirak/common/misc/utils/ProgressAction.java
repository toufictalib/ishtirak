package com.aizong.ishtirak.common.misc.utils;

public interface ProgressAction<T> {

    T action();

    void success(T t);

    void failure(Exception e);

}
