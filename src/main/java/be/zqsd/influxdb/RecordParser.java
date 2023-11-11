package be.zqsd.influxdb;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;

import static java.util.function.Predicate.not;

@ApplicationScoped
public class RecordParser {

    public Collection<Record> parse(String csv) {
        return csv.lines()
                .skip(1) // header
                .filter(not(String::isBlank))
                .map(Record::from)
                .toList();
    }
}
