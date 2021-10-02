package com.app.inventory.model.key;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class CustomerContactKey implements Serializable {

    @Column(name = "cus_id")
    private int customerId;

    @Column(name = "contact")
    private String contact;
}
