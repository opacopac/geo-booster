package com.tschanz.geobooster;

import com.tschanz.geobooster.netz.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GeoBoosterStartupRunner implements ApplicationRunner {
    private static final Logger logger = LogManager.getLogger(GeoBoosterStartupRunner.class);

    private final BetreiberRepo betreiberRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;


    @Override
    public void run(ApplicationArguments args) {
        logger.info("initializing cache repos...");
        this.betreiberRepo.init();
        this.verwaltungRepo.init();
        this.haltestelleRepo.init();
        this.verkehrskanteRepo.init();
        this.tarifkanteRepo.init();;
        logger.info("cache initialization done");
    }
}
