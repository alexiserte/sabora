package com.sabora.server.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "experiencia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Experience {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="tiempo")
    private Long time;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "dni", nullable = false)
    private Cliente client;

    @ManyToOne
    @JoinColumn(name = "id_escenario", referencedColumnName = "id", nullable = false)
    private Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "id_sonido", referencedColumnName = "id", nullable = false)
    private Sound sound;

    @ManyToOne
    @JoinColumn(name = "id_alimento", referencedColumnName = "id", nullable = false)
    private Food food;

    @OneToMany(mappedBy = "experience")
    private List<ExperienceSound> sounds = new ArrayList<>();

    @Column(name = "id_usuario", nullable = false)
    private Integer userId;
}
