package cc.modele.data.machines;

public class psvm {
    public static void main(String[] args) {
        String a = "A1BC";
        char charArray[] = a.toCharArray();
        boolean bool = Character.isDigit(charArray[0]);
        if(bool) {
            System.out.println("First character is a digit");
        } else {
            System.out.println("First character is not a digit");
        }
    }
}
