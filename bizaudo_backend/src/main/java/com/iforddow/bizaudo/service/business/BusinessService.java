package com.iforddow.bizaudo.service.business;

import com.iforddow.bizaudo.repository.business.BusinessRepository;
import com.iforddow.bizaudo.request.business.CreateBusinessRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;

    /**
    * A method to create a business within the application.
    *
    * @author IFD
    * @since 2025-07-18
    * */
    public ResponseEntity<Map<String,Object>> createBusiness(@RequestBody CreateBusinessRequest createBusinessRequest) {

        return ResponseEntity.ok(Map.of("businessName",createBusinessRequest.getBusinessName()));

    }

}
