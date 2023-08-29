package com.guro.kokeetea_project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ingredient")
@Getter
@Setter
public class Ingredient {
    @Id
    @Column(name = "ingredient_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public void setCategory(Category category) {
        if (this.category != null) {
            this.category.getIngredient().remove(this);
        }
        this.category = category;
        if (category != null) {
            category.addIngredient(this);
        }
    }

    @Column
    private Boolean isValid;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public void setSupplier(Supplier supplier) {
        if (this.supplier != null) {
            this.supplier.getIngredient().remove(this);
        }
        this.supplier = supplier;
        if (supplier != null) {
            supplier.addIngredient(this);
        }
    }
}
