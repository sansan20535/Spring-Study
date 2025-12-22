package com.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantChainService {

    private final StoreRepository storeRepository;

    public long calculateRevenue(long restaurantId) {
        List<Calculable> stores = storeRepository.findByRestaurantId(restaurantId);
        long revenue = 0;
        for (Calculable store : stores) {
            revenue += store.calculateRevenue();
        }
        return revenue;
    }

    public long calculateProfit(long restaurantId) {
        List<Calculable> stores = storeRepository.findByRestaurantId(restaurantId);
        long cost = 0;
        for (Calculable store : stores) {
            cost += store.calculateProfit();
        }
        return calculateRevenue() - cost;
    }
}
