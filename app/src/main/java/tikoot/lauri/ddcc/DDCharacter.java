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

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    public int[][] getAttributes() {
        return attributes;
    }

    public void setAttributes(int[][] attributes) {
        this.attributes = attributes;
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

    public void setAttribute(String attr, int score){
        switch (attr){
            case "str":
                break;
            case "dex":
                break;
            case "con":
                break;
            case "int":
                break;
            case "wis":
                break;
            case "cha":
                break;
            default:
                break;
        }
        setSkills(attr);
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
