package com.debtcollection.dto.person;


import com.debtcollection.dto.contact.ClientContactDto;
import com.debtcollection.dto.contactDetail.ContactDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonDto {
    private Long id;
    private Long endClientId;
    private String firstName;
    private String lastName;
    private List<ContactDetailDto> contacts;

}
