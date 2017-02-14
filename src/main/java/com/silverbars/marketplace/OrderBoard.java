package com.silverbars.marketplace;

import com.silverbars.marketplace.domain.Order;
import com.silverbars.marketplace.domain.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

/**
 * Created by eboh on 11/02/17.
 */
public class OrderBoard {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBoard.class);

    private final List<Order> registeredOrders = new ArrayList<>();

    public List<String> getSummaryInformation() {
        Map<OrderType, List<Order>> ordersByOrderType = registeredOrders.stream().collect(groupingBy(Order::getOrderType));

        return Stream.concat(
                collectOrdersForOrderType(ordersByOrderType.getOrDefault(OrderType.SELL, emptyList())),
                collectOrdersForOrderType(ordersByOrderType.getOrDefault(OrderType.BUY, emptyList())))
                .sorted()
                .map(this::formatOrderOutput)
                .collect(toList());
    }

    public void registerOrder(Order order) {
        LOGGER.info("Trying to register order: {}", order);
        if (!validOrder(order)) {
            throw new IllegalArgumentException("The order is not valid for registration.");
        }
        registeredOrders.add(order);
    }

    public void cancelOrder(Order order) {
        LOGGER.info("Trying to cancel order: {}", order);
        if (!registeredOrders.contains(order)) {
            throw new NoSuchElementException("This order is currently not available for cancellation.");
        }
        registeredOrders.remove(order);
    }

    private String formatOrderOutput(Order order) {
        //UK Locale is used to force dot-separation as this is locale specific.
        return String.format(Locale.UK, "%s: %.1f kg for £%d", order.getOrderType(), order.getQuantity(), order.getPricePerKg());
    }

    private Stream<Order> collectOrdersForOrderType(List<Order> ordersByOrderType) {
        return ordersByOrderType.stream()
                .collect(toMap(Order::getPricePerKg, Function.identity(), this::combineToDisplayOrder))
                .values().stream();
    }

    private Order combineToDisplayOrder(Order firstOrder, Order secondOrder) {
        return new Order.Builder()
                .pricePerKg(firstOrder.getPricePerKg())
                .orderType(firstOrder.getOrderType())
                .quantity(firstOrder.getQuantity() + secondOrder.getQuantity())
                .build();
    }

    private boolean validOrder(Order order) {
        return order != null && order.getOrderType() != null && validUserId(order) && validOrderPriceAndQuantity(order);
    }

    private boolean validOrderPriceAndQuantity(Order order) {
        return !(order.getPricePerKg() <= 0 || order.getQuantity() <= 0);
    }

    private boolean validUserId(Order order) {
        return order.getUserId() != null && !order.getUserId().isEmpty();
    }
}
