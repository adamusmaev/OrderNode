package com.services;

import com.entitys.Status;
import com.repository.StatusRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

@Service
@Log4j
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepo statusRepo;

    public Status findStatusById(Integer id){
        return statusRepo.findById(id).orElse(null);
    }

    public void saveStatus(Status status){
        statusRepo.save(status);
    }
    public void deleteStatus(Status status){
        statusRepo.delete(status);
    }
}
