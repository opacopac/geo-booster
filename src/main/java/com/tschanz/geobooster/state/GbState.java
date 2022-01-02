package com.tschanz.geobooster.state;

import com.tschanz.geobooster.netz_repo.model.NetzRepoState;
import com.tschanz.geobooster.webmapservice.model.WmsState;
import io.reactivex.subjects.BehaviorSubject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Getter
@Component
@RequiredArgsConstructor
public class GbState {
    private final BehaviorSubject<List<String>> connectionList$ = BehaviorSubject.createDefault(Collections.emptyList());
    private final BehaviorSubject<Integer> selectedConnection$ = BehaviorSubject.createDefault(-1);
    private final ProgressState progressState;
    private final NetzRepoState netzRepoState;
    private final WmsState wmsState;
}
