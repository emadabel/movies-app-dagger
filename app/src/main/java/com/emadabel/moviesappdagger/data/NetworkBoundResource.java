package com.emadabel.moviesappdagger.data;

import android.support.annotation.NonNull;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private Observable<Resource<ResultType>> mResult;

    protected NetworkBoundResource() {
        Observable<Resource<ResultType>> source;
        if (shouldFetch()) {
            source = createCall()
                    .subscribeOn(Schedulers.io())
                    .doOnNext(apiResponse -> saveCallResult(processResponse(apiResponse)))
                    .flatMap(apiResponse -> loadFromDb().toObservable().map(Resource::success))
                    .doOnError(t -> onFetchFaild())
                    .onErrorResumeNext(t -> {
                        return loadFromDb()
                                .toObservable()
                                .map(data -> Resource.error(t.getMessage(), data));
                    })
                    .observeOn(AndroidSchedulers.mainThread());

        }
    }

    protected void onFetchFaild() {}

    protected RequestType processResponse(Resource<RequestType> response) {
        return response.mData;
    }

    protected abstract void saveCallResult(@NonNull RequestType item);

    protected abstract boolean shouldFetch();

    protected abstract Flowable<ResultType> loadFromDb();

    protected abstract Observable<Resource<RequestType>> createCall();
}
