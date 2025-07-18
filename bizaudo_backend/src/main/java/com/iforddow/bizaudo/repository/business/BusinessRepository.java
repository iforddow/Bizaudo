package com.iforddow.bizaudo.repository.business;

import com.iforddow.bizaudo.jpa.entity.business.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {

}
