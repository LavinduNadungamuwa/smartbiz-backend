package com.smartbiz.controller;

import com.smartbiz.entity.Sale;
import com.smartbiz.service.SaleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public Sale createSale(@RequestBody Sale sale) {
        return saleService.saveSale(sale);
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    @GetMapping("/{id}")
    public Sale getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id);
    }

    @PutMapping("/{id}")
    public Sale updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        return saleService.updateSale(id, sale);
    }

    @DeleteMapping("/{id}")
    public void deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
    }
}