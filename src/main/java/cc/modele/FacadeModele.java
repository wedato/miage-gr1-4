package cc.modele;

import cc.modele.data.boissons.Boisson;
import cc.modele.data.boissons.TypeBoisson;
import cc.modele.data.comptes.Compte;
import cc.modele.data.comptes.TypeCompte;
import cc.modele.data.machines.Machine;
import cc.modele.exceptions.*;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * Façade du modèle.
 */
@Component
public class FacadeModele {

    // TODO compléter la classe et les méthodes

    List<Compte> tousLesComptes = new ArrayList<>();
    List<Machine> listeMachine = new ArrayList<>();

    /**
     * Créer un nouveau compte de type "drinker".
     *
     * @param login    le login du compte à créer
     * @param password le password du compte à créer
     * @return le compte créé
     * @throws CompteDejaExistantException      si un compte existe déjà avec le même login (quel que soit son type)
     * @throws InformationsIncompletesException si les informations fournies sont incomplètes (login ou password vide)
     */
    public Compte creerCompteDrinker(String login, String password) throws InformationsIncompletesException, CompteDejaExistantException {

        if (login == null || password == null || login.isBlank() || password.isBlank())
            throw new InformationsIncompletesException();

        Optional<Compte> drinkerExistant = tousLesComptes.stream().filter(compte -> compte.getLogin().equals(login)).findAny();
        if (drinkerExistant.isPresent())
            throw new CompteDejaExistantException();

        Compte drinker = new Compte(login,password,"drinker");
        tousLesComptes.add(drinker);
        return drinker;
    }

    /**
     * Créer un nouveau compte de type "fabulous".
     *
     * @param login    le login du compte à créer
     * @param password le password du compte à créer
     * @return le compte créé
     * @throws CompteDejaExistantException      si un compte existe déjà avec le même login (quel que soit son type)
     * @throws InformationsIncompletesException si les informations fournies sont incomplètes (login ou password vide)
     */
    public Compte creerCompteFabulous(String login, String password) throws InformationsIncompletesException, CompteDejaExistantException {
        if (login == null || password == null || login.isBlank() || password.isBlank())
            throw new InformationsIncompletesException();

        Optional<Compte> fabulousExistant = tousLesComptes.stream().filter(compte -> compte.getLogin().equals(login)).findAny();
        if (fabulousExistant.isPresent())
            throw new CompteDejaExistantException();

        Compte fabulous = new Compte(login,password,"fabulous");
        tousLesComptes.add(fabulous);
        return fabulous;
    }

    /**
     * Retourne l'ensemble des comptes.
     *
     * @return une collection de tous les comptes
     */
    public Collection<Compte> getAllComptes() {

        return tousLesComptes;
    }

    /**
     * Retourne un compte existant, d'après son id.
     *
     * @param idCompte l'identifiant du compte recherché
     * @return le compte
     * @throws CompteInconnuException si aucun compte n'existe avec cet identifiant
     */
    public Compte getCompteById(Integer idCompte) throws CompteInconnuException {

        Optional<Compte> optionalCompte = tousLesComptes.stream().filter(compte -> compte.getId().equals(idCompte)).findAny();
        if (optionalCompte.isEmpty())
            throw new CompteInconnuException();
        return optionalCompte.get();
    }

    /**
     * Retourne un compte existant, d'après son login.
     *
     * @param login le login du compte recherché
     * @return le compte
     * @throws CompteInconnuException si aucun compte n'existe avec ce login
     */
    public Compte getCompteByLogin(String login) throws CompteInconnuException {
        Optional<Compte> optionalCompte = tousLesComptes.stream().filter(compte -> compte.getLogin().equals(login)).findAny();
        if (optionalCompte.isEmpty())
            throw new CompteInconnuException();
        return optionalCompte.get();
    }

    /**
     * Modifie le type d'un compte.
     *
     * @param idCompte    l'identifiant du compte à modifier
     * @param nouveauType le nouveau type du compte
     * @return le compte modifié
     * @throws CompteInconnuException     si aucun compte n'existe avec cet identifiant
     * @throws TypeCompteInconnuException si le nouveau type de compte n'existe pas
     */
    public Compte modifierTypeCompte(Integer idCompte, String nouveauType) throws TypeCompteInconnuException, CompteInconnuException {

        if (!TypeCompte.getAllTypes().contains(nouveauType))
            throw new TypeCompteInconnuException();

        Compte compte = getCompteById(idCompte);
        int index = tousLesComptes.indexOf(compte);
        compte.setType(nouveauType);
        tousLesComptes.set(index,compte);
        return compte;
    }

