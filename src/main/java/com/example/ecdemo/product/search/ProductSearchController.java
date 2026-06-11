package com.example.ecdemo.product.search;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/search")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping
    public List<ProductDocument> searchProducts(
            @RequestParam String keyword
    ) {
        return productSearchService.search(keyword);
    }
}
