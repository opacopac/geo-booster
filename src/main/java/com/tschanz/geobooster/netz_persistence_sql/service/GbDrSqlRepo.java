package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.GbDr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GbDrSqlRepo {
    private final BetreiberSqlRepo betreiberSqlRepo;
    private final VerwaltungSqlRepo verwaltungSqlRepo;
    private final HaltestelleSqlRepo haltestelleSqlRepo;
    private final VerkehrskanteSqlRepo verkehrskanteSqlRepo;
    private final TarifkanteSqlRepo tarifkanteSqlRepo;


    public GbDr loadDr() {
        return new GbDr(
            this.betreiberSqlRepo.readAllElements(),
            this.betreiberSqlRepo.readAllVersions(),
            this.verwaltungSqlRepo.readAllElements(),
            this.verwaltungSqlRepo.readAllVersions(),
            this.haltestelleSqlRepo.readAllElements(),
            this.haltestelleSqlRepo.readAllVersions(),
            this.verkehrskanteSqlRepo.readAllElements(),
            this.verkehrskanteSqlRepo.readAllVersions(),
            this.tarifkanteSqlRepo.readAllElements(),
            this.tarifkanteSqlRepo.readAllVersions()
        );
    }
}
