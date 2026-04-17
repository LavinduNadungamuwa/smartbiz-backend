package com.smartbiz.service;

import com.smartbiz.entity.Sale;

import java.util.List;

public interface SaleService {
    Sale saveSale(Sale sale);
    List<Sale> getAllSales();
    Sale getSaleById(Long id);
    Sale updateSale(Long id, Sale sale);
    void deleteSale(Long id);
}