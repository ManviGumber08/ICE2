package ca.gbc.orderservice.service;

import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;

public interface OrderService {

    void placeOrder(OrderRequest orderRequest);
}
