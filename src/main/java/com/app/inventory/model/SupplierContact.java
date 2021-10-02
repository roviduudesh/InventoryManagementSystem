package com.app.inventory.model;

import com.app.inventory.model.key.SupplierContactKey;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sup_contact")
public class SupplierContact {

    @EmbeddedId
    private SupplierContactKey supConKey;
}
