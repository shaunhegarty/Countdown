
package learn;

import java.util.Arrays;

public class Numbers {
    private int target;
    private int[] numbers;
    private String solnString;
    private int solnLen;

    
    public Numbers(int target, int[] numbers){
        this.target = target;
        this.numbers = numbers;
        
    }

    private int solution = 0;
    
    
    private int function(int number, int[] nums, String solnString){
        if(solnString.isEmpty()){
            solnString = "" + number;
        }
        updateSolution(number, solnString);
        if(nums.length == 0){
            return this.solution;
        } else {
            add(number, nums, solnString);
            multiply(number, nums, solnString);
            divide(number, nums, solnString);
            subtract(number, nums, solnString);
        }
        return this.solution;

    }
    
    private int add(int number, int[] nums, String solnString){
        int result = number + nums[0];
        solnString = "(" + solnString + "+" + nums[0] + ")";
        updateSolution(result, solnString);

        return function(result, Arrays.copyOfRange(nums, 1, nums.length), solnString);
    }
    
    private int multiply(int number, int[] nums, String solnString){  
        if(nums.length < 2 && number < 0){
            return 0;
        }
        int result = number * nums[0];
        solnString = solnString + "x" + nums[0];
        updateSolution(result, solnString);

        return function(result, Arrays.copyOfRange(nums, 1, nums.length), solnString);
    }

    private int divide(int number, int[] nums, String solnString){
        if(nums[0] == 0 || number%nums[0] != 0 || (nums.length < 2 && number < 0)){
            return 0;
        }
        
        int result = number / nums[0];
        solnString = solnString + "/" + nums[0];
        updateSolution(result, solnString);

        return function(result, Arrays.copyOfRange(nums, 1, nums.length), solnString);
    }
    
    private int subtract(int number, int[] nums, String solnString){
        if(nums.length < 2 && number < 0){
            return 0;
        }
        
        int result = number - nums[0];
        solnString = "(" + solnString + "-" + nums[0] + ")";
        updateSolution(result, solnString);

        return function(result, Arrays.copyOfRange(nums, 1, nums.length), solnString);
    }
    
    private void updateSolution(int value, String solnString){
        
        if((Math.abs(target - value) == Math.abs(target - this.solution)) && getArgs(solnString) < this.solnLen){
            System.out.println("The previous solution used " + this.solnLen + " numbers");
            this.solution = value;
            this.solnLen = getArgs(solnString);
            this.solnString = solnString.toString();
        }else if(Math.abs(target - value) < Math.abs(target - this.solution)){
            this.solution = value;
            this.solnLen = getArgs(solnString);
            this.solnString = solnString.toString();            
        }
    }
    
    public int getSolution(){
        return solution;
    }    
    
    public String getSolnString(){
        return this.solnString;
    }
   
    public void solve(){
        
        int[] base = new int[0];
        long beforeTime = System.currentTimeMillis();
        this.getCombinations(this.numbers, base);

        long afterTime = System.currentTimeMillis();
        System.out.println("The solution took: " + (afterTime - beforeTime) + "ms" + " using only " + this.solnLen + " numbers.");
    }
    
    private void getCombinations(int[] nums, int[] base){
        //index start at 0
        int[] result = new int[this.numbers.length]; 
        int[] endHolder = new int[nums.length - 1];
        int[] baseHolder = new int[base.length + 1];
        System.arraycopy(base, 0, baseHolder, 0, base.length); //copy previous base to baseholder, leaving space for next base number
        
        for(int i = 0; i < nums.length; i++){
            baseHolder[base.length] = nums[i];
            System.arraycopy(nums, 0, endHolder, 0, i);
            System.arraycopy(nums, i + 1, endHolder, i, nums.length - 1 - i);
            if(nums.length == 3){
                System.arraycopy(baseHolder, 0, result, 0, baseHolder.length);
                System.arraycopy(endHolder, 0, result, baseHolder.length, endHolder.length);
                function(result[0], Arrays.copyOfRange(result, 1, result.length), "");
                
                //Then swap
                int temp = endHolder[0];
                endHolder[0] = endHolder[1];
                endHolder[1] = temp;
                
                System.arraycopy(baseHolder, 0, result, 0, baseHolder.length);
                System.arraycopy(endHolder, 0, result, baseHolder.length, endHolder.length);
                function(result[0], Arrays.copyOfRange(result, 1, result.length), "");              
                
            } else {
                getCombinations(endHolder, baseHolder);
            }
            
        }
    }
    
    public int fac(int n){
        int result = 1;
        for(int i = 1; i < n + 1; i++){
            result = result * i;
        }
        return result;
    }
    
    public void printArr(int[] arr){
        String end = "[";
        for(int i = 0; i < arr.length; i++){
            end = end + arr[i] + ", ";
        }
        end = end + "\b\b]\n";
        System.out.print(end);
    }
    
    private int getArgs(String in){        
        int count = in.length() - in.replaceAll("[+x/-]", "").length() + 1;
        return count;
    }
    
    
    
}
