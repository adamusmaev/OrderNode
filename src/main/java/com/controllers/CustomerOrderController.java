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
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    private final String httpGetOfferUri = "http://offernode.jelastic.regruhosting.ru/offer/";

    private final String httpGetCustomerUriId = "http://customer.jelastic.regruhosting.ru/customer/id";

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
    public String findOfferPrice(@PathVariable Integer offerId) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(httpGetOfferUri + offerId);
        HttpResponse httpresponse = httpclient.execute(httpget);
        Scanner sc = new Scanner(httpresponse.getEntity().getContent());
        JSONObject jsonObjectOffer = new JSONObject(sc.nextLine());
        Float priceOffer = jsonObjectOffer.getFloat("price");
        JSONObject jsonObjectCategory = jsonObjectOffer.getJSONObject("categoryTransfer");
        JSONObject jsonObjectRes = new JSONObject();
        jsonObjectRes.put("price", priceOffer);
        jsonObjectRes.put("categoryTransfer", jsonObjectCategory);
        System.out.println(jsonObjectRes);
        String strRes = String.valueOf(jsonObjectRes);
        return strRes;
    }

    @PostMapping("/offer/token")
    public void addOrderWithOfferAndToken(@RequestParam Integer offerId,
                                          @RequestParam String token) throws IOException, URISyntaxException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(httpGetOfferUri + offerId);
        HttpResponse httpresponse = httpclient.execute(httpget);
        Scanner sc = new Scanner(httpresponse.getEntity().getContent());
        JSONObject jsonObjectOffer = new JSONObject(sc.nextLine());
        Integer offer = jsonObjectOffer.getInt("id");
        HttpGet httpgetToken = new HttpGet(httpGetCustomerUriId);
        URI uri = new URIBuilder(httpgetToken.getURI())
                .addParameter("token", token)
                .build();
        ((HttpRequestBase) httpgetToken).setURI(uri);
        httpresponse = httpclient.execute(httpgetToken);
        sc = new Scanner(httpresponse.getEntity().getContent());
        Integer customer = Integer.valueOf(sc.nextLine());
        CustomerOrder order = new CustomerOrder();
        order.setCustomerId(customer);
        order.setOfferId(offer);
        order.setDeliveryTime(new Date());
        customerOrderService.saveOrder(order);
    }
}
