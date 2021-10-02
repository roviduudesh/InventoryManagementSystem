package com.app.inventory.model;

import com.app.inventory.model.key.CustomerContactKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cus_contact")
public class CustomerContact {

    @EmbeddedId
    private CustomerContactKey customerContactKey;
}
