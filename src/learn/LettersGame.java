package learn;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class LettersGame
{
    public static String file = "sowpods.txt"; //Specify dictionary file
    public static String conundrumFile = "conundrums.txt";
    public static Charset charset = Charset.forName("UTF-8"); //Specify dictionary charset (just in case).
    public static String[] dictionary;
    public static String[] nineLetters;
    public static String[] conundrums;
    public static int size;
    public static int score = 0;
    
/* Preliminary work to setup the dictionary and conundrums.
*/    
    public static void setup() {
    
        size = getListSize(file)+1; //Useful figure to throw around
        dictionary = getDictionary(); //Define the dictionary
        nineLetters = getxLetterWords(9);
        conundrums = getConundrums();
        
    }        

/* Main methods for each game/function
*/ 
    public static void isInTheDictionary()    {
        Scanner scan = new Scanner(System.in);
        LettersGame.spaceCreated();
        System.out.println("Please enter a word: ");
        String word = scan.nextLine();
        //word checking
        word = word.toLowerCase(); //all words in the dictionary are lower case
        boolean inDictionary = LettersGame.isAWord(word);
        
        if(inDictionary)
        {
            System.out.println("\n" + word + " is in the dictionary!");
        } else {
            System.out.println("\n" + word + " is not in the dictionary. :(");
        }    
    }

    public static void printFromDictionary()    {
        Scanner scan = new Scanner(System.in);
        spaceCreated();
        System.out.println("Please enter a number between 0 and " + (size-1));
        int index = scan.nextInt();
        scan.nextLine();
        System.out.println("\nThe word is " + dictionary[index]);    
    }
       
    public static void playLettersGame()    { //Plays the game such that the player chooses whether the next randomly generated letter is a consonant or a vowel. 
        Scanner scan = new Scanner(System.in);
        String mix = getLettersGame(getVowelsList(), getConsonantsList());
        
        try
        {
            spaceCreated();
            System.out.println("Choose a mix of 9 consonants and vowels: ");
            
            System.out.println(mix.toUpperCase() + "\nEnter the largest word you can find in the mix of letters.");
            String word = scan.nextLine();
            if(isAWord(word) && isValidCountdown(word, mix))
            {
                System.out.println("Well done, you got " + word.length() + " points!");
            } else if (!isAWord(word) && isValidCountdown(word, mix))
            {
                System.out.println("\nThat word is not in the dictionary!");
            } else {
                System.out.println("\nThat is not a valid word");
            }
            
        } catch (IndexOutOfBoundsException y)
        {
            System.out.println("You didn't enter a word. ");
        }   
        
        System.out.println("\nThe best word(s) available were: " + getBestWords(mix));
    }

    public static void playQuickLettersGame()    { //Plays the game with an automatically generated letters mix
        Scanner scan = new Scanner(System.in);
        String mix = getQuickLettersGame(getVowelsList(), getConsonantsList());
        
        try
        {
            spaceCreated();
            
            System.out.println(mix.toUpperCase() + "\nEnter the largest word you can find in the mix of letters.");
            String word = scan.nextLine();
            if(isAWord(word) && isValidCountdown(word, mix))
            {
                System.out.println("\nWell done, you got " + word.length() + " points!");
            } else if (!isAWord(word) && isValidCountdown(word, mix))
            {
                System.out.println("\nThat word is not in the dictionary!");
            } else {
                System.out.println("\nThat is not a valid word");
            }
            
            
        } catch (IndexOutOfBoundsException y)
        {
            System.out.println("You didn't enter a word. ");
        }
        
        System.out.println("\nThe best word(s) available were: " + getBestWords(mix));
    }
    
    public static void useAnagrammer()    { //Player can input a set of letters and the program will find all the words of a particular length inside the mix. 
        Scanner scan = new Scanner(System.in);
        spaceCreated();
        System.out.println("Enter a set of letters: ");
        String mix = scan.nextLine();
        System.out.println("Enter the length of word to search for: ");
        int length = scan.nextInt();
        if(getOtherWords(mix,length).length() < 2)
        {
            System.out.println("There are no available words of that length.\nLongest word(s): " + getBestWords(mix));            
        } else {
        System.out.println("\nThe available words are: " + getOtherWords(mix, length));
        }
    }

    public static void playConundrum() {
        Scanner scan = new Scanner(System.in);
        String mix = generateConundrum();

        try
        {
            spaceCreated();
            
            System.out.println("Find the " + mix.length() + " letter word in: " + mix.toUpperCase());
            String word = scan.nextLine().toLowerCase();
            if(isAWord(word) && isValidCountdown(word, mix) && word.length() == mix.length())
            {
                System.out.println("\nWell done, you got it!");
            } else if (mix.length() != word.length())
            {
                System.out.println("\nThat is not the correct length");
            } else {
                System.out.println("\nThat is not correct");
            }
            
            
        } catch (IndexOutOfBoundsException y)
        {
            System.out.println("You didn't enter a word. ");
        }
        
        System.out.println("\nThe answer was: " + getBestWords(mix).toUpperCase());
    }    

/* Methods required to run the games and functions
*/    
    
    public static int getListSize(String fileName) { //Calculates the number of lines in the dictionary
    
        int lineNumber = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line = null;
            
            while((line = reader.readLine()) != null)
            {
                lineNumber++;
            }
        } catch (IOException x) 
            {
                System.err.format("IOException: %s%n", x);
            }
        return lineNumber;
    }
    
    public static String[] getDictionary() {//Returns a string array made from the dictionary file.
    
        //int dictionarySize = getDictionarySize(); //Gets the number of lines in the dictionary
        String[] words = new String[size];
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {   
            int n = 0;
            String line = null;
            
            while((line = reader.readLine()) != null)
            {
                words[n] = line;
                n++;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        
        return words;
    }
    
    public static String[] getConundrums() {//Returns a string array made from the dictionary file.
    
        //int dictionarySize = getDictionarySize(); //Gets the number of lines in the dictionary
        String[] words = new String[getListSize(conundrumFile)];
        try (BufferedReader reader = new BufferedReader(new FileReader(conundrumFile)))
        {   
            int n = 0;
            String line = null;
            
            while((line = reader.readLine()) != null)
            {
                words[n] = line;
                n++;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        
        return words;
    }
   
    
    public static boolean isAWord(String word){
        word = word.toLowerCase();
        int index = (size + 1)/2;
        int previous = index;

        if(word.compareTo(dictionary[index]) > 0){
            index = (index + size)/2;
            return isAWord(word, index, size, previous);
        } else if(word.compareTo(dictionary[index]) < 0){
            index = index/2;
            return isAWord(word, index, previous, 0);
        } else {
            return true;
        }
    }

    public static boolean isAWord(String word, int index, int upper, int lower){
        int previous = index;
        
        if(word.compareTo(dictionary[index]) > 0){
            index = (index + upper)/2;
            if(index == previous){
                return false;
            }
            return isAWord(word, index, upper, previous);

        } else if(word.compareTo(dictionary[index]) < 0){
            index = (lower + index)/2;
            if(index == previous){
                return false;
            }
            return isAWord(word, index, previous, lower);
        } else {
            return true;
        }
    }
    
    public static boolean isValidCountdown(String word, String mix) {//Checks if a word is present in some larger string
    
        // mix is a random jumble of letters such like a countdown letters round. 
        // if word.length()> mix.length() = invalid
        // Take first char of word, check if it exists in mix. 
        // If any char of word doesn't exist or occurs twice, word is invalid. 
        int wordIndex = 0;

        mix = mix.toLowerCase();
        word = word.toLowerCase();
        
        do
        {
            int count = 0;
            for(int mixIndex = 0; mixIndex < mix.length(); mixIndex++) //i is mixIndex really
            {
                if(word.charAt(wordIndex) == mix.charAt(mixIndex))
                {
                    count++;
                    if(mixIndex != mix.length()-1)
                    {
                        mix = mix.substring(0,mixIndex) + mix.substring(mixIndex + 1);
                    } else {
                        mix = mix.substring(0, mixIndex);
                    }
                    mixIndex = mix.length();
                }
            }
            if(count == 0 || count > 1)
            {
                return false;
            }
            
            wordIndex++;
        } while (wordIndex < word.length());
        
        return true;
    }
    
    public static String getVowelsList()    {//Generates the list of vowels based on the quantity of each that Countdown uses
        int[] vowelNumber = {15, 21, 13, 13, 5};
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        
        int total = 0;
        for (int item : vowelNumber)
        {
            total = total + item;
        }
        String vowelsList = "";
        for(int i = 0; i < vowels.length; i++)
        {
            vowelsList = AppendLetter(vowelsList, vowelNumber[i], vowels[i]);
        }
        
        return vowelsList;
    }
    
    public static String getConsonantsList()    {//Generates the list of consonants based on the quantity of each that Countdown uses
        int[] consonantNumber = {2, 3, 6, 2, 3, 2, 1, 1, 5, 4, 8, 4, 1, 9, 9, 9, 1, 1, 1, 1, 1};
        char[] consonants = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'};
        int total = 0;
        for (int item : consonantNumber)
        {
            total = total + item;
        }
        
        String consonantsList = "";
        
        for(int i = 0; i < consonants.length; i++)
        {
            consonantsList = AppendLetter(consonantsList, consonantNumber[i], consonants[i]);
        }
        
        return consonantsList;
    }
    
    public static String AppendLetter(String list, int number, char c)    {//It appends a character (char c) onto the end of String list a number of times.
    //Function that probably already exists but I ended up writing it myself for experience. 
    
        for(int i = 0; i <= number; i++)
        {
            list += c;
        }
        return list;
    }
    
    public static String getLettersGame(String vowels, String consonants)    {//Takes user input to generate the letters mix
        Scanner letters = new Scanner(System.in);
        Random randomGenerator = new Random();
        String mix = "";
        for(int i = 0; i < 9;  )
        {
            System.out.println("Please enter C or V to choose a consonant or vowel.");
            String letter = letters.nextLine();
            if(letter.charAt(0) == 'c')
            {
                int index = randomGenerator.nextInt(consonants.length());
                mix = AppendLetter(mix, 0, consonants.charAt(index));
                if(index == consonants.length()-1)
                {
                    consonants = consonants.substring(0, index);
                } else {
                    consonants = consonants.substring(0, index) + consonants.substring(index+1, consonants.length()-1);
                }
                System.out.println(mix);
                i++;
            } else if (letter.charAt(0) == 'v')
            {
                int index = randomGenerator.nextInt(vowels.length());
                mix = AppendLetter(mix, 0, vowels.charAt(index));
                if(index == vowels.length()-1)
                {
                vowels = vowels.substring(0, index);
                } else {
                vowels = vowels.substring(0, index) + vowels.substring(index+1, vowels.length()-1);
                }
                System.out.println(mix);
                i++;
            } else {
                System.out.println("Please choose a consonant or a vowel");
            }
               
        }
        return mix;
    }
    
    public static String getQuickLettersGame(String vowels, String consonants)    {//Randomly generates the letters mix
        Scanner letters = new Scanner(System.in);
        Random randomGenerator = new Random();
        String mix = "";
        int consonantCount = 0;
        int vowelCount = 0;
        double distribution = 0.6; //The consonant-vowel preference factor. Greater than 0.5 prefers consonants. 
        for(int i = 0; i < 9;  )
        {
            double cOrV = randomGenerator.nextDouble(); //Chooses consonant or vowel at random with a preference for consonants. 
            
            
            //The following two while loops prevent excessive consonants or vowels
            while(consonantCount == 6 && cOrV <= distribution)
            {
                cOrV = randomGenerator.nextDouble();
            }
            
            while(vowelCount == 6 && cOrV > distribution)
            {
                cOrV = randomGenerator.nextDouble();
            }
            
            
            if(cOrV <= distribution) //True generates a consonant
            {
                int index = randomGenerator.nextInt(consonants.length());
                mix = AppendLetter(mix, 0, consonants.charAt(index));
                if(index == consonants.length()-1)
                {
                    consonants = consonants.substring(0, index);
                } else {
                    consonants = consonants.substring(0, index) + consonants.substring(index+1, consonants.length()-1);
                }
                consonantCount++;
                i++;
            } else if (cOrV > distribution)  //true generates a vowel
            {
                int index = randomGenerator.nextInt(vowels.length());
                mix = AppendLetter(mix, 0, vowels.charAt(index));
                if(index == vowels.length()-1)
                {
                vowels = vowels.substring(0, index);
                } else {
                vowels = vowels.substring(0, index) + vowels.substring(index+1, vowels.length()-1);
                }
                vowelCount++;
                i++;
            } 
               
        }
        return mix;
    }
    
    public static String getBestWords(String mix)    {//Returns a string containing the largest words in a letters mix
        String bestWords = new String();
        int maxLength = 0;
        long beforeTime = System.currentTimeMillis();
        for(int i = 1; i < size-1; i++)
        {
            //System.out.println(i);
            String word = dictionary[i].trim();
            if(isValidCountdown(word, mix) && maxLength <= word.trim().length())
            {
                if(maxLength < word.trim().length())
                {
                    bestWords = "";
                }
                maxLength = word.trim().length();
                bestWords = bestWords + ' ' + word;
            }
        }
        long afterTime = System.currentTimeMillis();
        System.out.println("Getting best words took: " + (afterTime - beforeTime) + " ms");
        return bestWords;
    }
    
    public static String getOtherWords(String mix, int length)    {//Returns a string containing words of a particular length in a letters mix
        String wordsOfLength = new String();
        for(int i = 1; i < size-1; i++)
        {
            //System.out.println(i);
            if(isValidCountdown(dictionary[i].trim(), mix) && length == dictionary[i].trim().length())
            {
                wordsOfLength = wordsOfLength + ' ' + dictionary[i];
            }
            
        }
        return wordsOfLength;
    }
    
    public final static void spaceCreated()    {//Skips a line in the terminal
        System.out.println("\b\n\n");
    }
   
    public static String generateConundrum() {//Returns a scrambled conundrum word. 
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(conundrums.length-1);
        String conundrum = conundrums[index];
        int length = conundrum.length();
        String scramble = "";

        while (scramble.length() < length) {
            int letter = randomGenerator.nextInt(conundrum.length());
            scramble = scramble + conundrum.charAt(letter);
            if(letter == conundrum.length() - 1){
                conundrum = conundrum.substring(0, letter);
            } else {
                conundrum = conundrum.substring(0, letter) + conundrum.substring(letter+1, conundrum.length());
            }
        }
        return scramble;
    }
    
    
    

    
/*
The following set of methods are for generating the conundrum file. Only needed for once off situations.
*/
    public static String[] getxLetterWords(int x) { //returns the array of words of a particular length from the dictionary
        int conundrumCount = 0;
        for (int i = 1 ; i < size - 1; i++) {
            if (dictionary[i].length() == x) {
                conundrumCount++;
            }
        }
        
        String[] conundrumset = new String[conundrumCount];
        int indexCount = 0;
        for (int i = 1 ; i < size - 1; i++) {
            if (dictionary[i].length() == 9) {
                conundrumset[indexCount] = dictionary[i];
                indexCount++;
            }
        }
        
        return conundrumset;
    }
    
    public static int numberOfBest9Words(String mix){ //returns the number of 9 letter words to be found in a particular mix. 
    //This is outrageously inefficient. Make a better method by counting the number of delimiters returned in the best words function.

        int numberOfWords = 0;
        int maxLength = 0;
        for(int i = 1; i < nineLetters.length-1; i++)
        {
            //System.out.println(i);
            if(isValidCountdown(nineLetters[i], mix) && maxLength <= nineLetters[i].length())
            {
                if(maxLength < nineLetters[i].length())
                {
                    numberOfWords = 0;
                }
                maxLength = nineLetters[i].length();
                numberOfWords++;
            }
        }
        return numberOfWords;
    }
   
    public static void makeConundrumsFile(){ //generates the conundrums file to conundrums.txt. 
    //A conundrum is a word which is not a standard plural and also has the only one possible word that is the same length as the mix. 
    
        try {
            File newFile = new File("conundrums.txt");
            
            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
                
            for (String conundrum : nineLetters){
                
                if (!newFile.exists()){
                    newFile.createNewFile();
                }
                
                if (conundrum.charAt(conundrum.length()-1) == 's' && isAWord(conundrum.substring(0, conundrum.length()-1))) {//Eliminates plurals ending in s
                    System.out.println(conundrum + ' ' + conundrum.substring(0, conundrum.length()-1));
                    continue;
                }
                    
                if (conundrum.substring(conundrum.length()-2).equals("es") && isAWord(conundrum.substring(0, conundrum.length()-2))) {//Eliminates plurals ending in es
                    System.out.println(conundrum + ' ' + conundrum.substring(0, conundrum.length()-2));
                    continue;
                }
                
                if (conundrum.substring(conundrum.length()-3).equals("ies") && isAWord(conundrum.substring(0, conundrum.length()-3) + 'y')) {//Eliminates plurals ending in ies
                    System.out.println(conundrum + ' ' + conundrum.substring(0, conundrum.length()-3) + 'y');
                    continue;
                }
                
                //Likely remaining plurals include 'ae', 'i', 'ia'
                
                if (numberOfBest9Words(conundrum)==1) {
                    bw.write(conundrum);
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
        }
    }
    

}