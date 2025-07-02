package com.example.ums.api;



import com.example.ums.entity.Invoice;
import com.example.ums.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceApi {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceApi(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/generate")
    public Invoice generateInvoice(@PathVariable Long orderId) {
        return invoiceService.generateInvoice(orderId);
    }
}
