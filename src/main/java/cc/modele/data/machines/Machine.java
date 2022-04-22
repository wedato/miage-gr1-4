package cc.modele.data.machines;

import cc.modele.data.boissons.Boisson;
import cc.modele.data.comptes.Compte;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Machine {

    private static Integer lastId = 0;

    private final Integer id;

    private final String nom;
    private final String typeBoissons;
    private final String salle;

    @JsonIgnore
    private final Collection<Boisson> boissonsPreparees;

    public Machine(String nom, String typeBoissons, String salle) {
        this.id = ++lastId;
        this.nom = nom;
        this.typeBoissons = typeBoissons;
        this.salle = salle;
        this.boissonsPreparees = new ArrayList<>();
    }

    public Boisson preparerBoisson(Compte compte, Integer nbSucres) {
        Boisson boisson = new Boisson(LocalDateTime.now(), compte, typeBoissons, nbSucres);
        boissonsPreparees.add(boisson);
        return boisson;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getTypeBoissons() {
        return typeBoissons;
    }

    public String getSalle() {
        return salle;
    }

    public Collection<Boisson> getBoissonsPreparees() {
        return boissonsPreparees;
    }

    public Integer getNbBoissonsPreparees() {
        return boissonsPreparees.size();
    }

}
