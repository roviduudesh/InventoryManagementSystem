package com.app.inventory.repository;

import com.app.inventory.model.CustomerContact;
import com.app.inventory.model.key.CustomerContactKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerContactRepository extends JpaRepository<CustomerContact, CustomerContactKey> {
}
