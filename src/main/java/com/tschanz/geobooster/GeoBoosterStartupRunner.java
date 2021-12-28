package com.tschanz.geobooster;

import com.tschanz.geobooster.netz.model.GbDr;
import com.tschanz.geobooster.netz.service.GbDrRepo;
import com.tschanz.geobooster.netz_persistence_json.service.GbDrJsonRepo;
import com.tschanz.geobooster.netz_persistence_sql.service.GbDrSqlRepo;
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

    private final GbDrSqlRepo gbDrSqlRepo;
    private final GbDrJsonRepo gbDrJsonRepo;
    private final GbDrRepo gbDrRepo;


    @Override
    public void run(ApplicationArguments args) {
        GbDr dr;

        if (this.gbDrJsonRepo.hasQuickStartDr()) {
            logger.info("loading json data...");
            dr = this.gbDrJsonRepo.readDr();
            logger.info("loading json data done.");
        } else {
            logger.info("loading sql data...");
            dr = this.gbDrSqlRepo.loadDr();
            logger.info("loading sql data done.");

            logger.info("saving quick start dr...");
            this.gbDrJsonRepo.save(dr);
            logger.info("saving quick start dr done.");
        }

        logger.info("init repos...");
        this.gbDrRepo.init(dr);
        logger.info("init repos done.");
    }
}
