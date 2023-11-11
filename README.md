# kwh-energyid-importer

This project reads values (two, one for the day meter and one for the night meter) from an InfluxDB Instance, and push them into an EnergyID Account.  
It pushes the last available value in the InfluxDB for the previous day, only if there is no value for that day in EnergyID.

This project is built with Quarkus and GraalVM into a Docker Image available on Docker Hub (`docker pull logscl/kwh-energyid-importer`).

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.

## Environment variables

To run this application (see below), you must provide some variables to the application.

There are multiple ways of doing that (see quarkus documentation). Below you will find the variables that you must pass as environment variables:

| Name                                     | Description                                                              | Example                              |
|------------------------------------------|--------------------------------------------------------------------------|--------------------------------------|
| QUARKUS_REST_CLIENT_ENERGY_ID_CLIENT_URL | URL of the API of EnergyID                                               | https://api.energyid.eu              |
| ENERGY_ID_API_KEY                        | API Key of your EnergyID Account. You can generate it from the settings. | <some-api-key>                       |
| ENERGY_ID_DAY_METER_ID                   | UUID of the 'day' meter in EnergyID                                      | 32d84de8-d1ee-4555-8447-a0aa04f12bf0 |
| ENERGY_ID_NIGHT_METER_ID                 | UUID of the 'night' meter in EnergyID                                    | 74d5fd69-d0ea-4697-8129-839a422935b4 |
| QUARKUS_REST_CLIENT_INFLUX_DB_CLIENT_URL | URL of an InfluxDB Instance where the data sits.                         | https://influxdb.yourdomain.com      |
| INFLUX_DB_API_KEY                        | API Key (read access is enough) with access to the meter values          | <some-api-key>                       |
| INFLUX_DB_ORG_ID                         | Organisation ID (not the name !) where the data sits                     | <some-organisation-id>               |
| INFLUX_DB_BUCKET_NAME                    | The buckets where the data sits                                          | electricmeter                        |
| INFLUX_DB_MEASUREMENT                    | The measurement name of the data (aka table name)                        | readings                             |
| INFLUX_DB_DAY_FIELD_NAME                 | The field name for the 'day' meter value                                 | day                                  |
| INFLUX_DB_NIGHT_FIELD_NAME               | The field name for the 'night' meter value                               | night                                |

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

Don't forget to pass the required variable values.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/kwh-energyid-importer-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## TODO

* Add Tests
* Clean up code
* Sentry (error reporting)