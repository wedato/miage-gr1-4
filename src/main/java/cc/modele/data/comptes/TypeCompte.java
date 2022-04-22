package cc.modele.data.comptes;

import java.util.Collection;
import java.util.List;

public class TypeCompte {

    public static final String DRINKER = "drinker";
    public static final String FABULOUS = "fabulous";

    public static Collection<String> getAllTypes() {
        return List.of(DRINKER, FABULOUS);
    }

    public static String[] getRoles(String type) {
        switch (type) {
            case DRINKER:
                return Role.getRolesDrinker();
            case FABULOUS:
                return Role.getRolesFabulous();
            default:
                return Role.getDefaultRoles();
        }
    }

    // Constructeur privé, car classe de constantes
    private TypeCompte() {
        // NOP
    }

}
