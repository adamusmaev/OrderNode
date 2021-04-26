package com.repository;

import com.entitys.CustomerOrder;
import org.springframework.data.repository.CrudRepository;

public interface CustomerOrderRepo extends CrudRepository<CustomerOrder, Integer> {
}
