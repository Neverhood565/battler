package org.battler.service.meeting;

import org.battler.service.meeting.dto.DyteRequest;
import org.battler.service.meeting.dto.DyteResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 31.08.2022
 */
@Path("/meeting")
@RegisterRestClient(configKey = "dyte-client")
public interface DyteMeetingService {

    @POST
    @Path("/create")
    CompletionStage<DyteResponse> createDyteMeeting(DyteRequest request);
}
