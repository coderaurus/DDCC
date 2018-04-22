package tikoot.lauri.ddcc;

import java.util.Random;

/**
 * DDCC_Utils contains practical methods from randomized integer suited for the app and simulating
 * rolling the dies.
 */
public class DDCC_Utils {

    /**
     * Method returns a randomized value between 1 (inclusive) and <code>value</code>(inclusive).
     *
     * @param value the max value
     * @return the randomized value
     */
    public static int randomInt(int value){
        return new Random().nextInt(value) + 1;
    }

    /**
     * Method simulates a single dice roll.
     *
     * @param die the amount of sides in dice (4, 6, 8, ..)
     * @return the result of the roll
     */
    public static int rollDie(int  die) {
        Random rand = new Random();
        return rand.nextInt(die) + 1;
    }

    /**
     * Method simulates different kinds of die rolls used in roleplaying and returns the sum of the
     * rolls. Method takes two bonus options currently:
     * <ul>
     *     <li>"smallest" to drop the smallest die off the result</li>
     *     <li>"biggest" to drop the biggest die off the result</li>
     * </ul>
     * Note that <u>only one option is taken</u>, even though there is possibility to add more.
     *
     * @param die the amount of sides in the dice (4, 6, 8, ...)
     * @param times how many times the dice is rolled
     * @param options special rules
     * @return the sum of the rolls
     */
    public static int rollDies(int die, int times, String... options){
        int result = 0;
        // Any options given? => Cycle through them
        if(options.length > 0){
            for(String option : options){
                if(option.equals("smallest")){      // Take smallest roll out of result
                    int smallest = 0;
                    for(int i=0; i<times; i++){
                        int roll = rollDie(die);
                        if(i == 0 || roll < smallest){
                            smallest = roll;
                        }
                        result += roll;
                    }
                    result -= smallest;
                    break;
                }
                else if(option.equals("biggest")){  // Take biggest roll out of result
                    int biggest = 0;
                    for(int i=0; i<times; i++){
                        int roll = rollDie(die);
                        if(i == 0 || roll < biggest){
                            biggest = roll;
                        }
                        result += roll;
                    }
                    result -= biggest;
                    break;
                }
            }
        }
        else { // No options, return the sum of ALL rolls
            for(int i=0; i<times; i++){
                result += rollDie(die);
            }
        }
        return result;
    }

    /**
     * Method makes a string containing values of two-dimensional array.
     * <i>Used for debug printing.</i>
     *
     * @param list two dimensional-array to print
     * @return string containing values of the array
     */
    public static String printList(int [][] list){
        String str = "{";
        int i=0;

        for(int [] item : list) {
            str += "[" + item[0] + ", " + item[1] + "]";
            if(i != list.length - 1){
                str += ", ";
            }
            i++;
        }
        str += "}";

        return str;
    }
}
