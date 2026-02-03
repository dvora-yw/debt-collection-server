package com.debtcollection.dto.endClient;

import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndClientUpdateDto {
    private String name;
    private BigDecimal totalDebt;
    private List<UserDto> users;
    private Set<Long> clientIds;

}
