package org.battler.model.session;

/**
 * Created by romanivanov on 10.11.2022
 */
public enum QuestionType {
    JS,
    JAVA;

    public static QuestionType of(String value) {
        value = value.toUpperCase();
        for (final QuestionType type : QuestionType.values()) {
            if (type.name().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown question type [%s]", value));
    }


}
