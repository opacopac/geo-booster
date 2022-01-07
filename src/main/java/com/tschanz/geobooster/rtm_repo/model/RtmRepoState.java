package com.tschanz.geobooster.rtm_repo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class RtmRepoState {
    private final HaltestelleWegangabeRepoState hstWegangabeRepoState;
    private final RgAuspraegungRepoState rgAuspraegungRepoState;
}
