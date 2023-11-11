package be.zqsd.influxdb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDate;
import java.time.ZoneId;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@ApplicationScoped
public class QueryBuilder {
    private final String bucketName;

    private final String measurement;

    private final ZoneId localZone;
    private static final ZoneId UTC = ZoneId.of("UTC");

    @Inject
    public QueryBuilder(@ConfigProperty(name = "influx-db.bucket-name")String bucketName,
                        @ConfigProperty(name = "influx-db.measurement") String measurement,
                        @ConfigProperty(name = "importer.timezone-name") String timezoneName) {
        this.bucketName = bucketName;
        this.measurement = measurement;
        this.localZone = ZoneId.of(timezoneName);
    }

    private static final String QUERY_FOR_LAST_VALUE_FOR_DAY_AND_METER = """
            from(bucket: "%s")
              |> range(start: %s, stop: %s)
              |> filter(fn: (r) => r._measurement == "%s")
              |> filter(fn: (r) => r._field == "%s")
              |> last()
            """;

    public String lastValueForDayAndMeter(LocalDate day, String meter) {
        var start = day.atStartOfDay(localZone).withZoneSameInstant(UTC);
        var end = start.plusDays(1);
        return QUERY_FOR_LAST_VALUE_FOR_DAY_AND_METER.formatted(
                bucketName,
                start.format(ISO_OFFSET_DATE_TIME),
                end.format(ISO_OFFSET_DATE_TIME),
                measurement,
                meter);
    }
}
