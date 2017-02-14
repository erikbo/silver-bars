package com.silverbars.marketplace.domain;

/**
 * Created by eboh on 11/02/17.
 */
public class Order implements Comparable<Order>{

    private String userId;
    private double quantity;
    private int pricePerKg;
    private OrderType orderType;

    public Order(String userId, double quantity, int pricePerKg, OrderType orderType) {
        this.userId = userId;
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getPricePerKg() {
        return pricePerKg;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (Double.compare(order.quantity, quantity) != 0) return false;
        if (pricePerKg != order.pricePerKg) return false;
        if (!userId.equals(order.userId)) return false;
        return orderType == order.orderType;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = userId.hashCode();
        temp = Double.doubleToLongBits(quantity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + pricePerKg;
        result = 31 * result + orderType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", quantity=" + quantity +
                ", pricePerKg=" + pricePerKg +
                ", orderType=" + orderType +
                '}';
    }

    @Override
    public int compareTo(Order order) {
        if(this.getOrderType() == null || order.getOrderType() == null){
           throw new IllegalArgumentException("Both orders need to have an order type.");
        }

        //First sort by order type to get sell orders on top
        if(this.getOrderType() == OrderType.SELL && order.getOrderType() != OrderType.SELL){
            return -1;
        } else if(this.getOrderType() == OrderType.BUY && order.getOrderType() != OrderType.BUY){
            return 1;
        }

        //Same order type
        int sellComparision = this.getPricePerKg() > order.getPricePerKg() ? 1 : this.pricePerKg < order.getPricePerKg() ? -1 : 0;

        //assume SELL order by default, invert if the order is of OrderType BUY
        return this.getOrderType() == OrderType.SELL ? sellComparision : -sellComparision;
    }

    public static class Builder {
        private String userId;
        private double quantity;
        private int pricePerKg;
        private OrderType orderType;

        public Builder() {
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder quantity(double quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder pricePerKg(int pricePerKg) {
            this.pricePerKg = pricePerKg;
            return this;
        }

        public Builder orderType(OrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public Order build() {
            return new Order(userId, quantity, pricePerKg, orderType);
        }
    }
}
