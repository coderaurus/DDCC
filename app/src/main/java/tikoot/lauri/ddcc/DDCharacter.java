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

    private String[] languages;

    int level;
    int proficiency;
    int health;
    int hitDie;

    // [cell 1] + [cell 2]
    int [][] attributes;        // attribute score + modifier

    int [][] attributeSaves;    // modfiers + proficiency
    int [][] skills;            // modifier + proficiency

    public int getHealth() {
        return health;
    }

    // Requires CON modifier set
    public void setHealth(int level) {
        if(level == 1) {
            this.health = hitDie + getAttributeModifier("con");
        }
        else {
            int levelUp = level - getLevel();
            for(int i=0; i<levelUp && levelUp > 0; i++) {
                this.health += DDCC_Utils.rollDie(hitDie) + getAttributeModifier("con");
            }
        }
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public int getHitDie() {
        return hitDie;
    }

    public void setHitDie(int hitDie) {
        this.hitDie = hitDie;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        switch(characterClass) {
            case "Barbarian":
                hitDie = 12;
                setAttributeSave("str", 1);
                setAttributeSave("con", 1);
                break;
            case "Bard":
                hitDie = 8;
                setAttributeSave("dex", 1);
                setAttributeSave("cha", 1);
                break;
            case "Cleric":
                hitDie = 8;
                setAttributeSave("wis", 1);
                setAttributeSave("cha", 1);
                break;
            case "Druid":
                hitDie = 8;
                setAttributeSave("int", 1);
                setAttributeSave("wis", 1);
                break;
            case "Fighter":
                hitDie = 10;
                setAttributeSave("str", 1);
                setAttributeSave("con", 1);
                break;
            case "Monk":
                hitDie = 8;
                setAttributeSave("str", 1);
                setAttributeSave("dex", 1);
                break;
            case "Paladin":
                hitDie = 10;
                setAttributeSave("wis", 1);
                setAttributeSave("cha", 1);
                break;
            case "Ranger":
                hitDie = 10;
                setAttributeSave("str", 1);
                setAttributeSave("dex", 1);
                break;
            case "Rogue":
                hitDie = 8;
                setAttributeSave("dex", 1);
                setAttributeSave("int", 1);
                break;
            case "Sorcerer":
                hitDie = 6;
                setAttributeSave("con", 1);
                setAttributeSave("cha", 1);
                break;
            case "Warlock":
                hitDie = 8;
                setAttributeSave("wis", 1);
                setAttributeSave("cha", 1);
                break;
            case "Wizard":
                hitDie = 6;
                setAttributeSave("int", 1);
                setAttributeSave("wis", 1);
                break;
            default:
                break;
        }
        this.characterClass = characterClass;
    }

    public String getRace() {
        return race;
    }

    // setRace() doesn't take in account subraces like Mountain Dwarf or Wood Elf as of yet
    public void setRace(String race) {
        // Reset current racial attribute bonuses
        switch(this.race){
            case "Dragonborn":
                addToAttribute("str", -2);
                addToAttribute("cha", -2);
                break;
            case "Dwarf":
                addToAttribute("con", -2);
                break;
            case "Elf":
                addToAttribute("dex", -2);
                break;
            case "Gnome":
                addToAttribute("int", -2);
                break;
            case "Half-Elf":
                addToAttribute("cha", -2);
                break;
            case "Halfling":
                addToAttribute("dex", -2);
                break;
            case "Half-Orc":
                addToAttribute("str", -2);
                addToAttribute("con", -1);
                break;
            case "Human":
                addToAttribute("str", -1);
                addToAttribute("dex", -1);
                addToAttribute("con", -1);
                addToAttribute("int", -1);
                addToAttribute("wis", -1);
                addToAttribute("cha", -1);
                break;
            case "Tiefling":
                addToAttribute("int", -1);
                addToAttribute("cha", -2);
                break;
            default:
                break;
        }
        // Set new race
        switch(race){
            case "Dragonborn":
                addToAttribute("str", 2);
                addToAttribute("cha", 2);
                break;
            case "Dwarf":
                addToAttribute("con", 2);
                break;
            case "Elf":
                addToAttribute("dex", 2);
                break;
            case "Gnome":
                addToAttribute("int", 2);
                break;
            case "HalfElf":
                addToAttribute("cha", 2);
                break;
            case "Halfling":
                addToAttribute("dex", 2);
                break;
            case "HalfOrc":
                addToAttribute("str", 2);
                addToAttribute("con", 1);
                break;
            case "Human":
                addToAttribute("str", 1);
                addToAttribute("dex", 1);
                addToAttribute("con", 1);
                addToAttribute("int", 1);
                addToAttribute("wis", 1);
                addToAttribute("cha", 1);
                break;
            case "Tiefling":
                addToAttribute("int", 1);
                addToAttribute("cha", 2);
                break;
            default:
                break;
        }
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
        setProficiency();
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

    public int getAttributeModifier(String attr){
        int mod = 0;
        switch (attr){
            case "str":
                mod = attributes[0][1];
                break;
            case "dex":
                mod = attributes[1][1];
                break;
            case "con":
                mod = attributes[2][1];
                break;
            case "int":
                mod = attributes[3][1];
                break;
            case "wis":
                mod = attributes[4][1];
                break;
            case "cha":
                mod = attributes[5][1];
                break;
            default:
                break;
        }
        return mod;
    }

    public void addToAttribute(String attr, int amount){
        switch (attr){
            case "str":
                attributes[0][0] += amount;
                setAttributeModifier(0, attributes[0][0]);
                break;
            case "dex":
                attributes[1][0] += amount;
                setAttributeModifier(1, attributes[1][0]);
                break;
            case "con":
                attributes[2][0] += amount;
                setAttributeModifier(2, attributes[2][0]);
                break;
            case "int":
                attributes[3][0] += amount;
                setAttributeModifier(3, attributes[3][0]);
                break;
            case "wis":
                attributes[4][0] += amount;
                setAttributeModifier(4, attributes[4][0]);
                break;
            case "cha":
                attributes[5][0] += amount;
                setAttributeModifier(5, attributes[5][0]);
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

    public void setAttributeSave(String attr, int toggle) {
        // Toggle 1 = true, 0 = false
        switch (attr){
            case "str":
                attributeSaves[0][1] = toggle;
                break;
            case "dex":
                attributeSaves[1][1] = toggle;
                break;
            case "con":
                attributeSaves[2][1] = toggle;
                break;
            case "int":
                attributeSaves[3][1] = toggle;
                break;
            case "wis":
                attributeSaves[4][1] = toggle;
                break;
            case "cha":
                attributeSaves[5][1] = toggle;
                break;
            default:
                break;
        }
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
