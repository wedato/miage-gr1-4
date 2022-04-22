package cc.modele.data.comptes;

public class Role {

    public static final String DRINKER = "DRINKER";
    public static final String FABULOUS = "FABULOUS";

    public static String[] getDefaultRoles() {
        return new String[]{};
    }

    public static String[] getRolesDrinker() {
        return new String[]{DRINKER};
    }

    public static String[] getRolesFabulous() {
        return new String[]{DRINKER, FABULOUS};
    }

}
