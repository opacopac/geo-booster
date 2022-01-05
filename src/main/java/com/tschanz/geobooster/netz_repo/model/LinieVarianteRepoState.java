package com.tschanz.geobooster.netz_repo.model;

import com.tschanz.geobooster.versioning_repo.model.VersionedObjectRepoState;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import org.springframework.stereotype.Component;


@Component
public class LinieVarianteRepoState extends VersionedObjectRepoState {
    private final BehaviorSubject<Boolean> isLoading$ = BehaviorSubject.createDefault(false);
    private final BehaviorSubject<Integer> loadedElementCount$ = BehaviorSubject.createDefault(0);


    public Observable<Boolean> getIsLoading$() {
        return this.isLoading$;
    }


    public void updateIsLoading(boolean isLoading) {
        this.isLoading$.onNext(isLoading);
    }


    public Observable<Integer> getLoadedElementCount$() {
        return this.loadedElementCount$;
    }


    public void updateLoadedElementCount(int count) {
        this.loadedElementCount$.onNext(count);
    }
}
