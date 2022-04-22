package cc.modele.data.comptes;

public class Compte {

    private static Integer lastId = 0;

    private final Integer id;
    private final String login;
    private final String password;
    private String type;

    private Integer nbBoissonsBues;

    public Compte(String login, String password, String type) {
        this.id = ++lastId;
        this.login = login;
        this.password = password;
        this.type = type;

        this.nbBoissonsBues = 0;
    }

    public void incrementerNbBoissonsBues() {
        nbBoissonsBues++;
    }

    public String[] getRoles() {
        return TypeCompte.getRoles(type);
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getNbBoissonsBues() {
        return nbBoissonsBues;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}