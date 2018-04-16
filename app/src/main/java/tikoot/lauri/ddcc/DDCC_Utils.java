package tikoot.lauri.ddcc;

import java.util.Random;

public class DDCC_Utils {

    // Easier method to call for random number
    // ( 1 - value ) both inclusive
    public static int randomInt(int value){
        return new Random().nextInt(value) + 1;
    }

    public static int rollDie(int  die) {
        Random rand = new Random();
        return rand.nextInt(die) + 1;
    }

    public static int rollDies(int die, int times, String... options){
        int result = 0;
        // Any options given? => Cycle through them
        if(options.length > 0){
            for(String option : options){
                int roll = 0;
                if(option.equals("smallest")){      // Take smallest roll out of result
                    int smallest = 0;
                    for(int i=0; i<times; i++){
                        roll = rollDie(die);
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
                        roll = rollDie(die);
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
