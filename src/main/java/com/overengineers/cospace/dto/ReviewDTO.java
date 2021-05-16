package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDTO {

    private String author;

    private String content;

    private int rating;

    private String parentName;
}
