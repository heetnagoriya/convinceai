package com.convinceai.persuasionservice.controller;

import com.convinceai.persuasionservice.dto.PersuasionRequest;
import com.convinceai.persuasionservice.dto.PersuasionResponse;
import com.convinceai.persuasionservice.service.PersuasionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persuade")
public class PersuasionController {

    private final PersuasionService persuasionService;

    public PersuasionController(PersuasionService persuasionService) {
        this.persuasionService = persuasionService;
    }

    @PostMapping
    public PersuasionResponse getPersuasionMessage(@RequestBody PersuasionRequest request) {
        return persuasionService.getPersuasionMessage(request);
    }
}