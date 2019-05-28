package com.ibm.omsMigrationTool.order;

public class Order {
    
    private String orderHeaderKey;
    private String orderNo;
    
    private String status;
    
    
    public String getOrderHeaderKey() {
        return orderHeaderKey;
    }
    public void setOrderHeaderKey(String orderHeaderKey) {
        this.orderHeaderKey = orderHeaderKey;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Order:: OrderHeaderKey=" + this.orderHeaderKey + " orderNo=" + this.orderNo + " status=" + this.status;
    }
    
}