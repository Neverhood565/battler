package org.battler.service.meeting;

import org.battler.model.sessions.Meeting;
import org.battler.service.meeting.dto.DyteRequest;
import org.battler.service.meeting.dto.DyteResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 31.08.2022
 */
@ApplicationScoped
public class MeetingServiceImpl implements MeetingService {

    @Inject
    @Any
    DyteMeetingService dyteMeetingService;

    @Override
    public CompletionStage<Meeting> createMeeting() {
        return dyteMeetingService.createDyteMeeting(new DyteRequest("new room"))
                                 .thenApply(dyteResponse -> {
                                     Meeting meeting = new Meeting();
                                     meeting.setRoomName(dyteResponse.getData().getMeeting().getRoomName());
                                     meeting.setId(dyteResponse.getData().getMeeting().getId());
                                     return meeting;
                                 });
    }
}
