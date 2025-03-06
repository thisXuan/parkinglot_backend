package com.parkinglot_backend.dto;

import lombok.Data;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-06
 * @Description:
 */

@Data
public class UserDTO {
    private String jwt;
    private Boolean type;
    public UserDTO(String jwt,Boolean type){
        this.jwt = jwt;
        this.type = type;
    }

}
