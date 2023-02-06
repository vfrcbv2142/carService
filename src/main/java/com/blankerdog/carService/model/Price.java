package com.blankerdog.carService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "painting")
    private Integer painting;

    @Column(nullable = false, name = "preparing_aluminum")
    private Integer preparingAluminum;

    @Column(nullable = false, name = "preparing_plastic")
    private Integer preparingPlastic;

    @Column(nullable = false, name = "preparing_iron")
    private Integer preparingIron;

    @Column(nullable = false, name = "soldering")
    private Integer soldering;

    @Column(nullable = false, name = "disassembling")
    private Integer disassembling;

    @Column(nullable = false, name = "straightening")
    private Integer straightening;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;
}
