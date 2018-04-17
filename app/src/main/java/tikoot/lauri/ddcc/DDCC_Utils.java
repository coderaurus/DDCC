package tikoot.lauri.ddcc;

import android.util.Log;

import java.util.List;
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

    public static void removeFromList(List<String> list, int... options) {
        if(options.length == 1){
            // We can just make a sublist and replace the original list with it
            int len = options[0];
            if(len >= list.size()) len = (options[0] - list.size()) - (list.size() -1) -1;
            Log.i("DDCC_Utils", "--- removeFromList(): len = " + len );
            for(int i=options[0]; list.listIterator().hasNext(); i++){
                list.remove(i);
            }
        }
        else if(options.length == 2){
            // Remove options[1] amount of items starting from options[0]
            int len = options[0] + options[1]; // Index we start from
            if(len >= list.size()) len = (len - list.size()) - (list.size() -1) -1;
            for(int i=len; i > options[0]; i--){
                list.remove(i);
            }
        }
    }
}