    /**
     * Ajoute une nouvelle machine.
     *
     * @param nom          le nom de la machine à ajouter
     * @param typeBoissons le type de boissons servi par la machine à ajouter
     * @param salle        la salle où se trouve la machine à ajouter
     * @return la machine ajoutée
     * @throws InformationsIncompletesException si les informations fournies sont incorrectes
     * @throws MachineDejaExistanteException    si une machine avec le même nom existe déjà
     * @throws TypeBoissonInconnuException      si le type de boisson n'existe pas
     * @throws FormatSalleIncorrectException    si le format du nom de la salle est incorrect (doit être de la forme "1 lettre suivie de 2 chiffres", p.ex. "A38")
     */
    public Machine ajouterMachine(String nom, String typeBoissons, String salle) throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        if (nom == null || typeBoissons == null || salle == null || salle.isBlank() || nom.isBlank() || typeBoissons.isBlank())
            throw new InformationsIncompletesException();
        if (!TypeBoisson.getAllTypes().contains(typeBoissons))
            throw new TypeBoissonInconnuException();
        char charArray[] = salle.toCharArray();
        if (Character.isDigit(charArray[0]) || salle.length() != 3)
            throw new FormatSalleIncorrectException();




        Optional<Machine> optionalMachine = listeMachine.stream().filter(machine -> machine.getNom().equals(nom)).findAny();
        if (optionalMachine.isPresent())
            throw new MachineDejaExistanteException();
        Machine machine = new Machine(nom, typeBoissons, salle);
        listeMachine.add(machine);
        return machine;
    }

    /**
     * Retourne l'ensemble des machines.
     *
     * @return une collection de toutes les machines
     */
    public Collection<Machine> getAllMachines() {

        return listeMachine;
    }

    /**
     * Retourne l'ensemble des machines présentes dans une salle.
     *
     * @param salle la salle dans laquelle se trouvent les machines
     * @return une collection des machines présentes dans la salle (éventuellement vide, si la salle ne contient aucune machine)
     * @throws FormatSalleIncorrectException si le format du nom de la salle est incorrect (doit être de la forme "1 lettre suivie de 2 chiffres", p.ex. "A38")
     */
    public Collection<Machine> getAllMachinesBySalle(String salle) throws FormatSalleIncorrectException {

        char charArray[] = salle.toCharArray();
        if (Character.isDigit(charArray[0]) || salle.length() != 3)
            throw new FormatSalleIncorrectException();

        List<Machine> machinesDeLaSalle = new ArrayList<>();
        for (Machine machine : listeMachine){
            if (machine.getSalle().equals(salle))
                machinesDeLaSalle.add(machine);

        }
        return machinesDeLaSalle;
    }

    /**
     * Récupère une machine existante, selon son id.
     *
     * @param idMachine l'identifiant de la machine recherchée
     * @return la machine
     * @throws MachineInconnueException si aucune machine n'existe avec cet identifiant
     */
    public Machine getMachineById(Integer idMachine) throws MachineInconnueException {

        Optional<Machine> optionalMachine = listeMachine.stream().filter(machine -> machine.getId().equals(idMachine)).findAny();
        if (optionalMachine.isEmpty())
            throw new MachineInconnueException();
        return optionalMachine.get();
    }

    /**
     * Récupère une machine existante, selon son nom.
     *
     * @param nom le nom de la machine recherchée
     * @return la machine
     * @throws MachineInconnueException si aucune machine n'existe avec ce nom
     */
    public Machine getMachineByNom(String nom) throws MachineInconnueException {
        Optional<Machine> optionalMachine = listeMachine.stream().filter(machine -> machine.getNom().equals(nom)).findAny();
        if (optionalMachine.isEmpty())
            throw new MachineInconnueException();
        return optionalMachine.get();
    }

    /**
     * Prépare une nouvelle boisson.
     *
     * @param idMachine   l'identifiant de la machine sur laquelle préparer la boisson
     * @param typeBoisson le type de boisson souhaité
     * @param idCompte    l'identifiant du compte demandant la préparation de la boisson
     * @param nbSucres    le nombre de sucres à ajouter dans la boisson (entier positif ou nul)
     * @return la boisson en cours de préparation
     * @throws MachineInconnueException    si aucune machine n'existe avec cet identifiant
     * @throws CompteInconnuException      si aucun compte n'existe avec cet identifiant
     * @throws TypeBoissonInconnuException si le type de boisson n'existe pas
     * @throws NbSucresIncorrectException  si le nombre de sucres est incorrect
     */
    public Boisson preparerBoisson(Integer idMachine, String typeBoisson, Integer idCompte, Integer nbSucres) throws CompteInconnuException, MachineInconnueException, TypeBoissonInconnuException, NbSucresIncorrectException {
       Compte compte = getCompteById(idCompte);
       Machine machine = getMachineById(idMachine);
       if (nbSucres < 0)
           throw new NbSucresIncorrectException();
       Boisson boisson = machine.preparerBoisson(compte,nbSucres);
       if (!TypeBoisson.getAllTypes().contains(machine.getTypeBoissons()))
           throw new TypeBoissonInconnuException();


       return boisson;
    }

}
