package com.example.ums.service;


import com.example.ums.entity.SalesReport;
import com.example.ums.repository.SalesReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesReportService {

    private final SalesReportRepository salesReportRepository;

    @Autowired
    public SalesReportService(SalesReportRepository salesReportRepository) {
        this.salesReportRepository = salesReportRepository;
    }

    // Get the best-selling products
    public List<SalesReport> getBestSellingProducts() {
        return salesReportRepository.findBestSellingProducts(); // Fetch best-selling products
    }

    // Get the monthly sales report
    public List<SalesReport> getMonthlySalesReport() {
        return salesReportRepository.findAll(); // Fetch monthly sales report (you can add logic to filter by date)
    }
}
