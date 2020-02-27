package com.javalabs.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
}
