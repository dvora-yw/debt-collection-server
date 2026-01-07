package com.debtcollection.dto.endClient;

import com.debtcollection.dto.person.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class EndClientCreateDto {
    private Long clientId;
    private String name;
    private BigDecimal totalDebt;
    private List<PersonDto> persons;

}
