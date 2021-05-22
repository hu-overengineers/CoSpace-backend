package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDTO {

    public final Long id;

    public final String author;

    private final String content;

    private final String postId;

}
