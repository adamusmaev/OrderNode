package com.controllers;

import com.detailsrequestmodels.StatusDetailsRequestModel;
import com.entitys.Status;
import com.services.StatusService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j
@RequestMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatusController {


    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @PostMapping
    public void addStatus(@RequestBody StatusDetailsRequestModel statusDRM){
        Status status = new Status();
        status.setName(statusDRM.getName());
        statusService.saveStatus(status);
    }
}
