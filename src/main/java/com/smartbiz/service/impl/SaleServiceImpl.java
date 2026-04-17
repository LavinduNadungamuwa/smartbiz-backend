package com.smartbiz.service.impl;

import com.smartbiz.entity.Business;
import com.smartbiz.entity.Customer;
import com.smartbiz.entity.Sale;
import com.smartbiz.entity.User;
import com.smartbiz.repository.BusinessRepository;
import com.smartbiz.repository.CustomerRepository;
import com.smartbiz.repository.SaleRepository;
import com.smartbiz.repository.UserRepository;
import com.smartbiz.service.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final BusinessRepository businessRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public SaleServiceImpl(SaleRepository saleRepository,
                           BusinessRepository businessRepository,
                           CustomerRepository customerRepository,
                           UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.businessRepository = businessRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Sale saveSale(Sale sale) {
        Long businessId = sale.getBusiness().getId();
        Long customerId = sale.getCustomer().getId();
        Long userId = sale.getUser().getId();

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        sale.setBusiness(business);
        sale.setCustomer(customer);
        sale.setUser(user);

        return saleRepository.save(sale);
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElse(null);
    }

    @Override
    public Sale updateSale(Long id, Sale sale) {
        Sale existingSale = saleRepository.findById(id).orElse(null);

        if (existingSale == null) {
            return null;
        }

        Long businessId = sale.getBusiness().getId();
        Long customerId = sale.getCustomer().getId();
        Long userId = sale.getUser().getId();

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingSale.setTotalAmount(sale.getTotalAmount());
        existingSale.setPaymentMethod(sale.getPaymentMethod());
        existingSale.setStatus(sale.getStatus());
        existingSale.setBusiness(business);
        existingSale.setCustomer(customer);
        existingSale.setUser(user);

        return saleRepository.save(existingSale);
    }

    @Override
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}