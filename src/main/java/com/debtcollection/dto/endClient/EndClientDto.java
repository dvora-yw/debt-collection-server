package com.debtcollection.dto.endClient;

import com.debtcollection.dto.user.UserDto;
import com.debtcollection.entity.Client;
import com.debtcollection.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class EndClientDto {
    private Long id;
    private Set<Long> clientIds;
    private String name;
    private BigDecimal totalDebt;
    private List<UserDto> users;
}

