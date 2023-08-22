package com.guro.kokeetea_project.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class SupplierFormDTO {
    private Long id;

    private String name;

    private String phone;

    private String location;

    @Email
    private String email;

    private String material;

    private String origin;



}
