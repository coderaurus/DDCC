package tikoot.lauri.ddcc;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LaglessLlama on 26.3.18.
 *
 * DDCharacter stores and handles all the necessary information of a created character.
 */

@Entity
public class DDCharacter implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="class")
    private String characterClass;
    @ColumnInfo(name="race")
    private String race;
    @ColumnInfo(name="background")
    private String background;
    @ColumnInfo(name="alignment")
    private String alignment;
    @ColumnInfo(name="languages")
    private List<List<String>> languages;
    @ColumnInfo(name="level")
    private int level;
    @ColumnInfo(name="proficiency")
    private int proficiency;
    @ColumnInfo(name="health")
    private int health;
    @ColumnInfo(name="hit-die")
    private int hitDie;
    @ColumnInfo(name="attribute")
    private int [][] attributes;        // skill:attribute score + modifier
    @ColumnInfo(name="attribute-save")
    private int [][] attributeSaves;    // modifiers + proficiency
    @ColumnInfo(name="skills")
    private int [][] skills;            // modifier + proficiency
    @ColumnInfo(name="initialized")
    private boolean initialized = false;

    /**
     * Empty constructor. Initializes variables and fills them with randomized values.
     */
    public DDCharacter() {
        characterClass = "";
        race = "";
        background = "";
        alignment = "";

        level = 0;
        proficiency = 0;
        health = 0;
        hitDie = 0;

        attributes = new int[6][2];
        attributeSaves = new int[6][2];
        skills = new int[18][2];
        languages = new ArrayList<>();
        languages.add(new ArrayList<String>()); // 0 is for languages provided by race
        languages.add(new ArrayList<String>()); // 1 is for languages provided by background

        setLevel(1);
        randomizeAttributes();
        randomizeRace();
        randomizeCharacterClass();
        randomizeBackground();
        randomizeSkillsByClass(characterClass);
        setHealth(getLevel());
        randomizeAlignment();
        initialized = true;
    }

    /**
     * Method set proficiency bonus of character. This is mainly for database if needed at all.
     * @param proficiency proficiency bonus
     */
    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    /**
     * Method sets the attribute saves array.
     * @param attributeSaves array of values you want to give
     */
    public void setAttributeSaves(int[][] attributeSaves) {
        this.attributeSaves = attributeSaves;
    }

    /**
     * Method returns state of initialization
     * @return boolean of state of initialization
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Method set the state of initialization of character.
     * @param initialized true if done, false if not
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Method returns the id. This is needed for database
     * @return primary key id
     */
    public int getId() {
        return id;

    }

    /**
     * Method sets the id. Needed for database.
     * <b>Do not use manually.</b>
     *
     * @param id primary key id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method returns total or maximum health value character can have.
     * @return total health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Method sets the health according to level and constitution modifier.
     * @param level level of character
     */
    public void setHealth(int level) {
        if(level == 1) {
            this.health = hitDie + getAttributeModifier("con");
        }
        else {
            this.health = hitDie + getAttributeModifier("con");
            for(int i=1; i<level; i++) {
                this.health += DDCC_Utils.rollDie(hitDie) + getAttributeModifier("con");
            }
        }
    }

    /**
     * Method returns List of Lists containing languages. The languages have been separated by origin.
     *
     * @return
     */
    public List<List<String>> getLanguages() {
        return languages;
    }

    /**
     * Method sets List holding all the languages
     * @param languages List of Lists containing languages
     */
    public void setLanguages(List<List<String>> languages) {
        this.languages = languages;
    }

    /**
     * Method adds languages to Lists. There are two options to use:
     *  - "racial" for langauges determined by Race
     *  - "other" for other sources
     * Super special languages like Druidic are not included.
     *
     * @param side the list you want to add language to
     * @param langs languages you want to add
     */
    public void addLanguage(String side, String... langs) {
        if(langs.length > 0) {
            if(side.equals("racial")) {
                for(String lang : langs){
                    if(languages.get(0).isEmpty() || !languages.get(0).contains(lang)) {
                        languages.get(0).add(lang);
                        // Replace duplicate from other array and randomize another language
                        // in return
                        if(languages.get(1).contains(lang)){
                            languages.get(1).remove(lang);
                            addRandomLanguages("other", 1);
                        }
                    }
                }
            }
            else { // side == "other"
                for(String lang : langs){
                    if(languages.get(1).isEmpty() || !languages.get(1).contains(lang) &&
                            !languages.get(0).contains(lang)) {
                        languages.get(1).add(lang);
                    }
                    else {
                        // Replace given language with a random one.
                        addRandomLanguages("other", 1);
                    }
                }
            }
        }
    }

    /**
     * Method adds random languages to specified list.
     * @param side list of languages you want to add to
     * @param amount amount of languages you want to randomize
     */
    public void addRandomLanguages(String side, int amount){
        for(int i=0; i<amount;) {
            String lang = randomLanguage();
            if(side.equals("racial")) {
                if(!languages.get(0).contains(lang)){
                    languages.get(0).add(lang);
                    i++;                        // only increment index if language was added
                }
            }
            else {
                if(!languages.get(1).contains(lang)){
                    languages.get(1).add(lang);
                    i++;                        // only increment index if language was added
                }
            }
        }
    }

    /**
     * Method returns a random language from a basic pool of languages determined by races.
     * @return randomized language
     */
    public String randomLanguage(){
        switch (DDCC_Utils.randomInt(7)) {
            case 1:
                return "Dwarvish";
            case 2:
                return "Elvish";
            case 3:
                return "Giant";
            case 4:
                return "Gnomish";
            case 5:
                return "Goblin";
            case 6:
                return "Halfling";
            case 7:
                return "Orc";
            default:
                return "N/A";
        }
    }

    /**
     * Method returns the amount of sides of hit die.
     * @return hit die value
     */
    public int getHitDie() {
        return hitDie;
    }

    /**
     * Method sets the hit die.
     * @param hitDie amount of sides the die has.
     */
    public void setHitDie(int hitDie) {
        this.hitDie = hitDie;
    }

    /**
     * Method returns class of the character.
     * @return the class of character
     */
    public String getCharacterClass() {
        return characterClass;
    }

    /**
     * Method randomizes the class of character.
     */
    public void randomizeCharacterClass(){
        switch (DDCC_Utils.randomInt(12)){
            case 1:
                setCharacterClass("Barbarian");
                break;
            case 2:
                setCharacterClass("Bard");
                break;
            case 3:
                setCharacterClass("Cleric");
                break;
            case 4:
                setCharacterClass("Druid");
                break;
            case 5:
                setCharacterClass("Fighter");
                break;
            case 6:
                setCharacterClass("Monk");
                break;
            case 7:
                setCharacterClass("Paladin");
                break;
            case 8:
                setCharacterClass("Ranger");
                break;
            case 9:
                setCharacterClass("Rogue");
                break;
            case 10:
                setCharacterClass("Sorcerer");
                break;
            case 11:
                setCharacterClass("Warlock");
                break;
            case 12:
                setCharacterClass("Wizard");
                break;
        }
    }

    /**
     * Method sets the class of character.
     * @param characterClass class you want to set
     */
    public void setCharacterClass(String characterClass) {
        if(initialized) resetCharacterClass();

        switch(characterClass) {
            case "Barbarian":
                hitDie = 12;
                setAttributeSave("str", true);
                setAttributeSave("con", true);
                break;
            case "Bard":
                hitDie = 8;
                setAttributeSave("dex", true);
                setAttributeSave("cha", true);
                break;
            case "Cleric":
                hitDie = 8;
                setAttributeSave("wis", true);
                setAttributeSave("cha", true);
                break;
            case "Druid":
                hitDie = 8;
                setAttributeSave("int", true);
                setAttributeSave("wis", true);
                break;
            case "Fighter":
                hitDie = 10;
                setAttributeSave("str", true);
                setAttributeSave("con", true);
                break;
            case "Monk":
                hitDie = 8;
                setAttributeSave("str", true);
                setAttributeSave("dex", true);
                break;
            case "Paladin":
                hitDie = 10;
                setAttributeSave("wis", true);
                setAttributeSave("cha", true);
                break;
            case "Ranger":
                hitDie = 10;
                setAttributeSave("str", true);
                setAttributeSave("dex", true);
                break;
            case "Rogue":
                hitDie = 8;
                setAttributeSave("dex", true);
                setAttributeSave("int", true);
                break;
            case "Sorcerer":
                hitDie = 6;
                setAttributeSave("con", true);
                setAttributeSave("cha", true);
                break;
            case "Warlock":
                hitDie = 8;
                setAttributeSave("wis", true);
                setAttributeSave("cha", true);
                break;
            case "Wizard":
                hitDie = 6;
                setAttributeSave("int", true);
                setAttributeSave("wis", true);
                break;
            default:
                break;
        }
        this.characterClass = characterClass;
    }

    /**
     * Method resets class of character.
     */
    private void resetCharacterClass() {
        hitDie = 0;
        switch(this.characterClass) {
            case "Barbarian":
                setAttributeSave("str", false);
                setAttributeSave("con", false);
                break;
            case "Bard":
                setAttributeSave("dex", false);
                setAttributeSave("cha", false);
                break;
            case "Cleric":
                setAttributeSave("wis", false);
                setAttributeSave("cha", false);
                break;
            case "Druid":
                setAttributeSave("int", false);
                setAttributeSave("wis", false);
                break;
            case "Fighter":
                setAttributeSave("str", false);
                setAttributeSave("con", false);
                break;
            case "Monk":
                setAttributeSave("str", false);
                setAttributeSave("dex", false);
                break;
            case "Paladin":
                setAttributeSave("wis", false);
                setAttributeSave("cha", false);
                break;
            case "Ranger":
                setAttributeSave("str", false);
                setAttributeSave("dex", false);
                break;
            case "Rogue":
                setAttributeSave("dex", false);
                setAttributeSave("int", false);
                break;
            case "Sorcerer":
                setAttributeSave("con", false);
                setAttributeSave("cha", false);
                break;
            case "Warlock":
                setAttributeSave("wis", false);
                setAttributeSave("cha", false);
                break;
            case "Wizard":
                setAttributeSave("int", false);
                setAttributeSave("wis", false);
                break;
            default:
                break;
        }
    }

    /**
     * Method returns the race of character.
     * @return the race of character
     */
    public String getRace() {
        return race;
    }

    /**
     * Method randomizes character's race. Races included are determined by Player's Handbook.
     */
    public void randomizeRace() {
        switch (DDCC_Utils.randomInt(9)){
            case 1:
                setRace("Dragonborn");
                break;
            case 2:
                setRace("Dwarf");
                break;
            case 3:
                setRace("Elf");
                break;
            case 4:
                setRace("Gnome");
                break;
            case 5:
                setRace("Half-Elf");
                break;
            case 6:
                setRace("Halfling");
                break;
            case 7:
                setRace("Half-Orc");
                break;
            case 8:
                setRace("Human");
                break;
            case 9:
                setRace("Tiefling");
                break;
        }
    }

    /**
     * Method sets the race of character.
     * @param race race of character
     */
    public void setRace(String race) {
        // Method doesn't take in account subraces like Mountain Dwarf or Wood Elf as of yet
        // Reset current racial attribute bonuses
        if(initialized) resetRace();
        // Set new race
        switch(race){
            case "Dragonborn":
                addToAttribute("str", 2);
                addToAttribute("cha", 2);
                addLanguage("racial", "Common", "Draconic");
                break;
            case "Dwarf":
                addToAttribute("con", 2);
                addLanguage("racial", "Common", "Dwarvish");
                break;
            case "Elf":
                addToAttribute("dex", 2);
                addLanguage("racial", "Common", "Elvish");
                break;
            case "Gnome":
                addToAttribute("int", 2);
                addLanguage("racial", "Common", "Gnomish");
                break;
            case "Half-Elf":
                addToAttribute("cha", 2);
                addLanguage("racial", "Common", "Elvish");
                addRandomLanguages("racial", 1);
                break;
            case "Halfling":
                addToAttribute("dex", 2);
                addLanguage("racial", "Common", "Halfling");
                break;
            case "Half-Orc":
                addToAttribute("str", 2);
                addToAttribute("con", 1);
                addLanguage("racial",  "Common", "Orc");
                break;
            case "Human":
                addToAttribute("str", 1);
                addToAttribute("dex", 1);
                addToAttribute("con", 1);
                addToAttribute("int", 1);
                addToAttribute("wis", 1);
                addToAttribute("cha", 1);
                addLanguage("racial", "Common");
                addRandomLanguages("racial",1);
                break;
            case "Tiefling":
                addToAttribute("int", 1);
                addToAttribute("cha", 2);
                addLanguage("racial", "Common", "Infernal");
                break;
            default:
                break;
        }
        this.race = race;
    }

    /**
     * Method resets all the attribute and language benefits race gives.
     */
    private void resetRace() {
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
        // Remove the two racial langauges
        languages.get(0).clear();
    }

    /**
     * Method returns background of character.
     * @return the background of character
     */
    public String getBackground() {
        return background;
    }

    /**
     * Method randomizes the background of character. Backgrounds are from Player's Handbook.
     */
    public void randomizeBackground() {
        switch (DDCC_Utils.randomInt(13)){
            case 1:
                setBackground("Acolyte");
                break;
            case 2:
                setBackground("Charlatan");
                break;
            case 3:
                setBackground("Criminal");
                break;
            case 4:
                setBackground("Entertainter");
                break;
            case 5:
                setBackground("Folk Hero");
                break;
            case 6:
                setBackground("Guild Artisan");
                break;
            case 7:
                setBackground("Hermit");
                break;
            case 8:
                setBackground("Noble");
                break;
            case 9:
                setBackground("Outlander");
                break;
            case 10:
                setBackground("Sage");
                break;
            case 11:
                setBackground("Sailor");
                break;
            case 12:
                setBackground("Soldier");
                break;
            case 13:
                setBackground("Urchin");
                break;
        }
    }

    /**
     * Method sets the background of character.
     * @param background background of character
     */
    public void setBackground(String background) {
        if(initialized) resetBackground();
        this.background = background;
        switch(background) {
            case "Acolyte":
                setSkillProficiency(true, "Insight", "Religion");
                addRandomLanguages("other",2);
                break;
            case "Charlatan":
                setSkillProficiency(true, "Deception", "Sleight_of_Hand");
                break;
            case "Criminal":
                setSkillProficiency(true, "Deception", "Stealth");
                break;
            case "Entertainer":
                setSkillProficiency(true,  "Acrobatics", "Performance");
                break;
            case "Folk Hero":
                setSkillProficiency(true,  "Animal_Handling", "Survival");
                break;
            case "Guild Artisan":
                setSkillProficiency(true,  "Insight", "Persuasion");
                addRandomLanguages("other",1);
                break;
            case "Hermit":
                setSkillProficiency(true,  "Medicine", "Religion");
                break;
            case "Noble":
                setSkillProficiency(true,  "History", "Persuasion");
                addRandomLanguages("other",1);
                break;
            case "Outlander":
                setSkillProficiency(true,  "Athletics", "Survival");
                addRandomLanguages("other",1);
                break;
            case "Sage":
                setSkillProficiency(true,  "Arcana", "History");
                addRandomLanguages("other",2);
                break;
            case "Sailor":
                setSkillProficiency(true,  "Athletics", "Perception");
                break;
            case "Soldier":
                setSkillProficiency(true,  "Athletics", "Intimidation");
                break;
            case "Urchin":
                setSkillProficiency(true,   "Sleight_Of_Hand", "Stealth");
                break;
            default:
                break;
        }        
    }

    /**
     * Method resets the benefits background gives. This includes skill proficiencies and languages.
     */
    public void resetBackground(){
        switch(this.background) {
            case "Acolyte":
                setSkillProficiency(false, "Insight", "Religion");
                languages.get(1).clear();
                break;
            case "Charlatan":
                setSkillProficiency(false, "Deception", "Sleight_of_Hand");
                break;
            case "Criminal":
                setSkillProficiency(false, "Deception", "Stealth");
                break;
            case "Entertainer":
                setSkillProficiency(false,  "Acrobatics", "Performance");
                break;
            case "Folk Hero":
                setSkillProficiency(false,  "Animal_Handling", "Survival");
                break;
            case "Guild Artisan":
                setSkillProficiency(false,  "Insight", "Persuasion");
                languages.get(1).clear();
                break;
            case "Hermit":
                setSkillProficiency(false,  "Medicine", "Religion");
                break;
            case "Noble":
                setSkillProficiency(false,  "History", "Persuasion");
                languages.get(1).clear();
                break;
            case "Outlander":
                setSkillProficiency(false,  "Athletics", "Survival");
                languages.get(1).clear();
                break;
            case "Sage":
                setSkillProficiency(false,  "Arcana", "History");
                languages.get(1).clear();
                break;
            case "Sailor":
                setSkillProficiency(false,  "Athletics", "Perception");
                break;
            case "Soldier":
                setSkillProficiency(false,  "Athletics", "Intimidation");
                break;
            case "Urchin":
                setSkillProficiency(false,   "Sleight_Of_Hand", "Stealth");
                break;
            default:
                break;
        }
    }

    /**
     * Method returns character's alignment.
     * @return alignment of character
     */
    public String getAlignment() {
        return alignment;
    }

    /**
     * Method sets the alignment of character.
     * @param alignment alignment of character
     */
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    /**
     * Method randomizes the alignment of character.
     */
    public void randomizeAlignment(){
        switch (DDCC_Utils.randomInt(9)){
           case 1:
               setAlignment("Lawful Good");
               break;
           case 2:
                setAlignment("Neutral Good");
                break;
            case 3:
                setAlignment("Chaotic Good");
                break;
            case 4:
                setAlignment("Lawful Evil");
                break;
            case 5:
                setAlignment("Neutral Evil");
                break;
            case 6:
                setAlignment("Chaotic Evil");
                break;
            case 7:
                setAlignment("Lawful Neutral");
                break;
            case 8:
                setAlignment("Neutral");
                break;
            case 9:
                setAlignment("Chaotic Neutral");
                break;
            default:
                break;
        }
    }

    /**
     * Method returns the level of character.
     * @return value of level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Method sets the level of character. Attribute increases are not included currenctly.
     *
     * @param level level you want
     */
    public void setLevel(int level) {
        this.level = level;
        setProficiency();
        /* TODO:    Implement Abiliy (aka attribute) Score additions at lvls 4, 8, 12, 16 and 19
                    Needs: Method(s) for adding attribute by 1) random or 2) class
         */
    }

    /**
     * Method returns the proficiency bonus determined by level.
     * @return
     */
    public int getProficiency() {
        return proficiency;
    }

    /**
     * Method sets the proficiency bonus. Bonus starts from level one and increases by one every
     * fourth level (excluding first stage at levels 1 to 4).
     */
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

    /**
     * Method returns attributes in two dimensional array. Array holds score and modifier
     * of attributes.
     * @return attributes in 2d array
     */
    public int[][] getAttributes() {
        return attributes;
    }

    /**
     * Method returns specified attribute score (not the modifier).
     * @param i index of attribute wanted
     * @return attribute score
     */
    public int getAttribute(int i){
        return attributes[i][0];
    }

    /**
     * Method sets array of attributes.
     * @param attributes 2D array of values
     */
    public void setAttributes(int[][] attributes) {
        this.attributes = attributes;
    }

    /**
     * Method randomizes attributes with formula given by Player's Handbook:
     * - Roll four d6 dice and take the smallest roll out of the result.
     */
    public void randomizeAttributes(){
        setAttribute("str", DDCC_Utils.rollDies(6, 4, "smallest"));
        setAttribute("dex", DDCC_Utils.rollDies(6, 4, "smallest"));
        setAttribute("con", DDCC_Utils.rollDies(6, 4, "smallest"));
        setAttribute("int", DDCC_Utils.rollDies(6, 4, "smallest"));
        setAttribute("wis", DDCC_Utils.rollDies(6, 4, "smallest"));
        setAttribute("cha", DDCC_Utils.rollDies(6, 4, "smallest"));
    }

    /**
     * Method sets specified attribute to specified score.
     * Method uses abbreviations of attributes.
     * @param attr attribute you want to set
     * @param score score you want
     */
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

        setAttributeSaveMod(attr);
        setSkills(attr);
    }

    /**
     * Method returns the modifier of given attribute
     * @param attr attribute of modifier you want
     * @return modifier of attribute
     */
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

    /**
     * Method adds a value to given attribute.
     * @param attr attribute you want to modify
     * @param amount amount you want to modify by
     */
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

    /**
     * Method sets modifier of attribute of given index to given score.
     * @param i index of attribute
     * @param score attribute score
     */
    public void setAttributeModifier(int i, int score) {
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

    /**
     * Method returns array holding values of attribute saves and their proficiencies.
     * @return 2D array of information about attribute saves
     */
    public int[][] getAttributeSaves() {
        return attributeSaves;
    }

    /**
     * Method sets the attribute save value according to whether attribute has proficiency or not.
     * @param attr attribute you want to change
     */
    public void setAttributeSaveMod(String attr){
        switch (attr){
            case "str":
                attributeSaves[0][0] = attributeSaves[0][1] == 1 ? attributes[0][1] + proficiency : attributes[0][1];
                break;
            case "dex":
                attributeSaves[1][0] = attributeSaves[1][1] == 1 ? attributes[1][1] + proficiency : attributes[1][1];
                break;
            case "con":
                attributeSaves[2][0] = attributeSaves[2][1] == 1 ? attributes[2][1] + proficiency : attributes[2][1];
                break;
            case "int":
                attributeSaves[3][0] = attributeSaves[3][1] == 1 ? attributes[3][1] + proficiency : attributes[3][1];
                break;
            case "wis":
                attributeSaves[4][0] = attributeSaves[4][1] == 1 ? attributes[4][1] + proficiency : attributes[4][1];
                break;
            case "cha":
                attributeSaves[5][0] = attributeSaves[5][1] == 1 ? attributes[5][1] + proficiency : attributes[5][1];
                break;
            default:
                break;
        }
    }

    /**
     * Method sets given attribute save.
     * @param attr attribute
     * @param toggle proficiency switch, true if has and false if hasn't
     */
    public void setAttributeSave(String attr, boolean toggle) {
        // Toggle 1 = true, 0 = false
        switch (attr){
            case "str":
                attributeSaves[0][1] = toggle ? 1 : 0;
                break;
            case "dex":
                attributeSaves[1][1] = toggle ? 1 : 0;
                break;
            case "con":
                attributeSaves[2][1] = toggle ? 1 : 0;
                break;
            case "int":
                attributeSaves[3][1] = toggle ? 1 : 0;
                break;
            case "wis":
                attributeSaves[4][1] = toggle ? 1 : 0;
                break;
            case "cha":
                attributeSaves[5][1] = toggle ? 1 : 0;
                break;
            default:
                break;
        }
        setAttributeSaveMod(attr);
    }

    /**
     * Method returns information array of given skill: score and proficiency
     * @param i index of skill
     * @return integer array holding information
     */
    public int[] getSkill(int i){
        return skills[i];
    }

    /**
     * Method returns all data of skills in a two dimensional array
     * @return 2D array of data of skills
     */
    public int[][] getSkills() {
        return skills;
    }

    /**
     * Method sets skills array
     * @param skills new 2D array
     */
    public void setSkills(int[][] skills) {
        this.skills = skills;
    }

    /**
     * Method sets the scores of skills related to given attribute
     * @param attr attribute of character
     */
    public void setSkills(String attr) {
        attr = attr.toLowerCase();
        switch (attr){
            case "str":
                skills[3][0] = skills[3][1] == 1 ? attributes[0][1] + proficiency : attributes[0][1];
                break;
            case "dex":
                skills[0][0] = skills[0][1] == 1 ? attributes[1][1] + proficiency : attributes[1][1];
                skills[15][0] = skills[15][1] == 1 ? attributes[1][1] + proficiency : attributes[1][1];
                skills[16][0] = skills[16][1] == 1 ? attributes[1][1] + proficiency : attributes[1][1];
                break;
            case "con":
                break;
            case "int":
                skills[2][0] = skills[2][1] == 1 ? attributes[3][1] + proficiency : attributes[3][1];
                skills[5][0] = skills[5][1] == 1 ? attributes[3][1] + proficiency : attributes[3][1];
                skills[8][0] = skills[8][1] == 1 ? attributes[3][1] + proficiency : attributes[3][1];
                skills[10][0] = skills[10][1] == 1 ? attributes[3][1] + proficiency : attributes[3][1];
                skills[14][0] = skills[14][1] == 1 ? attributes[3][1] + proficiency : attributes[3][1];
                break;
            case "wis":
                skills[1][0] = skills[1][1] == 1 ? attributes[4][1] + proficiency : attributes[4][1];
                skills[6][0] = skills[6][1] == 1 ? attributes[4][1] + proficiency : attributes[4][1];
                skills[9][0] = skills[9][1] == 1 ? attributes[4][1] + proficiency : attributes[4][1];
                skills[11][0] = skills[11][1] == 1 ? attributes[4][1] + proficiency : attributes[4][1];
                skills[17][0] = skills[17][1] == 1 ? attributes[4][1] + proficiency : attributes[4][1];
                break;
            case "cha":
                skills[4][0] = skills[4][1] == 1 ? attributes[5][1] + proficiency : attributes[5][1];
                skills[7][0] = skills[7][1] == 1 ? attributes[5][1] + proficiency : attributes[5][1];
                skills[12][0] = skills[12][1] == 1 ? attributes[5][1] + proficiency : attributes[5][1];
                skills[13][0] = skills[13][1] == 1 ? attributes[5][1] + proficiency : attributes[5][1];
                break;
            default:
                break;
        }
    }

    /**
     * Method sets the proficiency of skills
     * @param toggle true for proficient
     * @param index indexes of skills you want to change
     */
    public void setSkillProficiency(boolean toggle, int... index){
        for(int i : index){
            skills[i][1] = toggle ? 1 : 0;
        }
    }

    /**
     * Method sets proficiencies of given skills
     * @param toggle true for proficient
     * @param skills skills you want to change
     */
    public void setSkillProficiency(boolean toggle, String... skills){
        for(String skill : skills) {
            skill = skill.toLowerCase();
            switch(skill) {
                case "acrobatics":
                    this.skills[0][1] = toggle ? 1 : 0;
                    break;
                case "animal_handling":
                    this.skills[1][1] = toggle ? 1 : 0;
                    break;
                case "arcana":
                    this.skills[2][1] = toggle ? 1 : 0;
                    break;
                case "athletics":
                    this.skills[3][1] = toggle ? 1 : 0;
                    break;
                case "deception":
                    this.skills[4][1] = toggle ? 1 : 0;
                    break;
                case "history":
                    this.skills[5][1] = toggle ? 1 : 0;
                    break;
                case "insight":
                    this.skills[6][1] = toggle ? 1 : 0;
                    break;
                case "intimidation":
                    this.skills[7][1] = toggle ? 1 : 0;
                    break;
                case "investigation":
                    this.skills[8][1] = toggle ? 1 : 0;
                    break;
                case "medicine":
                    this.skills[9][1] = toggle ? 1 : 0;
                    break;
                case "nature":
                    this.skills[10][1] = toggle ? 1 : 0;
                    break;
                case "perception":
                    this.skills[11][1] = toggle ? 1 : 0;
                    break;
                case "performance":
                    this.skills[12][1] = toggle ? 1 : 0;
                    break;
                case "persuasion":
                    this.skills[13][1] = toggle ? 1 : 0;
                    break;
                case "religion":
                    this.skills[14][1] = toggle ? 1 : 0;
                    break;
                case "sleight_of_hand":
                    this.skills[15][1] = toggle ? 1 : 0;
                    break;
                case "stealth":
                    this.skills[16][1] = toggle ? 1 : 0;
                    break;
                case "survival":
                    this.skills[17][1] = toggle ? 1 : 0;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Method resets proficiency of skills
     */
    public void resetSkillProficiencies() {
        for(int[] skill: skills) {
            // 1 = has proficiency
            if(skill[1] == 1) {
                skill[0] -= proficiency;
                skill[1] = 0;
            }
        }
    }

    /**
     * Method randomly picks skills by class. Each class has a specific pool of skills to choose from.
     * @param cls class you want to randomize by
     */
    public void randomizeSkillsByClass(String cls){
        int [] skillPool;

        switch (cls){
            case "Barbarian":
                skillPool = new int[]{1, 3, 7, 10, 11, 17};
                skillPool = chooseSkills(2, skillPool);
                break;
            case "Bard":
                skillPool = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
                skillPool = chooseSkills(3, skillPool);
                break;
            case "Cleric":
                skillPool = new int[]{5,6,9,13,14};
                skillPool = chooseSkills(2, skillPool);
                break;
            case "Druid":
                skillPool = new int[]{1,2,6,9,10,11,14,17};
                skillPool = chooseSkills(2, skillPool);
                break;
            case "Fighter":
                skillPool = new int[]{0,1,3,5,6,7,11,17};
                skillPool = chooseSkills(2, skillPool);
                break;
            case "Monk":
                skillPool = new int[]{0,3,5,6,14,17};
                skillPool = chooseSkills(2, skillPool);
                break;
            case "Paladin":
                skillPool = new int[]{3,6,7,9,13,14};
                skillPool = chooseSkills(2, skillPool);
                break;
            case "Ranger":
                skillPool = new int[]{1,3,6,8,10,11,16,17};
                skillPool = chooseSkills(3, skillPool);
                break;
            case "Rogue":
                skillPool = new int[]{0,3,4,6,7,8,11,12,13,15,16};
                skillPool = chooseSkills(4, skillPool);
                break;
            case "Sorcerer":
                skillPool = new int[]{2,4,6,7,13,14};
                skillPool = chooseSkills(2, skillPool);
                break;
            case "Warlock":
                skillPool = new int[]{2,4,5,7,8,10,14};
                skillPool = chooseSkills(2, skillPool);
                break;
            case "Wizard":
                skillPool = new int[]{2,5,6,8,9,14};
                skillPool = chooseSkills(2, skillPool);
                break;
            default:
                skillPool = new int[]{-1};
                break;
        }

        setSkillProficiency(true, skillPool);
    }

    /**
     * Method returns array of randomly chosen skills
     * @param amount how many skills is chosen
     * @param skillPool pool of indexes to choose from
     * @return array of randomly chosen skills
     */
    public int[] chooseSkills(int amount, int[] skillPool) {
        int[] chosenSkills = new int[amount];
        for(int i =0; i<chosenSkills.length; i++) {
            boolean isTaken = true;
            while(isTaken){
                isTaken = false;
                int randomSkill = DDCC_Utils.randomInt(skillPool.length)-1;
                for(int skill : chosenSkills){
                    if(skill == randomSkill){
                        isTaken = true;
                        break;
                    }
                }
                if(!isTaken){
                    chosenSkills[i] = randomSkill;
                }
            }
        }
        return chosenSkills;
    }

    /**
     * Method returns String representation of DDCharacter class. Used for debugging.
     * @return stringified class
     */
    @Override
    public String toString() {
        return "DDCharacter{" +
                "\n\tcharacterClass='" + characterClass + '\'' +
                ",\n\t race='" + race + '\'' +
                ",\n\t background='" + background + '\'' +
                ",\n\t alignment='" + alignment + '\'' +
                ",\n\t languages=" + languages +
                ",\n\t level=" + level +
                ",\n\t proficiency=" + proficiency +
                ",\n\t health=" + health +
                ",\n\t hitDie=" + hitDie +
                ",\n\t attributes=" + DDCC_Utils.printList(attributes) +
                ",\n\t attributeSaves=" + DDCC_Utils.printList(attributeSaves) +
                ",\n\t skills=" + DDCC_Utils.printList(skills) +
                '}';
    }
}