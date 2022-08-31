package org.battler.model.question;

import lombok.Builder;
import lombok.Data;

/**
 * Created by romanivanov on 31.08.2022
 */
@Data
@Builder
public class Answer {

    private String id;
    private String title;
}
