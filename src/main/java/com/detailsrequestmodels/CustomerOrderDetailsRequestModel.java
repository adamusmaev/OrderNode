package com.detailsrequestmodels;

import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.util.Date;

@Data
@Log4j
public class CustomerOrderDetailsRequestModel {

    private String name;
    private Date deliveryTime;
    private Boolean paid;   

}
