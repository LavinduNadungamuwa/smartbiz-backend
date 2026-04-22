package com.smartbiz.service.impl;

import com.smartbiz.dto.SaleRequestDto;
import com.smartbiz.dto.SaleResponseDto;
import com.smartbiz.entity.Business;
import com.smartbiz.entity.Customer;
import com.smartbiz.entity.Sale;
import com.smartbiz.entity.User;
import com.smartbiz.exception.AccessDeniedException;
import com.smartbiz.exception.ResourceNotFoundException;
import com.smartbiz.repository.CustomerRepository;
import com.smartbiz.repository.SaleRepository;
import com.smartbiz.repository.UserRepository;
import com.smartbiz.service.SaleService;
import com.smartbiz.util.SecurityHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public SaleServiceImpl(SaleRepository saleRepository,
                           CustomerRepository customerRepository,
                           UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SaleResponseDto saveSale(SaleRequestDto request) {
        User loggedInUser = SecurityHelper.getLoggedInUser(userRepository);
        Business business = loggedInUser.getBusiness();

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!customer.getBusiness().getId().equals(business.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        Sale sale = Sale.builder()
                .totalAmount(request.getTotalAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(request.getStatus())
                .business(business)
                .customer(customer)
                .user(loggedInUser)
                .build();

        Sale saved = saleRepository.save(sale);
        return mapToDto(saved);
    }

    @Override
    public List<SaleResponseDto> getAllSales() {
        User loggedInUser = SecurityHelper.getLoggedInUser(userRepository);
        Long businessId = loggedInUser.getBusiness().getId();

        return saleRepository.findByBusinessId(businessId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public SaleResponseDto getSaleById(Long id) {
        User loggedInUser = SecurityHelper.getLoggedInUser(userRepository);
        Long businessId = loggedInUser.getBusiness().getId();

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (!sale.getBusiness().getId().equals(businessId)) {
            throw new AccessDeniedException("Access denied");
        }

        return mapToDto(sale);
    }

    @Override
    public SaleResponseDto updateSale(Long id, SaleRequestDto request) {
        User loggedInUser = SecurityHelper.getLoggedInUser(userRepository);
        Business business = loggedInUser.getBusiness();
        Long businessId = business.getId();

        Sale existing = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (!existing.getBusiness().getId().equals(businessId)) {
            throw new AccessDeniedException("Access denied");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!customer.getBusiness().getId().equals(businessId)) {
            throw new AccessDeniedException("Access denied");
        }

        existing.setTotalAmount(request.getTotalAmount());
        existing.setPaymentMethod(request.getPaymentMethod());
        existing.setStatus(request.getStatus());
        existing.setCustomer(customer);
        existing.setUser(loggedInUser);
        existing.setBusiness(business);

        Sale updated = saleRepository.save(existing);
        return mapToDto(updated);
    }

    @Override
    public void deleteSale(Long id) {
        User loggedInUser = SecurityHelper.getLoggedInUser(userRepository);
        Long businessId = loggedInUser.getBusiness().getId();

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (!sale.getBusiness().getId().equals(businessId)) {
            throw new AccessDeniedException("Access denied");
        }

        saleRepository.deleteById(id);
    }

    private SaleResponseDto mapToDto(Sale sale) {
        return SaleResponseDto.builder()
                .id(sale.getId())
                .saleDate(sale.getSaleDate())
                .totalAmount(sale.getTotalAmount())
                .paymentMethod(sale.getPaymentMethod())
                .status(sale.getStatus())
                .businessId(sale.getBusiness().getId())
                .customerId(sale.getCustomer().getId())
                .userId(sale.getUser().getId())
                .build();
    }
}