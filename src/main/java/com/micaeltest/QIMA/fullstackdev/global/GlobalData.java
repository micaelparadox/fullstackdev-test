package com.micaeltest.QIMA.fullstackdev.global;



import java.util.ArrayList;
import java.util.List;

import com.micaeltest.QIMA.fullstackdev.model.Product;

public class GlobalData {
    public static List<Product> cart;

    static {
        cart = new ArrayList<Product>();
    }
}