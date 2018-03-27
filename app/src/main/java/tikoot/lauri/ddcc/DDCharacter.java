package tikoot.lauri.ddcc;

/**
 * Created by LaglessLlama on 26.3.18.
 *
 * DDCharacter stores all the necessary information of a created character.
 */

public class DDCharacter {

    private String characterClass;
    private String race;
    private String background;
    private String alignment;

    int level, proficiency, health;
                                // [cell 1] + [cell 2]
    int [][] attributes;        // attribute score + modifier
    int [][] attributeSaves;    // modfiers + proficiency
    int [][] skills;            // modifier + proficiency

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency() {
        if(level >= 1 && level <= 4){
            proficiency = 2;
        }
        else if(level >= 5 && level <= 8) {
            proficiency = 3;
        }
        else if(level >= 9 && level <= 12) {
            proficiency = 4;
        }
        else if(level >= 13 && level <= 16) {
            proficiency = 5;
        }
        else if(level >= 17 && level <= 20) {
            proficiency = 6;
        }
    }

    public int[][] getAttributes() {
        return attributes;
    }

    public void setAttributes(int[][] attributes) {
        this.attributes = attributes;
    }

    public void setAttribute(String attr, int score){
        switch (attr){
            case "str":
                attributes[0][0] = score;
                setAttributeModifier(0, score);
                break;
            case "dex":
                attributes[1][0] = score;
                setAttributeModifier(1, score);
                break;
            case "con":
                attributes[2][0] = score;
                setAttributeModifier(2, score);
                break;
            case "int":
                attributes[3][0] = score;
                setAttributeModifier(3, score);
                break;
            case "wis":
                attributes[4][0] = score;
                setAttributeModifier(4, score);
                break;
            case "cha":
                attributes[5][0] = score;
                setAttributeModifier(5, score);
                break;
            default:
                break;
        }
        setSkills(attr);
    }

    private void setAttributeModifier(int i, int score) {
        int mod = 0;
        if(score == 1){
            mod = -5;
        }
        else if(score == 2 || score == 3){
            mod = -4;
        }
        else if(score == 4 || score == 5){
            mod = -3;
        }
        else if(score == 6 || score == 7){
            mod = -2;
        }
        else if(score == 8 || score == 9){
            mod = -1;
        }
        else if(score == 10 || score == 11){
            mod = 0;
        }
        else if(score == 12 || score == 13){
            mod = 1;
        }
        else if(score == 14 || score == 15){
            mod = 2;
        }
        else if(score == 16 || score == 17){
            mod = 3;
        }
        else if(score == 18 || score == 19){
            mod = 4;
        }
        else if(score == 20 || score == 21){
            mod = 5;
        }
        else if(score == 22 || score == 23){
            mod = 6;
        }
        else if(score == 24 || score == 25){
            mod = 7;
        }
        else if(score == 26 || score == 27){
            mod = 8;
        }
        else if(score == 28 || score == 29){
            mod = 9;
        }
        else if(score == 30){
            mod = 10;
        }
        attributes[i][1] = mod;
    }

    public int[][] getAttributeSaves() {
        return attributeSaves;
    }

    public void setAttributeSaves(int[][] attributeSaves) {
        this.attributeSaves = attributeSaves;
    }

    public int[][] getSkills() {
        return skills;
    }

    public void setSkills(int[][] skills) {
        this.skills = skills;
    }

    private void setSkills(String attr) {
        switch (attr){
            case "str":
                skills[3][0] = attributes[0][1];
                break;
            case "dex":
                skills[0][0] = attributes[1][1];
                skills[15][0] = attributes[1][1];
                skills[16][0] = attributes[1][1];
                break;
            case "con":
                break;
            case "int":
                skills[2][0] = attributes[3][1];
                skills[5][0] = attributes[3][1];
                skills[8][0] = attributes[3][1];
                skills[10][0] = attributes[3][1];
                skills[14][0] = attributes[3][1];
                break;
            case "wis":
                skills[1][0] = attributes[4][1];
                skills[6][0] = attributes[4][1];
                skills[9][0] = attributes[4][1];
                skills[11][0] = attributes[4][1];
                skills[17][0] = attributes[4][1];
                break;
            case "cha":
                skills[4][0] = attributes[5][1];
                skills[7][0] = attributes[5][1];
                skills[12][0] = attributes[5][1];
                skills[13][0] = attributes[5][1];
                break;
            default:
                break;
        }
        applySkillProficiencies();
    }

    private void applySkillProficiencies() {
        for(int[] skill: skills) {
            // 1 = has proficiency
            if(skill[1] == 1) {
                skill[0] += proficiency;
            }
        }
    }
}
