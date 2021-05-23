package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class SubClubDTO {

    public final int id;

    public final Date created;

    public final String name;

    public final String details;

    public final String parentName;

    public final int rating;

    public final String moderatorUsername;

    public final List<QuestionDTO> questions;

}
