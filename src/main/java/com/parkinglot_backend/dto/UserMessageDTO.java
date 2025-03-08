package com.parkinglot_backend.dto;

import lombok.Data;

@Data
public class UserMessageDTO {
    private Integer id;

    private String name;

    private String phone;

    private String point;

    private Integer type;

    public UserMessageDTO(Integer id, String name, String phone, String point, Integer type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.point = point;
        this.type = type;
    }
}
