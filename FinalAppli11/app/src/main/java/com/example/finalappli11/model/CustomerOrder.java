package com.example.finalappli11.model;

public class CustomerOrder {

    private String clientName;
    private String items;
    private double total;

    public CustomerOrder(String clientName, String items, double total){
        this.clientName = clientName;
        this.items = items;
        this.total = total;
    }

    public String getClientName() {
        return clientName;
    }

    public String getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }
}
