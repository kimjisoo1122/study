package com.hellojpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AddresEntity {

    @Id @GeneratedValue
    private Long id;


}
