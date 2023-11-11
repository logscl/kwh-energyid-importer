package be.zqsd.influxdb;

import io.quarkus.rest.client.reactive.ClientQueryParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/v2")
@RegisterRestClient(configKey = "influx-db-client")
@ClientHeaderParam(name = "Authorization", value = "Bearer ${influx-db.api.key}")
@Produces("application/csv")
@Consumes("application/vnd.flux")
public interface InfluxDBClient {

    @Path("/query")
    @POST
    @ClientQueryParam(name = "orgID", value = "${influx-db.org-id}")
    String queryLastMeasurement(String body);
}
