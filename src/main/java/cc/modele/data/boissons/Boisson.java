package cc.modele.data.boissons;

import cc.modele.data.comptes.Compte;

import java.time.LocalDateTime;

public class Boisson {

    private final LocalDateTime dateHeurePreparation;
    private final String type;
    private final Compte compte;
    private final Integer nbSucres;

    public Boisson(LocalDateTime dateHeurePreparation, Compte compte, String type, Integer nbSucres) {
        this.dateHeurePreparation = dateHeurePreparation;
        this.compte = compte;
        this.type = type;
        this.nbSucres = nbSucres;
    }

    public LocalDateTime getDateHeurePreparation() {
        return dateHeurePreparation;
    }

    public Compte getCompte() {
        return compte;
    }

    public String getType() {
        return type;
    }

    public Integer getNbSucres() {
        return nbSucres;
    }

}
