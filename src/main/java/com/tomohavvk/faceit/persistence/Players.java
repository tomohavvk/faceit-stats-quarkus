package com.tomohavvk.faceit.persistence;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Players extends PanacheEntityBase {

    @Id
    public String id;
    public String nickname;

}
