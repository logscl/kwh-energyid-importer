package be.zqsd;

import be.zqsd.energyid.EnergyIDService;
import be.zqsd.influxdb.InfluxDBService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;

import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;

@ApplicationScoped
public class Importer {
    private static final Logger LOG = getLogger(Importer.class);

    private final EnergyIDService energyIDService;
    private final InfluxDBService influxDBService;
    private final LocalDate dayToProcess;

    @Inject
    public Importer(EnergyIDService energyIDService,
                    InfluxDBService influxDBService) {
        this.energyIDService = energyIDService;
        this.influxDBService = influxDBService;
        this.dayToProcess = LocalDate.now().minusDays(1); // process data for yesterday (runs early in the morning of the next day)
    }

    public void processDay() {
        LOG.info("Starting process of day reading for {}", dayToProcess);
        if (!energyIDService.hasDayReadingFor(dayToProcess)) {
            var lastValueOfDay = influxDBService.findLastDayValueFor(dayToProcess);
            if (lastValueOfDay != null) {
                var reading = energyIDService.addDayReading(lastValueOfDay.time(), lastValueOfDay.value());
                LOG.info("Process for day done. Timestamp: {} - value: {}", reading.timestamp(), reading.value());
            } else {
                LOG.warn("For some reason, no day value was found in InfluxDB");
            }
        } else {
            LOG.info("There is already a record for the day meter. Skipping.");
        }
    }

    public void processNight() {
        LOG.info("Starting process of night reading for {}", dayToProcess);
        if (!energyIDService.hasNightReadingFor(dayToProcess)) {
            var lastValueOfDay = influxDBService.findLastNightValueFor(dayToProcess);
            if (lastValueOfDay != null) {
                var reading = energyIDService.addNightReading(lastValueOfDay.time(), lastValueOfDay.value());
                LOG.info("Process for night done. Timestamp: {} - value: {}", reading.timestamp(), reading.value());
            } else {
                LOG.warn("For some reason, no night value was found in InfluxDB");
            }
        } else {
            LOG.info("There is already a record for the night meter. Skipping.");
        }
    }
}
