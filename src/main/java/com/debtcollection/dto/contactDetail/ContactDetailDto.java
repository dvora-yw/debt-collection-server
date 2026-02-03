package com.debtcollection.dto.contactDetail;

import com.debtcollection.entity.ContactType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDetailDto {
    private Long id;
    private Long clientId;
    private Long endClientId;
    private Long userId;
    private ContactType type;
    private String value;

}
