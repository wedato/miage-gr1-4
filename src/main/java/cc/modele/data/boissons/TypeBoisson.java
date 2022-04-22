package cc.modele.data.boissons;

import java.util.Collection;
import java.util.List;

public class TypeBoisson {

    public static final String CAFE = "cafe";
    public static final String THE = "the";

    public static Collection<String> getAllTypes() {
        return List.of(CAFE, THE);
    }

    // Constructeur privé, car classe de constantes
    private TypeBoisson() {
        // NOP
    }

}
