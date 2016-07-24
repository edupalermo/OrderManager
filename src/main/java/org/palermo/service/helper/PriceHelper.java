package org.palermo.service.helper;

import org.palermo.entity.Item;
import org.palermo.entity.Order;
import org.palermo.entity.Transaction;
import org.palermo.entity.enums.PaymentType;

public class PriceHelper {

    public static void updateOrderPrice(Order order) {
        int total = 0;

        for (Item item : order.getItems()) {
            total += item.getUnitPrice();
        }
        
        for (Transaction transaction: order.getTransactions()) {
            if (transaction.getPaymentType() == PaymentType.PAYMENT) {
                total -= transaction.getAmount();
            }
            
        }
        order.setPrice(total);

    }
    
}
