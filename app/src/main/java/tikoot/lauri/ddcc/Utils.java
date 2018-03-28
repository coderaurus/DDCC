package tikoot.lauri.ddcc;

import java.util.Optional;
import java.util.Random;

public class Utils {
    public int rollDie(int  die) {
        Random rand = new Random();
        return rand.nextInt(die) + 1;
    }

    public int rollDies(int die, int times, String... options){
        int result = 0;
        if(options.length > 0){
            for(String option : options){
                int roll = 0;
                if(option.equals("smallest")){
                    int smallest = 0;
                    for(int i=0; i<times; i++){
                        roll = rollDie(die);
                        if(i == 0 || roll < smallest){
                            smallest = roll;
                        }
                        result += roll;
                    }
                    result -= smallest;
                }
                else if(option.equals("biggest")){
                    int biggest = 0;
                    for(int i=0; i<times; i++){
                        roll = rollDie(die);
                        if(i == 0 || roll < biggest){
                            biggest = roll;
                        }
                        result += roll;
                    }
                    result -= biggest;
                }
            }
        }
        else {
            for(int i=0; i<times; i++){
                result += rollDie(die);
            }
        }

        return result;
    }
}
