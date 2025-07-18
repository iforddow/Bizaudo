package com.iforddow.bizaudo.bo.business;

import com.iforddow.bizaudo.jpa.entity.business.Business;
import com.iforddow.bizaudo.util.BizUtils;

public class BusinessBO {

    public boolean validateNewBusiness(Business business) throws Exception {

        if(BizUtils.isNullOrEmpty(business.getName())) {
            throw new Exception("Business name cannot be empty");
        }

        return false;
    }

}
