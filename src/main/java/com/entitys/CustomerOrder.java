package com.entitys;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Date deliveryTime;

    private Boolean paid;

    private Integer customerId;

    private Integer offerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private Status status;

}
