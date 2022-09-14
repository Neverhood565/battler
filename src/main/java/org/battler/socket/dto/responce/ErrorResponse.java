package org.battler.socket.dto.responce;

import lombok.Builder;
import lombok.Data;

/**
 * Created by romanivanov on 14.09.2022
 */
@Data
public class ErrorResponse {

    private final Boolean success = false;
    private final String message;

}
