package com.shopapp.catalogue;

public class product {
    private String title;
    private String price;
    private String category;


    public product(String title, String price, String category){
        this.title = title;
        this.price = price;
        this.category = category;

    }

    public String getTitle(){
        return title;
    }

    public String getPrice(){
        return price;
    }

    public String getCategory(){
        return category;
    }


}
