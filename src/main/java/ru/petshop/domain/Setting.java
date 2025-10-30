package ru.petshop.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Setting implements Serializable {
    private String name;
    private Integer value;
    private String valueString;
}
