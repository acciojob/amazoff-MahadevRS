package com.driver;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Map<String,Order> orderMap=new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap=new HashMap<>();
    Map<String, List<String>> partnerOrderMap=new HashMap<>();
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
        List<String> orderList=new ArrayList<>();
        orderList.add(orderId);
        if(!partnerOrderMap.containsKey(partnerId)) partnerOrderMap.put(partnerId,orderList);
        else{
            orderList=partnerOrderMap.get(partnerId);
            orderList.add(orderId);
            partnerOrderMap.put(partnerId,orderList);
        }

        for(String partnerid: deliveryPartnerMap.keySet()){
            if(partnerid.equals(partnerId)){
                deliveryPartnerMap.get(partnerId).setNumberOfOrders(deliveryPartnerMap.get(partnerId).getNumberOfOrders()+1);
            }
        }
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> orders=new ArrayList<>();
        for(String orderid:orderMap.keySet()) orders.add(orderid);
        return orders;
    }

    public Integer getCountofUnassignedOrders() {
        int count=0;
        for(String orderid:orderMap.keySet()){
            if(!orderPartnerMap.containsKey(orderid)) count++;
        }
        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int timeinint=0;
        timeinint=60*Integer.parseInt(time.substring(0,2));
        timeinint=timeinint+Integer.parseInt(time.substring(3,5));
        int count=0;
        List<String> orderids=partnerOrderMap.get(partnerId);
        for(int i=0;i<orderids.size();i++){
            Order order=orderMap.get(orderids.get(i));
            if(order.getDeliveryTime()>timeinint) count++;
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String time="";

        int lasttime=0;
        List<String> orderids=partnerOrderMap.get(partnerId);
        for(int i=0;i<orderids.size();i++){
            Order order=orderMap.get(orderids.get(i));
            lasttime=Math.max(lasttime,order.getDeliveryTime());
        }

//        for(String orderid:orderPartnerMap.keySet()){
//            if((orderPartnerMap.get(orderid)).equals(partnerId)){
//                lasttime=Math.max(lasttime,orderMap.get(orderid).getDeliveryTime());
//            }
//        }
        if(lasttime/60<10) time+="0";
        time+=Integer.toString(lasttime/60);
        time+=":";
        if(lasttime%60<10) time+="0";
        time+=Integer.toString(lasttime%60);
        return time;
        
    }

    public void deletePartnerById(String partnerId) {
        List<String> orders=new ArrayList<>();
        for(String orderid:orderPartnerMap.keySet()){
            String partnerid=orderPartnerMap.get(orderid);
            if(partnerid.equals(partnerId)){
                orders.add(orderid);
            }
        }
        for(String orderid:orders){
            orderPartnerMap.remove(orderid);
        }

        partnerOrderMap.remove(partnerId);
        deliveryPartnerMap.remove(partnerId);

    }

    public void deleteOrderById(String orderId) {
        orderMap.remove(orderId);
        if(orderPartnerMap.containsKey(orderId)) {
            String partnerId=orderPartnerMap.get(orderId);
            List<String> orderids=partnerOrderMap.get(partnerId);
            orderids.remove(orderId);
            partnerOrderMap.put(partnerId,orderids);
            orderPartnerMap.remove(orderId);

        }

    }
}
