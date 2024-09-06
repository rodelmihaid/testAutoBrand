package com.example.webScrapping.controller;


import com.example.webScrapping.entity.Product;
import com.example.webScrapping.service.ScrapingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ScrapingController {

    @Autowired
    private ScrapingService scrapingService;

    @GetMapping("/api/product")
    public Product getProductData(@RequestParam String productCode) throws Exception {
        return scrapingService.scrapeProductData(productCode);
    }


}
