package com.app.inventory.model.key;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class SupplierContactKey implements Serializable {

    @Column(name = "sup_id")
    private int supplierId;

    @Column(name = "contact")
    private String contact;
}
