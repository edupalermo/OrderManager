package org.orderManager.service.helper;

import org.orderManager.entity.Item;
import org.orderManager.entity.Order;
import org.orderManager.entity.Transaction;
import org.orderManager.entity.enums.OrderStatus;
import org.orderManager.entity.enums.PaymentType;

public class OrderHelper {

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
    
    public static void treatOrderStatus(Order order) {
        // If the Order have a price to pay then it mus be Entered
        if (order.getPrice() > 0) {
        	order.setStatus(OrderStatus.ENTERED);
        }
        
        if ((order.getPrice() == 0) && (order.getItems().size() > 0)) {
        	order.setStatus(OrderStatus.PAID);
        }
        
    }
    
}
