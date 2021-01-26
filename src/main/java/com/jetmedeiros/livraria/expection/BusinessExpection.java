package com.jetmedeiros.livraria.expection;

public class BusinessExpection extends RuntimeException {
    public BusinessExpection(String be) {
        super(be);
    }
}
