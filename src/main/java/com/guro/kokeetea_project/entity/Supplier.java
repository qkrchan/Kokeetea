package com.guro.kokeetea_project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="supplier")
@Getter
@Setter
public class Supplier {
    @Id
    @Column(name = "supplier_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String location;

    @Column
    private String email;

    @Column
    private String material;

    @Column
    private String origin;

    @Column
    private Boolean isValid;

    @OneToMany(mappedBy = "supplier")
    private List<Ingredient> ingredient = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        if (!this.ingredient.contains(ingredient)) {
            this.ingredient.add(ingredient);
        }
        if (ingredient.getSupplier() != this) {
            ingredient.setSupplier(this);
        }
    }

}
