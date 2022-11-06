package org.battler.service.meeting;

import org.battler.model.session.Meeting;

import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 31.08.2022
 */
public interface MeetingService {

    CompletionStage<Meeting> createMeeting();
}
