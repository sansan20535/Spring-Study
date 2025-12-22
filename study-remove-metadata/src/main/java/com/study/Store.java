package com.study;

import java.util.List;

public class Store implements Calculable{

    private List<Calculable> orders;
    private long rentalFee;

    public long calculateRevenue() {
        long revenue = 0;
        for (Calculable order : orders) {
            revenue += order.calculateRevenue();
        }
        return revenue;
    }

    public long calculateProfit() {
        long income = 0;
        for (Calculable order : orders) {
            income += order.calculateProfit();
        }
        return income - rentalFee;
    }
}
