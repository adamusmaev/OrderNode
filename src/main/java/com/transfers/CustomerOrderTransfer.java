package com.transfers;

import com.entitys.CustomerOrder;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.util.Date;


@Data
@Log4j
public class CustomerOrderTransfer {

    private Integer id;
    private String name;
    private Date deliveryTime;
    private Boolean paid;
    private StatusTransfer statusTransfer;

    public CustomerOrderTransfer(CustomerOrder customerOrder) {
        this.id = customerOrder.getId();
        this.name = customerOrder.getName();
        this.deliveryTime = customerOrder.getDeliveryTime();
        this.paid = customerOrder.getPaid();
        this.statusTransfer = new StatusTransfer(customerOrder.getStatus());
    }
}
