
package learn;

public class NumbersGame {
    public static void main(String[] args){
        int target = 4551;
        int[] numbers = {4, 6, 3, 9, 4, 6, 7};
        
        Numbers game1 = new Numbers(target, numbers);
        game1.solve();

        System.out.println(game1.getSolution() + ": " + game1.getSolnString());

    }
}
