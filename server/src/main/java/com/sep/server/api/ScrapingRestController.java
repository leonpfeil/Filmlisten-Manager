package com.sep.server.api;

import com.sep.server.model.ScrapingInstructions;
import com.sep.server.services.ScrapingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScrapingRestController {
    //von UserRestController
    private ScrapingService scrapingService;

    public ScrapingRestController(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    @PostMapping(path = "scraping/start")
    public ResponseEntity<String> StartScraping(@RequestBody ScrapingInstructions scrapingInstructions) {
        return scrapingService.startScraping(scrapingInstructions.getURL(), scrapingInstructions.getObergrenze());
    }
}
