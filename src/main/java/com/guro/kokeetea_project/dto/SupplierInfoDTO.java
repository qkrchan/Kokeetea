package com.guro.kokeetea_project.dto;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class SupplierInfoDTO {

    private Long id;

    private String name;

    private String phone;

    private String location;

    private String email;

    private String material;

    private String origin;

    private String categoryName;
}
