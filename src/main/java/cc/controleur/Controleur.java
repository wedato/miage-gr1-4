package cc.controleur;

import cc.modele.FacadeModele;
import cc.modele.data.comptes.Compte;
import cc.modele.exceptions.CompteDejaExistantException;
import cc.modele.exceptions.InformationsIncompletesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;


@RestController
@RequestMapping("/api")
public class Controleur {


    @Autowired
    FacadeModele facadeModele;


    @PostMapping("/drinkers")
    public ResponseEntity<Compte> registerDrinker(@RequestParam String login, @RequestParam String password){

        try {
            Compte drinker = facadeModele.creerCompteDrinker(login, password);
            URI nextLocation = ServletUriComponentsBuilder.fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(drinker.getId())
                    .toUri();
            return ResponseEntity.created(nextLocation).build();
        } catch (InformationsIncompletesException e) {
            return ResponseEntity.status(400).build();
        } catch (CompteDejaExistantException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @GetMapping("/drinkers")
    public ResponseEntity<Collection<Compte>> getAll(){

        return ResponseEntity.ok(facadeModele.getAllComptes());
    }


}
