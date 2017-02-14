package com.silverbars.marketplace;

import com.silverbars.marketplace.domain.Order;
import com.silverbars.marketplace.domain.OrderType;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by eboh on 11/02/17.
 */
@RunWith(Theories.class)
public class OrderBoardTest {

    private OrderBoard orderBoard;

    private Order defaultOrder = new Order.Builder()
            .userId("some-user")
            .orderType(OrderType.SELL)
            .pricePerKg(10)
            .quantity(10.00)
            .build();

    @Before
    public void setUp(){
        orderBoard = new OrderBoard();
    }

    @Test
    public void registerOrder_shouldAddAnOrderToTheOrderBoard() {
        orderBoard.registerOrder(defaultOrder);

        assertThat(orderBoard.getSummaryInformation()).hasSize(1);
    }

    @Theory
    @Test(expected = IllegalArgumentException.class)
    public void registerOrder_shouldValidateInput(Order order) {
        orderBoard.registerOrder(order);
    }

    @DataPoints
    public static List<Order> failingRegistrationOrders() {
        Order orderWithoutUserId = new Order.Builder().orderType(OrderType.BUY).pricePerKg(100).quantity(10.0).build();
        Order orderWithEmptyUserId = new Order.Builder().userId("").orderType(OrderType.BUY).pricePerKg(100).quantity(10.0).build();
        Order orderWithoutOrderType = new Order.Builder().userId("some-user").pricePerKg(100).quantity(10.0).build();
        Order orderWithoutPricePerKg = new Order.Builder().userId("some-user").orderType(OrderType.BUY).quantity(10.0).build();
        Order orderWithNegativePricePerKg = new Order.Builder().userId("some-user").orderType(OrderType.BUY).pricePerKg(-10).quantity(10.0).build();
        Order orderWithZeroQuantity = new Order.Builder().userId("some-user").orderType(OrderType.BUY).pricePerKg(100).quantity(0.0).build();
        Order orderWithNegativeQuantity = new Order.Builder().userId("some-user").orderType(OrderType.BUY).pricePerKg(100).quantity(-1.00).build();

        return asList(orderWithoutUserId, orderWithEmptyUserId, orderWithoutOrderType, orderWithoutPricePerKg,
                orderWithNegativePricePerKg, orderWithZeroQuantity, orderWithNegativeQuantity, null);
    }

    @Test
    public void cancelOrder_shouldCancelOrder(){
        orderBoard.registerOrder(defaultOrder);

        assertThat(orderBoard.getSummaryInformation()).hasSize(1);

        orderBoard.cancelOrder(defaultOrder);

        assertThat(orderBoard.getSummaryInformation()).hasSize(0);
    }

    @Test(expected = NoSuchElementException.class)
    public void cancelOrder_ShouldNotBeAbleToCancelOrder_ifNoOrderExists(){
        orderBoard.cancelOrder(defaultOrder);
    }

    @Test(expected = NoSuchElementException.class)
    public void cancelOrder_shouldThrowNoSuchElementException_ifInputIsNull() {
        orderBoard.cancelOrder(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void cancelOrder_ShouldNotBeAbleToCancelOrder_ifNoEqualOrderExists(){
        orderBoard.registerOrder(defaultOrder);
        assertThat(orderBoard.getSummaryInformation()).hasSize(1);

        Order notExistingOrder = new Order.Builder().orderType(OrderType.SELL).pricePerKg(100).quantity(10.000).userId("some-other-user-id").build();
        orderBoard.cancelOrder(notExistingOrder);
    }

    @Test
    public void orderSummary_shouldReturnOrdersInCorrectOrder() {
        Order sellOrder2 = new Order.Builder().userId("some-user").quantity(10.0).pricePerKg(200).orderType(OrderType.SELL).build();
        Order buyOrder2 = new Order.Builder().userId("some-user").quantity(10.0).pricePerKg(200).orderType(OrderType.BUY).build();
        Order sellOrder1 = new Order.Builder().userId("some-user").quantity(10.0).pricePerKg(100).orderType(OrderType.SELL).build();
        Order buyOrder1 = new Order.Builder().userId("some-user").quantity(10.0).pricePerKg(100).orderType(OrderType.BUY).build();

        orderBoard.registerOrder(sellOrder1);
        orderBoard.registerOrder(sellOrder2);
        orderBoard.registerOrder(buyOrder1);
        orderBoard.registerOrder(buyOrder2);

        String[] expectedOrders = new String[]{"SELL: 10.0 kg for £100", "SELL: 10.0 kg for £200", "BUY: 10.0 kg for £200", "BUY: 10.0 kg for £100"};

        List<String> summaryInformation = orderBoard.getSummaryInformation();
        assertThat(summaryInformation)
                .hasSize(4)
                .containsExactly(expectedOrders);
    }

    @Test
    public void orderSummary_shouldMergeOrders_withSamePriceAndOrderType() {
        Order sellOrder2 = new Order.Builder().userId("some-user").quantity(11.4).pricePerKg(200).orderType(OrderType.SELL).build();
        Order buyOrder3 = new Order.Builder().userId("some-user").quantity(10.0).pricePerKg(100).orderType(OrderType.BUY).build();
        Order buyOrder2 = new Order.Builder().userId("some-user").quantity(20.0).pricePerKg(100).orderType(OrderType.BUY).build();
        Order buyOrder1 = new Order.Builder().userId("some-user").quantity(10.0).pricePerKg(100).orderType(OrderType.BUY).build();
        Order sellOrder1 = new Order.Builder().userId("some-user").quantity(22.35).pricePerKg(200).orderType(OrderType.SELL).build();

        orderBoard.registerOrder(sellOrder1);
        orderBoard.registerOrder(sellOrder2);
        orderBoard.registerOrder(buyOrder1);
        orderBoard.registerOrder(buyOrder2);
        orderBoard.registerOrder(buyOrder3);

        String[] expectedOrders = new String[]{"SELL: 33.8 kg for £200", "BUY: 40.0 kg for £100"};

        List<String> summaryInformation = orderBoard.getSummaryInformation();
        assertThat(summaryInformation)
                .hasSize(2)
                .containsExactly(expectedOrders);
    }

    @Test
    public void orderSumarry_shouldReturnAnEmptyList_whenThereAreNoOrders(){
        assertThat(orderBoard.getSummaryInformation()).isEmpty();
    }

}