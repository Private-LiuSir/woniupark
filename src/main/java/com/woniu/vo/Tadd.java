package com.woniu.vo;


import com.woniu.model.User;
import lombok.Data;

@Data
public class Tadd {
    private String jwtToken;
    private User user;
    private int roleId;
}
