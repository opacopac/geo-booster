package com.tschanz.geobooster;

import com.tschanz.geobooster.netz_cache.service.*;
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

    private final BetreiberCacheRepo betreiberCacheRepo;
    private final VerwaltungCacheRepo verwaltungCacheRepo;
    private final HaltestelleCacheRepo haltestelleCacheRepo;
    private final VerkehrskanteCacheRepo verkehrskanteCacheRepo;
    private final TarifkanteCacheRepo tarifkanteCacheRepo;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("initializing cache repos...");
        this.betreiberCacheRepo.init();
        this.verwaltungCacheRepo.init();
        this.haltestelleCacheRepo.init();
        this.verkehrskanteCacheRepo.init();
        this.tarifkanteCacheRepo.init();;
        logger.info("cache initialization done");
    }
}
