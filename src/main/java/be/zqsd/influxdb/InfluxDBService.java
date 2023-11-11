package be.zqsd.influxdb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class InfluxDBService {

    private final String dayMeter;
    private final String nightMeter;
    private final QueryBuilder queryBuilder;
    private final RecordParser recordParser;
    private final InfluxDBClient client;

    @Inject
    public InfluxDBService(@ConfigProperty(name = "influx-db-day-field-name") String dayMeter,
                           @ConfigProperty(name = "influx-db-night-field-name") String nightMeter,
                           QueryBuilder queryBuilder,
                           RecordParser recordParser,
                           @RestClient InfluxDBClient client) {
        this.dayMeter = dayMeter;
        this.nightMeter = nightMeter;
        this.queryBuilder = queryBuilder;
        this.recordParser = recordParser;
        this.client = client;
    }

    public Record findLastDayValueFor(LocalDate day) {
        var body = queryBuilder.lastValueForDayAndMeter(day, dayMeter);
        return findRecord(body).orElse(null);
    }

    public Record findLastNightValueFor(LocalDate day) {
        var body = queryBuilder.lastValueForDayAndMeter(day, nightMeter);
        return findRecord(body).orElse(null);
    }


    public Optional<Record> findRecord(String queryBody) {
        return recordParser.parse(client.queryLastMeasurement(queryBody)).stream().findFirst();
    }
}
