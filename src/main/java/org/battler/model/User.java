package org.battler.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by romanivanov on 28.08.2022
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
@ToString(of = "id")
public class User {

    private final String id;
    private final String name;
}
