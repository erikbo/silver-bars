package com.silverbars.marketplace.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by eboh on 13/02/17.
 */
public class OrderTest {

    @Test
    public void compareTo_shouldPrioritizeOrderType_withSellOrdersBeforeBuyOrders(){
        Order order1 = new Order.Builder().orderType(OrderType.SELL).pricePerKg(10).quantity(10).userId("some-user-id").build();
        Order order2 = new Order.Builder().orderType(OrderType.BUY).pricePerKg(10).quantity(10).userId("some-user-id").build();

        assertThat(order1.compareTo(order2)).isEqualTo(-1);
        assertThat(order2.compareTo(order1)).isEqualTo(1);
    }

    @Test
    public void compareTo_shouldPrioritizePricePerKilo_whenOrderTypeIsEqual(){
        Order sellOrder1 = new Order.Builder().orderType(OrderType.SELL).pricePerKg(100).quantity(10).userId("some-user-id").build();
        Order sellOrder2 = new Order.Builder().orderType(OrderType.SELL).pricePerKg(10).quantity(10).userId("some-user-id").build();

        Order buyOrder1 = new Order.Builder().orderType(OrderType.BUY).pricePerKg(100).quantity(10).userId("some-user-id").build();
        Order buyOrder2 = new Order.Builder().orderType(OrderType.BUY).pricePerKg(10).quantity(10).userId("some-user-id").build();

        assertThat(sellOrder1.compareTo(sellOrder2)).isEqualTo(1);
        assertThat(sellOrder2.compareTo(sellOrder1)).isEqualTo(-1);

        assertThat(buyOrder1.compareTo(buyOrder2)).isEqualTo(-1);
        assertThat(buyOrder2.compareTo(buyOrder1)).isEqualTo(1);
    }

    @Test
    public void compareTo_shouldConsiderOrdersEqual_whenOrderTypeAndPricePerKiloIsEqual(){
        Order order1 = new Order.Builder().orderType(OrderType.SELL).pricePerKg(100).quantity(100).userId("some-user-id").build();
        Order order2 = new Order.Builder().orderType(OrderType.SELL).pricePerKg(100).quantity(10).userId("some-other-user-id").build();

        assertThat(order1.compareTo(order2)).isEqualTo(0);
        assertThat(order2.compareTo(order1)).isEqualTo(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compareTo_shouldThrowIllegalArgumentException_whenFirstOrderTypeIsNull(){
        Order order1 = new Order.Builder().pricePerKg(100).quantity(100).userId("some-user-id").build();
        Order order2 = new Order.Builder().orderType(OrderType.SELL).pricePerKg(100).quantity(10).userId("some-other-user-id").build();

        order1.compareTo(order2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compareTo_shouldThrowIllegalArgumentException_whenSecondOrderTypeIsNull(){
        Order order1 = new Order.Builder().orderType(OrderType.SELL).pricePerKg(100).quantity(100).userId("some-user-id").build();
        Order order2 = new Order.Builder().pricePerKg(100).quantity(10).userId("some-other-user-id").build();

        order1.compareTo(order2);
    }

}