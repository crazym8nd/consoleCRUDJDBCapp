package com.vitaly.crudjdbcapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Writer {
    private Integer id;
    private String firstName;
    private String lastName;
    private List<Post> writerPosts;
    private Status status;

}
