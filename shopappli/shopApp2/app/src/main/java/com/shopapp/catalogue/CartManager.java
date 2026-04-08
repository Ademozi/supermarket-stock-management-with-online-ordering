package com.shopapp.catalogue;


import java.util.ArrayList;
import java.util.List;

public class CartManager {
        private static List<product>
                cartList = new ArrayList<>();
        public static void addItem(product p){
            cartList.add(p);
        }

        public static List<product>getCartList(){
            return cartList;
        }

        public static double calculateTotal(){
            double total = 0;
            for(product p: cartList){
              total +=  p.getPrice();
            }
            return total;
        }

    }

