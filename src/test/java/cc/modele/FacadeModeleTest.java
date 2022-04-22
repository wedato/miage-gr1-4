package cc.modele;

import cc.modele.data.boissons.Boisson;
import cc.modele.data.boissons.TypeBoisson;
import cc.modele.data.comptes.Compte;
import cc.modele.data.comptes.TypeCompte;
import cc.modele.data.machines.Machine;
import cc.modele.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;


class FacadeModeleTest {

    private FacadeModele classeTestee;

    @BeforeEach
    void setUp() {
        classeTestee = new FacadeModele();
    }

    @Test
    void creerCompteDrinker_OK() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String login = "foo";
        String password = "bar";

        // ACT
        Compte compte = classeTestee.creerCompteDrinker(login, password);

        // ASSERT
        Assertions.assertNotNull(compte);
        Assertions.assertEquals(TypeCompte.DRINKER, compte.getType());
        Assertions.assertEquals(login, compte.getLogin());
    }

    @Test
    void creerCompteDrinker_KO_compteDejaExistant() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String login = "foo";
        String password1 = "bar1";
        String password2 = "bar2";

        // ACT
        classeTestee.creerCompteDrinker(login, password1);

        // ACT & ASSERT
        Assertions.assertThrows(CompteDejaExistantException.class, () -> classeTestee.creerCompteDrinker(login, password2));
    }

    @Test
    void creerCompteDrinker_KO_loginVide() {
        // ARRANGE
        String login = "   ";
        String password = "bar";

        // ACT & ASSERT
        Assertions.assertThrows(InformationsIncompletesException.class, () -> classeTestee.creerCompteDrinker(login, password));
    }

    @Test
    void creerCompteDrinker_KO_passwordVide() {
        // ARRANGE
        String login = "foo";
        String password = "   ";

        // ACT & ASSERT
        Assertions.assertThrows(InformationsIncompletesException.class, () -> classeTestee.creerCompteDrinker(login, password));
    }

    @Test
    void creerCompteFabulous_OK() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String login = "foo";
        String password = "bar";

        // ACT
        Compte compte = classeTestee.creerCompteFabulous(login, password);

        // ASSERT
        Assertions.assertNotNull(compte);
        Assertions.assertEquals(TypeCompte.FABULOUS, compte.getType());
        Assertions.assertEquals(login, compte.getLogin());
    }

    @Test
    void creerCompteFabulous_KO_compteDejaExistant() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String login = "foo";
        String password1 = "bar1";
        String password2 = "bar2";

        // ACT
        classeTestee.creerCompteDrinker(login, password1);

        // ACT & ASSERT
        Assertions.assertThrows(CompteDejaExistantException.class, () -> classeTestee.creerCompteFabulous(login, password2));
    }

    @Test
    void creerCompteFabulous_KO_loginVide() {
        // ARRANGE
        String login = "   ";
        String password = "bar";

        // ACT & ASSERT
        Assertions.assertThrows(InformationsIncompletesException.class, () -> classeTestee.creerCompteFabulous(login, password));
    }

    @Test
    void creerCompteFabulous_KO_passwordVide() {
        // ARRANGE
        String login = "foo";
        String password = "   ";

        // ACT & ASSERT
        Assertions.assertThrows(InformationsIncompletesException.class, () -> classeTestee.creerCompteFabulous(login, password));
    }

    @Test
    void getAllComptes_OK() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String loginDummy = "dummy";
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteFabulous(login2, password2);

        // ACT
        Collection<Compte> comptes = classeTestee.getAllComptes();

        // ASSERT
        Assertions.assertNotNull(comptes);
        Assertions.assertEquals(2, comptes.size());
        Assertions.assertTrue(comptes.stream().anyMatch(c -> login1.equals(c.getLogin())));
        Assertions.assertTrue(comptes.stream().anyMatch(c -> login2.equals(c.getLogin())));
        Assertions.assertTrue(comptes.stream().noneMatch(c -> loginDummy.equals(c.getLogin())));
    }

    @Test
    void getCompteById_OK() throws InformationsIncompletesException, CompteDejaExistantException, CompteInconnuException {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        Compte compte1 = classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteFabulous(login2, password2);

        // ACT
        Compte compte = classeTestee.getCompteById(compte1.getId());

        // ASSERT
        Assertions.assertNotNull(compte);
        Assertions.assertEquals(compte1.getId(), compte.getId());
    }

    @Test
    void getCompteById_KO_compteInconnu() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteFabulous(login2, password2);

        Integer idDummy = -1;

        // ACT & ASSERT
        Assertions.assertThrows(CompteInconnuException.class, () -> classeTestee.getCompteById(idDummy));
    }

    @Test
    void getCompteByLogin_OK() throws InformationsIncompletesException, CompteDejaExistantException, CompteInconnuException {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteFabulous(login2, password2);

        // ACT
        Compte compte = classeTestee.getCompteByLogin(login1);

        // ASSERT
        Assertions.assertNotNull(compte);
        Assertions.assertEquals(login1, compte.getLogin());
    }

    @Test
    void getCompteByLogin_KO_compteInconnu() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String loginDummy = "dummy";
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteFabulous(login2, password2);

        // ACT & ASSERT
        Assertions.assertThrows(CompteInconnuException.class, () -> classeTestee.getCompteByLogin(loginDummy));
    }

    @Test
    void modifierTypeCompte_OK_fabulous() throws InformationsIncompletesException, CompteDejaExistantException, TypeCompteInconnuException, CompteInconnuException {
        // ARRANGE
        String nouveauType = TypeCompte.FABULOUS;
        String login1 = "foo1";
        String password1 = "bar1";
        Compte compte1 = classeTestee.creerCompteDrinker(login1, password1);

        // ACT
        Compte compte = classeTestee.modifierTypeCompte(compte1.getId(), nouveauType);

        // ASSERT
        Assertions.assertNotNull(compte);
        Assertions.assertEquals(nouveauType, compte.getType());
    }

    @Test
    void modifierTypeCompte_OK_drinker() throws InformationsIncompletesException, CompteDejaExistantException, TypeCompteInconnuException, CompteInconnuException {
        // ARRANGE
        String nouveauType = TypeCompte.DRINKER;
        String login1 = "foo1";
        String password1 = "bar1";
        Compte compte1 = classeTestee.creerCompteDrinker(login1, password1);

        // ACT
        Compte compte = classeTestee.modifierTypeCompte(compte1.getId(), nouveauType);

        // ASSERT
        Assertions.assertNotNull(compte);
        Assertions.assertEquals(nouveauType, compte.getType());
    }

    @Test
    void modifierTypeCompte_KO_compteInconnu() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String nouveauType = TypeCompte.DRINKER;
        String login = "foo";
        String password = "bar";
        classeTestee.creerCompteDrinker(login, password);

        Integer idDummy = -1;

        // ACT & ASSERT
        Assertions.assertThrows(CompteInconnuException.class, () -> classeTestee.modifierTypeCompte(idDummy, nouveauType));
    }

    @Test
    void modifierTypeCompte_typeCompteInconnu() throws InformationsIncompletesException, CompteDejaExistantException {
        // ARRANGE
        String nouveauTypeDummy = "dummy";
        String login = "foo";
        String password = "bar";
        Compte compte = classeTestee.creerCompteDrinker(login, password);

        // ACT & ASSERT
        Assertions.assertThrows(TypeCompteInconnuException.class, () -> classeTestee.modifierTypeCompte(compte.getId(), nouveauTypeDummy));
    }

    @Test
    void ajouterMachine_OK() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "A38";

        // ACT
        Machine machine = classeTestee.ajouterMachine(nom, typeBoissons, salle);

        // ASSERT
        Assertions.assertNotNull(machine);
        Assertions.assertEquals(nom, machine.getNom());
        Assertions.assertEquals(typeBoissons, machine.getTypeBoissons());
        Assertions.assertEquals(salle, machine.getSalle());
    }

    @Test
    void ajouterMachine_KO_nomVide() {
        // ARRANGE
        String nom = "   ";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "A38";

        // ACT & ASSERT
        Assertions.assertThrows(InformationsIncompletesException.class, () -> classeTestee.ajouterMachine(nom, typeBoissons, salle));
    }

    @Test
    void ajouterMachine_KO_typeBoissonsVide() {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = "   ";
        String salle = "A38";

        // ACT & ASSERT
        Assertions.assertThrows(InformationsIncompletesException.class, () -> classeTestee.ajouterMachine(nom, typeBoissons, salle));
    }

    @Test
    void ajouterMachine_KO_salleVide() {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "   ";

        // ACT & ASSERT
        Assertions.assertThrows(InformationsIncompletesException.class, () -> classeTestee.ajouterMachine(nom, typeBoissons, salle));
    }

    @Test
    void ajouterMachine_KO_machineDejaExistante() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "A38";
        classeTestee.ajouterMachine(nom, typeBoissons, salle);

        // ACT & ASSERT
        Assertions.assertThrows(MachineDejaExistanteException.class, () -> classeTestee.ajouterMachine(nom, typeBoissons, salle));
    }

    @Test
    void ajouterMachine_KO_typeBoissonInconnu() {
        // ARRANGE
        String nom = "coffee";
        String typeBoissonsDummy = "dummy";
        String salle = "A38";

        // ACT & ASSERT
        Assertions.assertThrows(TypeBoissonInconnuException.class, () -> classeTestee.ajouterMachine(nom, typeBoissonsDummy, salle));
    }

    @Test
    void ajouterMachine_KO_formatSalleIncorrect() {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "420";

        // ACT & ASSERT
        Assertions.assertThrows(FormatSalleIncorrectException.class, () -> classeTestee.ajouterMachine(nom, typeBoissons, salle));
    }

    @Test
    void getAllMachines_OK() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String nomDummy = "dummy";
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT
        Collection<Machine> machines = classeTestee.getAllMachines();

        // ASSERT
        Assertions.assertNotNull(machines);
        Assertions.assertEquals(2, machines.size());
        Assertions.assertTrue(machines.stream().anyMatch(m -> nom1.equals(m.getNom())));
        Assertions.assertTrue(machines.stream().anyMatch(m -> nom2.equals(m.getNom())));
        Assertions.assertTrue(machines.stream().noneMatch(m -> nomDummy.equals(m.getNom())));
    }

    @Test
    void getAllMachinesBySalle_OK_salleAvecMachines() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT
        Collection<Machine> machines = classeTestee.getAllMachinesBySalle(salle1);

        // ASSERT
        Assertions.assertNotNull(machines);
        Assertions.assertEquals(1, machines.size());
        Assertions.assertTrue(machines.stream().anyMatch(m -> nom1.equals(m.getNom())));
        Assertions.assertTrue(machines.stream().noneMatch(m -> nom2.equals(m.getNom())));
    }

    @Test
    void getAllMachinesBySalle_OK_salleSansMachine() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String salle = "Z00";
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT
        Collection<Machine> machines = classeTestee.getAllMachinesBySalle(salle);

        // ASSERT
        Assertions.assertNotNull(machines);
        Assertions.assertTrue(machines.isEmpty());
    }

    @Test
    void getAllMachinesBySalle_KO_formatSalleIncorrect() {
        // ARRANGE
        String salleDummy = "420";

        // ACT & ASSERT
        Assertions.assertThrows(FormatSalleIncorrectException.class, () -> classeTestee.getAllMachinesBySalle(salleDummy));
    }

    @Test
    void getMachineById_OK() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException, MachineInconnueException {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT
        Machine machine = classeTestee.getMachineById(machine1.getId());

        // ASSERT
        Assertions.assertNotNull(machine);
        Assertions.assertEquals(machine1.getId(), machine1.getId());
    }

    @Test
    void getMachineById_KO_machineInconnue() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        Integer idDummy = -1;

        // ACT & ASSERT
        Assertions.assertThrows(MachineInconnueException.class, () -> classeTestee.getMachineById(idDummy));
    }

    @Test
    void getMachineByNom_OK() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException, MachineInconnueException {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT
        Machine machine = classeTestee.getMachineByNom(nom1);

        // ASSERT
        Assertions.assertNotNull(machine);
        Assertions.assertEquals(nom1, machine.getNom());
    }

    @Test
    void getMachineByNom_KO_machineInconnue() throws MachineDejaExistanteException, InformationsIncompletesException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String nomDummy = "dummy";
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        Assertions.assertThrows(MachineInconnueException.class, () -> classeTestee.getMachineByNom(nomDummy));
    }

    @Test
    void preparerBoisson_OK() throws InformationsIncompletesException, CompteDejaExistantException, MachineDejaExistanteException, FormatSalleIncorrectException, NbSucresIncorrectException, TypeBoissonInconnuException, CompteInconnuException, MachineInconnueException {
        // ARRANGE
        String typeBoisson = TypeBoisson.CAFE;
        Integer nbSucres = 1;
        // Comptes
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        Compte compte1 = classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteFabulous(login2, password2);
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT
        Boisson boisson = classeTestee.preparerBoisson(machine1.getId(), typeBoisson, compte1.getId(), nbSucres);

        // ASSERT
        // Boisson
        Assertions.assertNotNull(boisson);
        Assertions.assertEquals(typeBoisson, boisson.getType());
        // Machine
        Assertions.assertEquals(1, machine1.getNbBoissonsPreparees());
        // Compte
        Assertions.assertNotNull(boisson.getCompte());
        Assertions.assertEquals(login1, boisson.getCompte().getLogin());
        Assertions.assertEquals(1, compte1.getNbBoissonsBues());
    }

    @Test
    void preparerBoisson_KO_machineInconnue() throws InformationsIncompletesException, CompteDejaExistantException, MachineDejaExistanteException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String typeBoisson = TypeBoisson.CAFE;
        Integer nbSucres = 1;
        // Comptes
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        Compte compte1 = classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteFabulous(login2, password2);
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        Integer idDummy = -1;

        // ACT & ASSERT
        Assertions.assertThrows(MachineInconnueException.class, () -> classeTestee.preparerBoisson(idDummy, typeBoisson, compte1.getId(), nbSucres));
    }

    @Test
    void preparerBoisson_KO_compteInconnu() throws InformationsIncompletesException, CompteDejaExistantException, MachineDejaExistanteException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String typeBoisson = TypeBoisson.CAFE;
        Integer nbSucres = 1;
        // Comptes
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteFabulous(login2, password2);
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        Integer idDummy = -1;

        // ACT & ASSERT
        Assertions.assertThrows(CompteInconnuException.class, () -> classeTestee.preparerBoisson(machine1.getId(), typeBoisson, idDummy, nbSucres));
    }

    @Test
    void preparerBoisson_KO_typeBoissonInconnu() throws InformationsIncompletesException, CompteDejaExistantException, MachineDejaExistanteException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String typeBoissonDummy = "dummy";
        Integer nbSucres = 1;
        // Comptes
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        Compte compte1 = classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteDrinker(login2, password2);
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        Assertions.assertThrows(TypeBoissonInconnuException.class, () -> classeTestee.preparerBoisson(machine1.getId(), typeBoissonDummy, compte1.getId(), nbSucres));
    }

    @Test
    void preparerBoisson_KO_nbSucresIncorrect() throws InformationsIncompletesException, CompteDejaExistantException, MachineDejaExistanteException, TypeBoissonInconnuException, FormatSalleIncorrectException {
        // ARRANGE
        String typeBoisson = TypeBoisson.CAFE;
        Integer nbSucres = -1;
        // Comptes
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        Compte compte1 = classeTestee.creerCompteDrinker(login1, password1);
        classeTestee.creerCompteDrinker(login2, password2);
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = classeTestee.ajouterMachine(nom1, typeBoissons1, salle1);
        classeTestee.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        Assertions.assertThrows(NbSucresIncorrectException.class, () -> classeTestee.preparerBoisson(machine1.getId(), typeBoisson, compte1.getId(), nbSucres));
    }

}
