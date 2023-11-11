package be.zqsd;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.slf4j.Logger;

import java.time.OffsetDateTime;

import static org.slf4j.LoggerFactory.getLogger;

@QuarkusMain
public class Main {

    public static void main(String[] args) {
        Quarkus.run(EnergyImporter.class);
    }

    public static class EnergyImporter implements QuarkusApplication {
        private static final Logger LOG = getLogger(EnergyImporter.class);
        private final Importer importer;

        @Inject
        public EnergyImporter(Importer importer) {
            this.importer = importer;
        }

        @Override
        public int run(String... args) throws Exception {
            LOG.info("Starting process at {}...", OffsetDateTime.now());
            importer.processDay();
            importer.processNight();
            LOG.info("Process finished at {}...", OffsetDateTime.now());
            Quarkus.waitForExit();
            Quarkus.asyncExit();
            return 0;
        }
    }
}
