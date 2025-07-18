package com.iforddow.bizaudo.controller.business;

import com.iforddow.bizaudo.request.business.CreateBusinessRequest;
import com.iforddow.bizaudo.service.business.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/business")
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String,Object>> createBusiness(@RequestBody CreateBusinessRequest createBusinessRequest) {
        return businessService.createBusiness(createBusinessRequest);
    }

}
