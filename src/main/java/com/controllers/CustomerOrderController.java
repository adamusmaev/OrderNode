package com.controllers;


import com.detailsrequestmodels.CustomerOrderDetailsRequestModel;
import com.entitys.CustomerOrder;
import com.entitys.Status;
import com.services.CustomerOrderService;
import com.services.StatusService;
import com.transfers.CustomerOrderTransfer;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


@RestController
@Log4j
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    private final StatusService statusService;

    public CustomerOrderController(CustomerOrderService customerOrderService, StatusService statusService) {
        this.customerOrderService = customerOrderService;
        this.statusService = statusService;
    }

    @PostMapping
    public void addCustomerOrder(@RequestBody CustomerOrderDetailsRequestModel customerOrderDRM) {
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setName(customerOrderDRM.getName());
        customerOrder.setDeliveryTime(new Date());
        customerOrder.setPaid(customerOrderDRM.getPaid());
        customerOrderService.saveOrder(customerOrder);
    }

    @GetMapping
    public List<CustomerOrderTransfer> findAllOrders() {
        List<CustomerOrderTransfer> customerOrderTransferList = new ArrayList<>();
        for (CustomerOrder c : customerOrderService.findOrders()) {
            customerOrderTransferList.add(new CustomerOrderTransfer(c));
        }
        return customerOrderTransferList;
    }

    @GetMapping("/{orderId}")
    public CustomerOrderTransfer findOrderById(@PathVariable Integer orderId) {
        CustomerOrderTransfer customerOrderTransfer = new CustomerOrderTransfer(customerOrderService.findCustomerOrderById(orderId));
        return customerOrderTransfer;
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Integer orderId) {
        CustomerOrder customerOrder = customerOrderService.findCustomerOrderById(orderId);
        customerOrderService.deleteOrder(customerOrder);
    }

    @PutMapping("/{orderId}")
    public void updateOrder(@PathVariable Integer orderId, @RequestBody CustomerOrderDetailsRequestModel customerOrderDRM) {
        CustomerOrder customerOrder = customerOrderService.findCustomerOrderById(orderId);
        customerOrder.setName(customerOrderDRM.getName());
        customerOrder.setPaid(customerOrderDRM.getPaid());
        customerOrderService.saveOrder(customerOrder);
    }

    @PatchMapping("/{orderId}/status/{statusId}")
    public void addStatus(@PathVariable Integer orderId, @PathVariable Integer statusId) {
        CustomerOrder customerOrder = customerOrderService.findCustomerOrderById(orderId);
        Status status = statusService.findStatusById(statusId);
        customerOrder.setStatus(status);
        status.setCustomerOrder(customerOrder);
        statusService.saveStatus(status);
        customerOrderService.saveOrder(customerOrder);
    }

    @GetMapping(value = "/offer/{offerId}")
    public void findOfferPrice(@PathVariable Integer offerId) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://offer.jelastic.regruhosting.ru/offer" + offerId);
        HttpResponse httpresponse = httpclient.execute(httpget);
        Scanner sc = new Scanner(httpresponse.getEntity().getContent());
        System.out.println(sc.nextLine());
    }

}
