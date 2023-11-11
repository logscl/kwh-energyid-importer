package be.zqsd.energyid;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record Reading(String key, OffsetDateTime timestamp, double value) {

    public boolean isOccurringOn(LocalDate day) {
        return timestamp.toLocalDate().equals(day);
    }
}