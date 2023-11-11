package be.zqsd.influxdb;

import java.time.OffsetDateTime;

public record Record(OffsetDateTime start, OffsetDateTime stop, OffsetDateTime time, double value, String field, String measurement) {

    static Record from(String raw) {
        var values = raw.split(",");
        return new Record(
                OffsetDateTime.parse(values[3]),
                OffsetDateTime.parse(values[4]),
                OffsetDateTime.parse(values[5]),
                Double.parseDouble(values[6]),
                values[7],
                values[8]
        );
    }
}
