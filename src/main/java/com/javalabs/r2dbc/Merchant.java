package com.javalabs.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
    @Id
    private Long id;
    private String registeredName;
    private String doingBusinessAs;
    private String email;
    private String password;
    private String status;
}
