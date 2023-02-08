package com.blankerdog.carService.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name can't be empty")
    @Column(nullable = false, name = "name")
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @NotNull
    @ElementCollection
    @MapKeyColumn(name="work_type")
    @Column(name="price")
    @CollectionTable(name="item_prices", joinColumns=@JoinColumn(name="item_id"))
    private Map<WorkType, Integer> itemPrice;
}
