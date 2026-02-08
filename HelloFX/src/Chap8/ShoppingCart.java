package Chap8;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Double> items = new ArrayList<>();

    public void addItem(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        items.add(price);
    }

    public int getItemCount() {
        return items.size();
    }

    public double calculateTotal() {
        double sum = 0.0;
        for (Double price : items) {
            sum += price;
        }
        return sum;
    }

    // Business Logic:
    // If total > 100, apply 10% discount.
    // Otherwise, 0% discount.
    public double applyDiscount() {
        double total = calculateTotal();
        if (total > 100) {
            return total * 0.90; // 10% off
        }
        return total;
    }
}
