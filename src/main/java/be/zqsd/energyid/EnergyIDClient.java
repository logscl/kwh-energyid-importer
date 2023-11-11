package be.zqsd.energyid;

import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.OffsetDateTime;

@Path("/api/v1")
@RegisterRestClient(configKey = "energy-id-client")
@ClientHeaderParam(name = "Authorization", value = "apikey ${energy-id.api.key}")
public interface EnergyIDClient {

    @Path("/Meters/{meterId}/readings")
    @POST
    Reading addReading(@PathParam("meterId") String meterId, @QueryParam("timestamp") OffsetDateTime timestamp, @QueryParam("value") double value);

    @Path("/Meters/{meterId}/readings")
    @GET
    Readings getReadings(@PathParam("meterId") String meterId);

}
