package com.estaine.elo.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data

@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue
    private Long id;

}
