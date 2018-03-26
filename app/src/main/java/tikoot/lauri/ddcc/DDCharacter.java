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

    int level, proficiency;
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
    }
}
