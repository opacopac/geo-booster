package com.tschanz.geobooster.presentation.model;

import com.tschanz.geobooster.netz_repo.model.NetzRepoState;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.webmapservice.model.WmsState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class GbState {
    private final ProgressState progressState;
    private final ConnectionState connectionState;
    private final NetzRepoState netzRepoState;
    private final WmsState wmsState;
}
