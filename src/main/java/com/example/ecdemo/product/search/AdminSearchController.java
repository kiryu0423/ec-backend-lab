package com.example.ecdemo.product.search;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/search")
@RequiredArgsConstructor
public class AdminSearchController {

    private final ProductSearchService productSearchService;

    @PostMapping("/reindex")
    public void reindex() {
        productSearchService.reindexAll();
    }
}
