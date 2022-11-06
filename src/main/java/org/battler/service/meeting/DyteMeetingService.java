package org.battler.service.meeting;

import lombok.extern.slf4j.Slf4j;
import org.battler.model.session.Meeting;
import org.battler.service.meeting.dto.DyteRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 31.08.2022
 */
@Slf4j
@ApplicationScoped
public class DyteMeetingService implements MeetingService {

    @Inject
    @Any
    DyteMeetingClient dyteMeetingClient;

    @Override
    public CompletionStage<Meeting> createMeeting() {
        log.info("Sending request to Dyte");
        return dyteMeetingClient.createDyteMeeting(new DyteRequest("new room"))
                                .thenApply(dyteResponse -> {
                                    log.info("Got response from Dyte. response = {}", dyteResponse);
                                    Meeting meeting = new Meeting();
                                    meeting.setRoomName(dyteResponse.getData().getMeeting().getRoomName());
                                    meeting.setId(dyteResponse.getData().getMeeting().getId());
                                    return meeting;
                                });
    }
}
