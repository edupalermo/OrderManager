package org.orderManager.service.helper;

import org.orderManager.entity.Item;
import org.orderManager.entity.Order;
import org.orderManager.entity.Transaction;
import org.orderManager.entity.enums.PaymentType;

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
