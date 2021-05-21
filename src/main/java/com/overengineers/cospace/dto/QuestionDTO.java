package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class QuestionDTO {

    public final Long id;

    public final String content;

    public final String answer1;

    public final String answer2;

    public final String answer3;

    public final String answer4;

    public final String groundTruth;

    public final String parentName;

}
