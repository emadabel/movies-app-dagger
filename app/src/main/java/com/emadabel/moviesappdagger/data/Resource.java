package com.emadabel.moviesappdagger.data;

import static com.emadabel.moviesappdagger.data.Status.ERROR;
import static com.emadabel.moviesappdagger.data.Status.LOADING;
import static com.emadabel.moviesappdagger.data.Status.SUCCESS;

public class Resource<T> {

    public final Status mStatus;

    public final T mData;

    public final String mMessage;

    private Resource(Status status, T data, String message) {
        mStatus = status;
        mData = data;
        mMessage = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(LOADING, data, null);
    }

    public boolean isSuccess() {
        return mStatus == SUCCESS && mData != null;
    }

    public boolean isLoading() {
        return mStatus == LOADING;
    }

    public boolean isLoaded() {
        return mStatus != LOADING;
    }
}
