package com.entitys;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "status", cascade = CascadeType.ALL)
    private CustomerOrder customerOrder;

}
