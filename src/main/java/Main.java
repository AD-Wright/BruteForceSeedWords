/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AD-Wright
 */

import java.time.*;
import java.util.*;

public class Main {
    
    public static void main (String args[]){
        Instant start = Instant.now();
        int seed = 1626596909;
        int maxlength = 9;
        Instant end = Instant.now();
        System.out.print(Duration.between(start, end));
        System.out.println(" Beginning test for: " + seed + " to length: " + maxlength);
        // iterate over word length from 1 to maxlength
        for (int h = 1; h < maxlength + 1; h++) {
            end = Instant.now();
            System.out.print(Duration.between(start, end));
            System.out.println(" Testing words of length: " + h);
            // combination generator code adapted from: https://stackoverflow.com/a/9176142/9727429
            char[] guess = new char[h];
            Arrays.fill(guess, 'A');
            do {
                //System.out.print(", " + new String(guess));
                // calculate the hash of the word (this inner loop could be multi-threaded)
                // input guess, seed; return true if match (or return string)
                int hash = 0;
                for (int i = 0; i < h; i++) {
                    int mult = 1;
                    for (int j = (h - (i + 1)); j > 0; j--) {
                    mult *= 31;
                    }
                    hash += mult*guess[i];
                }
                // print matching "words"
                if ( hash == seed ) {
                    end = Instant.now();
                    System.out.print(Duration.between(start, end));
                    System.out.print(" " + new String(guess));
                    System.out.println(", " + hash);
                }
                // back to main thread
                int incrementIndex = guess.length - 1;
                while (incrementIndex >= 0) {
                    guess[incrementIndex]++;
                    if (guess[incrementIndex] > 'z') {
                        if (incrementIndex > 0) {
                            guess[incrementIndex] = 'A';
                        }
                        incrementIndex--;
                    }
                    else {
                        break;
                    }
                }
            } while (guess[0] <= 'z');
        }
        end = Instant.now();
        System.out.print(Duration.between(start, end));
        System.out.println(" Search Completed");
    }
}

// to make runnable on GPU:
// partition out word generator code into function: take starting word, return X number of next words
// feed that into the hash function (on GPU)
// sort through results (or have GPU compare): return correct string(s)
// feed last word into word generator function again.
// continuously write results to dated file.