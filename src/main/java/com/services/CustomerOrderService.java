package com.services;

import com.entitys.CustomerOrder;
import com.repository.CustomerOrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

@Service
@Log4j
@RequiredArgsConstructor
public class CustomerOrderService {

    private final CustomerOrderRepo customerOrderRepo;

    public CustomerOrder findCustomerOrderById(Integer id){
        CustomerOrder customerOrder = customerOrderRepo.findById(id).orElse(null);
        if (customerOrder == null) log.error("Order not found");
        return customerOrder;
    }

    public Iterable<CustomerOrder> findOrders(){
        return customerOrderRepo.findAll();
    }

    public void saveOrder(CustomerOrder customerOrder){
        customerOrderRepo.save(customerOrder);
    }

    public void deleteOrder(CustomerOrder customerOrder){
        customerOrderRepo.delete(customerOrder);
    }


}
