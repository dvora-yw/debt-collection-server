package com.debtcollection.dto.contactDetail;

import com.debtcollection.entity.ContactType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactDetailDto {
    private Long id;
    private Long clientId;
    private Long endClientId;
    private Long personId;
    private ContactType type;
    private String value;

}
