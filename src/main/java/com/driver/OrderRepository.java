package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Map<String,Order> orderMap=new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap=new HashMap<>();
    Map<String, List<Order>> partnerOrderMap=new HashMap<>();
    Map<String,String> orderPartnerMap=new HashMap<>();

    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
        deliveryPartnerMap.put(partnerId,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderPartnerMap.put(orderId,partnerId);
        for(String partnerid: deliveryPartnerMap.keySet()){
            if(partnerid.equals(partnerId)){
                deliveryPartnerMap.get(partnerId).setNumberOfOrders(deliveryPartnerMap.get(partnerId).getNumberOfOrders()+1);
            }
        }
    }

    public Order getOrderByIdd(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        int no=0;
        for(String partnerid: orderPartnerMap.keySet()){
            if(partnerid.equals(partnerId)){
                no++;
            }
        }
        return no;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders=new ArrayList<>();
        for(String partnerid:orderPartnerMap.keySet()){
            if(partnerid.equals(partnerId)){
                orders.add(orderPartnerMap.get(partnerId));
            }
        }
        return orders;
    }

    public List<String> getAllOrders() {
        List<String> orders=new ArrayList<>();
        for(String orderid:orderMap.keySet()) orders.add(orderid);
        return orders;
    }
}