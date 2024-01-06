package com.vitaly.crudjdbcapp.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post  {
    private Integer id;
    private String content;
    private String created;
    private String updated;
    private PostStatus postStatus;
    private List<Label> postLabels;
    private Integer writerId;
}

