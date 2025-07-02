package com.example.ums.repository;

import com.example.ums.entity.Invoice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public InvoiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Invoice invoice) {
        String sql = "INSERT INTO invoices (order_id, total_amount, tax_amount, discount_amount, final_amount) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, invoice.getOrder().getId(), invoice.getTotalAmount(), invoice.getTaxAmount(),
                invoice.getDiscountAmount(), invoice.getFinalAmount());
    }
}

