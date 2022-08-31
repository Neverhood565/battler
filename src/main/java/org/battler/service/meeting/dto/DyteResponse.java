package org.battler.service.meeting.dto;

import lombok.Data;

/**
 * Created by romanivanov on 31.08.2022
 */
@Data
public class DyteResponse {

    private String success;
    private Data data;


    @lombok.Data
    public static class Data {

        private Meeting meeting;
    }

    @lombok.Data
    public static class Meeting {

        private String id;
        private String roomName;
    }
}