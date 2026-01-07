package com.debtcollection.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonUpdateDto {
    private Long endClientId;
    private String firstName;
    private String lastName;
}
