package be.zqsd.energyid;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@ApplicationScoped
public class EnergyIDService {

    private final String dayMeterId;
    private final String nightMeterId;
    private final EnergyIDClient client;

    public EnergyIDService(@ConfigProperty(name = "energy-id.day-meter-id") String dayMeterId,
                           @ConfigProperty(name = "energy-id.night-meter-id")String nightMeterId,
                           @RestClient EnergyIDClient client) {
        this.dayMeterId = dayMeterId;
        this.nightMeterId = nightMeterId;
        this.client = client;
    }

    public boolean hasDayReadingFor(LocalDate day) {
        return hasReadingFor(client.getReadings(dayMeterId), day);
    }

    public boolean hasNightReadingFor(LocalDate day) {
        return hasReadingFor(client.getReadings(nightMeterId), day);
    }

    public Reading addDayReading(OffsetDateTime timestamp, double value) {
        return client.addReading(dayMeterId, timestamp, value);
    }

    public Reading addNightReading(OffsetDateTime timestamp, double value) {
        return client.addReading(nightMeterId, timestamp, value);
    }

    private boolean hasReadingFor(Readings readings, LocalDate day) {
        return readings
                .readings()
                .stream()
                .findFirst()
                .map(reading -> reading.isOccurringOn(day))
                .orElse(false);
    }
}
