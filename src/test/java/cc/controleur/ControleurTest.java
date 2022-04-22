package cc.controleur;

import cc.modele.FacadeModele;
import cc.modele.data.boissons.TypeBoisson;
import cc.modele.data.comptes.Compte;
import cc.modele.data.comptes.TypeCompte;
import cc.modele.data.machines.Machine;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.text.MessageFormat;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ControleurTest {

    @Autowired
    FacadeModele facadeModele;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mvc;

    private final String loginFabulous = "fabulous";
    private final String passwordFabulous = "CoffeeLover";

    @Test
    void postDrinker_OK_201() throws Exception {
        // ARRANGE
        String login = "foo";
        String password = "bar";

        // ACT & ASSERT
        String params = MessageFormat.format("login={0}&password={1}", login, password);
        mvc.perform(post(URI.create("/api/drinkers"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(params))
                .andExpectAll(
                        status().isCreated(),
                        header().exists("Location")
                );
    }

    @Test
    void postDrinker_KO_400_loginInvalide() throws Exception {
        // ARRANGE
        String login = "   ";
        String password = "bar";

        // ACT & ASSERT
        String params = MessageFormat.format("login={0}&password={1}", login, password);
        mvc.perform(post(URI.create("/api/drinkers"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(params))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postDrinker_KO_400_passwordInvalide() throws Exception {
        // ARRANGE
        String login = "foo";
        String password = "   ";

        // ACT & ASSERT
        String params = MessageFormat.format("login={0}&password={1}", login, password);
        mvc.perform(post(URI.create("/api/drinkers"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(params))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postDrinker_KO_409_compteDejaExistant() throws Exception {
        // ARRANGE
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String params = MessageFormat.format("login={0}&password={1}", login, password);
        mvc.perform(post(URI.create("/api/drinkers"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(params))
                .andExpect(status().isConflict());
    }

    @Test
    void getDrinkers_OK_200() throws Exception {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        facadeModele.creerCompteDrinker(login1, passwordEncoder.encode(password1));
        facadeModele.creerCompteFabulous(login2, passwordEncoder.encode(password2));

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/drinkers"))
                        .with(httpBasic(loginFabulous, passwordFabulous)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getDrinkers_KO_403() throws Exception {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        facadeModele.creerCompteDrinker(login1, passwordEncoder.encode(password1));
        facadeModele.creerCompteFabulous(login2, passwordEncoder.encode(password2));

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/drinkers"))
                        .with(httpBasic(login1, password1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getDrinkerById_OK_200_drinker() throws Exception {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        facadeModele.creerCompteDrinker(login1, passwordEncoder.encode(password1));
        String login2 = "foo2";
        String password2 = "bar2";
        Compte compte2 = facadeModele.creerCompteFabulous(login2, passwordEncoder.encode(password2));

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/drinkers/" + compte2.getId()))
                        .with(httpBasic(login1, password1)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getDrinkerById_OK_200_fabulous() throws Exception {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        Compte compte1 = facadeModele.creerCompteDrinker(login1, passwordEncoder.encode(password1));

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/drinkers/" + compte1.getId()))
                        .with(httpBasic(loginFabulous, passwordFabulous)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getDrinkerById_KO_401_sansAuth() throws Exception {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        Compte compte1 = facadeModele.creerCompteDrinker(login1, passwordEncoder.encode(password1));

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/drinkers/" + compte1.getId())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getDrinkerById_OK_404() throws Exception {
        // ARRANGE
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        facadeModele.creerCompteDrinker(login1, passwordEncoder.encode(password1));
        facadeModele.creerCompteFabulous(login2, passwordEncoder.encode(password2));

        int idDummy = -1;

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/drinkers/" + idDummy))
                        .with(httpBasic(login1, password1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchDrinker_OK_200() throws Exception {
        // ARRANGE
        String typeCompte = TypeCompte.FABULOUS;
        String login = "foo";
        String password = "bar";
        Compte compte = facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        mvc.perform(patch(URI.create("/api/drinkers/" + compte.getId() + "/type"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(typeCompte))
                .andExpect(status().isOk());
    }

    @Test
    void patchDrinker_KO_403() throws Exception {
        // ARRANGE
        String typeCompte = TypeCompte.FABULOUS;
        String login = "foo";
        String password = "bar";
        Compte compte = facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        mvc.perform(patch(URI.create("/api/drinkers/" + compte.getId() + "/type"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(typeCompte))
                .andExpect(status().isForbidden());
    }

    @Test
    void patchDrinker_KO_400() throws Exception {
        // ARRANGE
        String typeCompte = "dummy";
        String login = "foo";
        String password = "bar";
        Compte compte = facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        mvc.perform(patch(URI.create("/api/drinkers/" + compte.getId() + "/type"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(typeCompte))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchDrinker_KO_404() throws Exception {
        // ARRANGE
        String typeCompte = TypeCompte.FABULOUS;
        String login1 = "foo1";
        String password1 = "bar1";
        String login2 = "foo2";
        String password2 = "bar2";
        facadeModele.creerCompteDrinker(login1, passwordEncoder.encode(password1));
        facadeModele.creerCompteFabulous(login2, passwordEncoder.encode(password2));

        int idDummy = -1;

        // ACT & ASSERT
        mvc.perform(patch(URI.create("/api/drinkers/" + idDummy + "/type"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(typeCompte))
                .andExpect(status().isNotFound());
    }

    @Test
    void postMachine_OK_201() throws Exception {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "A38";

        // ACT & ASSERT
        String jsonMachine = new JSONObject()
                .put("nom", nom)
                .put("typeBoissons", typeBoissons)
                .put("salle", salle)
                .toString();
        mvc.perform(post(URI.create("/api/machines"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMachine))
                .andExpectAll(
                        status().isCreated(),
                        header().exists("Location")
                );
    }

    @Test
    void postMachine_KO_401() throws Exception {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "A38";

        // ACT & ASSERT
        String jsonMachine = new JSONObject()
                .put("nom", nom)
                .put("typeBoissons", typeBoissons)
                .put("salle", salle)
                .toString();
        mvc.perform(post(URI.create("/api/machines"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMachine))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void postMachine_KO_403() throws Exception {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "A38";
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonMachine = new JSONObject()
                .put("nom", nom)
                .put("typeBoissons", typeBoissons)
                .put("salle", salle)
                .toString();
        mvc.perform(post(URI.create("/api/machines"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMachine))
                .andExpect(status().isForbidden());
    }

    @Test
    void postMachine_KO_400_nomVide() throws Exception {
        // ARRANGE
        String nom = "   ";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "A38";

        // ACT & ASSERT
        String jsonMachine = new JSONObject()
                .put("nom", nom)
                .put("typeBoissons", typeBoissons)
                .put("salle", salle)
                .toString();
        mvc.perform(post(URI.create("/api/machines"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMachine))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postMachine_KO_400_typeBoissonVide() throws Exception {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = "   ";
        String salle = "A38";

        // ACT & ASSERT
        String jsonMachine = new JSONObject()
                .put("nom", nom)
                .put("typeBoissons", typeBoissons)
                .put("salle", salle)
                .toString();
        mvc.perform(post(URI.create("/api/machines"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMachine))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postMachine_KO_400_salleVide() throws Exception {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "   ";

        // ACT & ASSERT
        String jsonMachine = new JSONObject()
                .put("nom", nom)
                .put("typeBoissons", typeBoissons)
                .put("salle", salle)
                .toString();
        mvc.perform(post(URI.create("/api/machines"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMachine))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postMachine_KO_400_formatSalleIncorrect() throws Exception {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "420";

        // ACT & ASSERT
        String jsonMachine = new JSONObject()
                .put("nom", nom)
                .put("typeBoissons", typeBoissons)
                .put("salle", salle)
                .toString();
        mvc.perform(post(URI.create("/api/machines"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMachine))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postMachine_KO_409_machineDejaExistante() throws Exception {
        // ARRANGE
        String nom = "coffee";
        String typeBoissons = TypeBoisson.CAFE;
        String salle = "A38";
        facadeModele.ajouterMachine(nom, typeBoissons, salle);

        // ACT & ASSERT
        String jsonMachine = new JSONObject()
                .put("nom", nom)
                .put("typeBoissons", typeBoissons)
                .put("salle", salle)
                .toString();
        mvc.perform(post(URI.create("/api/machines"))
                        .with(httpBasic(loginFabulous, passwordFabulous))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMachine))
                .andExpect(status().isConflict());
    }

    @Test
    void getMachines_OK_200_sansFiltre() throws Exception {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/machines")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getMachines_OK_200_salleExistante() throws Exception {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/machines?salle=" + salle1)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getMachines_OK_200_salleInexistante() throws Exception {
        // ARRANGE
        String salleDummy = "Z00";
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/machines?salle=" + salleDummy)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getMachines_KO_400_formatSalleIncorrect() throws Exception {
        // ARRANGE
        String salleDummy = "420";
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/machines?salle=" + salleDummy)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMachineById_OK_200_sansAuth() throws Exception {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/machines/" + machine1.getId())))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getMachineById_OK_200_authDrinker() throws Exception {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/machines/" + machine1.getId()))
                        .with(httpBasic(login, password)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getMachineById_OK_200_authFabulous() throws Exception {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/machines/" + machine1.getId()))
                        .with(httpBasic(loginFabulous, passwordFabulous)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getMachineById_OK_404() throws Exception {
        // ARRANGE
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        int idDummy = -1;

        // ACT & ASSERT
        mvc.perform(get(URI.create("/api/machines/" + idDummy)))
                .andExpect(status().isNotFound());
    }

    @Test
    void putMachineCafe_OK_202_authDrinker() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine1.getId() + "/cafe"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isAccepted());
    }

    @Test
    void putMachineCafe_OK_202_authFabulous() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "fabulous"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteFabulous(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine1.getId() + "/cafe"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isAccepted());
    }

    @Test
    void putMachineCafe_KO_401_sansAuth() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine1.getId() + "/cafe"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void putMachineCafe_KO_400_nbSucresIncorrect() throws Exception {
        // ARRANGE
        Integer nbSucres = -1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine1.getId() + "/cafe"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putMachineCafe_KO_404_machineInexistante() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        int idDummy = -1;

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + idDummy + "/cafe"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isNotFound());
    }

    @Test
    void putMachineCafe_KO_418_theiere() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        Machine machine2 = facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine2.getId() + "/cafe"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isIAmATeapot());
    }

    @Test
    void putMachineThe_OK_202_authDrinker() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        Machine machine2 = facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine2.getId() + "/the"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isAccepted());
    }

    @Test
    void putMachineThe_OK_202_authFabulous() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        Machine machine2 = facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "fabulous"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteFabulous(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine2.getId() + "/the"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isAccepted());
    }

    @Test
    void putMachineThe_KO_401_sansAuth() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        Machine machine2 = facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine2.getId() + "/the"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void putMachineThe_KO_400_nbSucresIncorrect() throws Exception {
        // ARRANGE
        Integer nbSucres = -1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        Machine machine2 = facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine2.getId() + "/the"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putMachineThe_KO_404_machineInexistante() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        int idDummy = -1;

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + idDummy + "/the"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isNotFound());
    }

    @Test
    void putMachineThe_KO_400_cafetiere() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine1.getId() + "/the"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putMachine_KO_400_typeBoissonInconnu() throws Exception {
        // ARRANGE
        Integer nbSucres = 1;
        // Machines
        String nom1 = "coffee1";
        String typeBoissons1 = TypeBoisson.CAFE;
        String salle1 = "A38";
        String nom2 = "tea2";
        String typeBoissons2 = TypeBoisson.THE;
        String salle2 = "B73";
        Machine machine1 = facadeModele.ajouterMachine(nom1, typeBoissons1, salle1);
        facadeModele.ajouterMachine(nom2, typeBoissons2, salle2);
        // Compte "drinker"
        String login = "foo";
        String password = "bar";
        facadeModele.creerCompteDrinker(login, passwordEncoder.encode(password));

        // ACT & ASSERT
        String jsonPreparation = new JSONObject()
                .put("nbSucres", nbSucres)
                .toString();
        mvc.perform(put(URI.create("/api/machines/" + machine1.getId() + "/dummy"))
                        .with(httpBasic(login, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPreparation))
                .andExpect(status().isBadRequest());
    }

}
