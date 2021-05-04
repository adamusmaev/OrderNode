package com.transfers;

import com.entitys.Status;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@Data
@Log4j
public class StatusTransfer {

    private Integer id;
    private String name;


    public StatusTransfer(Status status) {
        this.id = status.getId();
        this.name = status.getName();
    }
}
