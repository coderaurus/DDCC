package tikoot.lauri.ddcc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class CreatorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner classes, levels, races, backgrounds, alignments;
    private Spinner [] attributes;

    private int [] attributeIds;
    private int [] attributeNames;
    private int [] attributeTexts;
    private int [][] skills;

    private TextView health;
    private TextView languages;

    private DDCharacter character;

    private DatabaseService dbService;
    private ServiceConnection serviceConnection;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        serviceConnection = new DatabaseServiceConnection();

        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_bar);
        setSupportActionBar(toolbar);

        character = (DDCharacter) getIntent().getSerializableExtra("character");

        attributes = new Spinner[6];
        attributeIds = new int[]{
                R.id.creator_attribute_str_score,
                R.id.creator_attribute_dex_score,
                R.id.creator_attribute_con_score,
                R.id.creator_attribute_int_score,
                R.id.creator_attribute_wis_score,
                R.id.creator_attribute_cha_score };

        attributeNames = new int[]{
                R.string.strength,
                R.string.dexterity,
                R.string.constitution,
                R.string.intelligence,
                R.string.wisdom,
                R.string.charisma
        };

        attributeTexts = new int[]{
                R.id.creator_attribute_str_text,
                R.id.creator_attribute_dex_text,
                R.id.creator_attribute_con_text,
                R.id.creator_attribute_int_text,
                R.id.creator_attribute_wis_text,
                R.id.creator_attribute_cha_text
        };


        initializeSpinners();
        setHealth();

        initializeSkills();
        updateSkills();

        languages = (TextView) findViewById(R.id.languages);
        updateLanguages();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, DatabaseService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private void initializeSkills() {
        skills = new int[][]{
                new int[]{R.id.creator_skills_acrobatics,        R.string.acrobatics},
                new int[]{R.id.creator_skills_animalhandling,    R.string.animalHandling},
                new int[]{R.id.creator_skills_arcana,            R.string.arcana},
                new int[]{R.id.creator_skills_athletics,         R.string.athletics},
                new int[]{R.id.creator_skills_deception,         R.string.deception},
                new int[]{R.id.creator_skills_history,           R.string.history},
                new int[]{R.id.creator_skills_insight,           R.string.insight},
                new int[]{R.id.creator_skills_intimidation,      R.string.intimidation},
                new int[]{R.id.creator_skills_investigation,     R.string.investigation},
                new int[]{R.id.creator_skills_medicine,          R.string.medicine},
                new int[]{R.id.creator_skills_nature,            R.string.nature},
                new int[]{R.id.creator_skills_perception,        R.string.perception},
                new int[]{R.id.creator_skills_performance,       R.string.performance},
                new int[]{R.id.creator_skills_persuasion,        R.string.persuasion},
                new int[]{R.id.creator_skills_religion,          R.string.religion},
                new int[]{R.id.creator_skills_sleightofhand,     R.string.sleightOfHand},
                new int[]{R.id.creator_skills_stealth,           R.string.stealth},
                new int[]{R.id.creator_skills_survival,          R.string.survival}
        };
    }

    private void updateAttributeText(int... i) {
        if(i.length > 0){
            int [][] saves = character.getAttributeSaves();
            TextView view = findViewById(attributeTexts[i[0]]);
            String str = getResources().getString(attributeNames[i[0]]);
            str += saves[i[0]][0] > -1 ? " (+" + saves[i[0]][0] + ")" :
                    " (" + saves[i[0]][0] + ")";

            view.setText(str);

            if(saves[i[0]][1] == 1){
                boldText(view, true);
            }
            else {
                boldText(view, false);
            }
            view.invalidate();
        }
        else {
            for(int j = 0; j < attributeTexts.length; j++) {
                int [][] saves = character.getAttributeSaves();
                TextView view = findViewById(attributeTexts[j]);
                String str = getResources().getString(attributeNames[j]);
                str += saves[j][0] > -1 ? " (+" + saves[j][0] + ")" :
                        " (" + saves[j][0] + ")";

                view.setText(str);

                if(saves[j][1] == 1){
                    boldText(view, true);
                }
                else {
                    boldText(view, false);
                }
                view.invalidate();
            }
        }
    }

    private void updateAttributes() {
        for(int i=0; i<attributes.length; i++){
            attributes[i].setSelection(findSelection(attributes[i].getAdapter(),
                    String.valueOf(character.getAttribute(i))));
            updateAttributeText(i);
        }
    }

    private void updateLanguages() {
        String text = "";
        for(String lang : character.getLanguages().get(0)){
                text += lang + "  ";
        }
        for(String lang : character.getLanguages().get(1)){
                text += lang + "  ";
        }
        languages.setText(text);
    }

    private void updateSkills() {
        for(int i=0; i<skills.length; i++){
            TextView textView = (TextView) findViewById(skills[i][0]);

            String text = character.getSkill(i)[0] > -1 ? "(+" + character.getSkill(i)[0] + ")" :
                    "(" + character.getSkill(i)[0] + ")";
            text += " " + getResources().getString(skills[i][1]);

            textView.setText(text);

            if(character.getSkill(i)[1] == 1){
                boldText(textView, true);
            }
            else {
                boldText(textView, false);
            }
            textView.invalidate();
        }
    }

    private void boldText(TextView skill, boolean proficient) {
        if(proficient){
            skill.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), 1);
        }
        else {
            skill.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), 0);
        }
    }

    public void setHealth(){
        health = (TextView) findViewById(R.id.creator_health);
        updateHealth();
    }

    public void initializeSpinners(){
        classes = (Spinner) findViewById(R.id.creator_class);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classes, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        classes.setAdapter(adapter);
        classes.setSelection(findSelection(classes.getAdapter(), character.getCharacterClass()));
        classes.setOnItemSelectedListener(this);

        levels = (Spinner) findViewById(R.id.creator_level);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.levels, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        levels.setAdapter(adapter);
        levels.setSelection(findSelection(levels.getAdapter(), String.valueOf(character.getLevel())));
        levels.setOnItemSelectedListener(this);

        races = (Spinner) findViewById(R.id.creator_race);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.races, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        races.setAdapter(adapter);
        races.setSelection(findSelection(races.getAdapter(), character.getRace()));
        races.setOnItemSelectedListener(this);

        backgrounds = (Spinner) findViewById(R.id.creator_background);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.backgrounds, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        backgrounds.setAdapter(adapter);
        backgrounds.setSelection(findSelection(backgrounds.getAdapter(),
                character.getBackground()));
        backgrounds.setOnItemSelectedListener(this);

        alignments = (Spinner) findViewById(R.id.creator_alignment);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.alignments, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        alignments.setAdapter(adapter);
        alignments.setSelection(findSelection(alignments.getAdapter(), character.getAlignment()));
        alignments.setOnItemSelectedListener(this);

        initializeAttributeSpinners(adapter);
    }

    private void initializeAttributeSpinners(ArrayAdapter<CharSequence> adapter) {
        // Set attributes
        for(int i=0; i < attributeIds.length; i++) {
            attributes[i] = (Spinner) findViewById(attributeIds[i]);
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.attributes, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            attributes[i].setAdapter(adapter);
            attributes[i].setSelection(findSelection(attributes[i].getAdapter(),
                    String.valueOf(character.getAttribute(i))));
            attributes[i].setOnItemSelectedListener(this);
            //TODO: update modifier
        }
    }

    private int findSelection(SpinnerAdapter adapter, String wanted){
        for(int i=0; i<adapter.getCount(); i++){
            if(adapter.getItem(i).equals(wanted)){
                return i;
            }
        }
        return -1;
    }

    private String getHealthString() {
        return character.getHealth() + " (d" + character.getHitDie() + ")";
    }

    private void updateHealth(){
        health.setText(getHealthString());
        health.invalidate();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.d("CreatorActivity--", parent.getItemAtPosition(pos).toString());
        switch (parent.getId()){
            case R.id.creator_level:
                int newLevel = Integer.parseInt(parent.getItemAtPosition(pos).toString());
                character.setHealth(newLevel);
                character.setLevel(newLevel);
                updateHealth();
                break;
            case R.id.creator_class:
                character.setCharacterClass(parent.getItemAtPosition(pos).toString());
                character.setHealth(character.getLevel());
                updateAttributeText();
                updateHealth();
                break;
            case R.id.creator_race:
                character.setRace(parent.getItemAtPosition(pos).toString());
                updateLanguages();
                updateAttributes();
                updateSkills();
                break;
            case R.id.creator_background:
                character.setBackground(parent.getItemAtPosition(pos).toString());
                updateLanguages();
                break;
            case R.id.creator_alignment:
                character.setAlignment(parent.getItemAtPosition(pos).toString());
                break;
            case R.id.creator_attribute_str_score:
                character.setAttribute("str", Integer.parseInt(parent.getItemAtPosition(pos).toString()));
                updateAttributeText(0);
                updateSkills();
                break;
            case R.id.creator_attribute_dex_score:
                character.setAttribute("dex", Integer.parseInt(parent.getItemAtPosition(pos).toString()));
                updateAttributeText(1);
                updateSkills();
                break;
            case R.id.creator_attribute_con_score:
                character.setAttribute("con", Integer.parseInt(parent.getItemAtPosition(pos).toString()));
                character.setHealth(character.getLevel());
                updateAttributeText(2);
                updateSkills();
                updateHealth();
                break;
            case R.id.creator_attribute_int_score:
                character.setAttribute("int", Integer.parseInt(parent.getItemAtPosition(pos).toString()));
                updateAttributeText(3);
                updateSkills();
                break;
            case R.id.creator_attribute_wis_score:
                character.setAttribute("wis", Integer.parseInt(parent.getItemAtPosition(pos).toString()));
                updateAttributeText(4);
                updateSkills();
                break;
            case R.id.creator_attribute_cha_score:
                character.setAttribute("cha", Integer.parseInt(parent.getItemAtPosition(pos).toString()));
                updateAttributeText(5);
                updateSkills();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void recreateCharacter(){
        character = new DDCharacter();

        levels.setSelection(findSelection(levels.getAdapter(), String.valueOf(character.getLevel())));
        classes.setSelection(findSelection(classes.getAdapter(), character.getCharacterClass()));
        races.setSelection(findSelection(races.getAdapter(), character.getRace()));
        backgrounds.setSelection(findSelection(backgrounds.getAdapter(),
                character.getBackground()));
        alignments.setSelection(findSelection(alignments.getAdapter(), character.getAlignment()));

        setHealth();
        updateHealth();
        updateAttributes();
        updateAttributeText();
        updateSkills();
        updateLanguages();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create:
                recreateCharacter();
                return true;
            case R.id.menu_save:
                // dbService.saveCharacter(character);
                return true;
            case R.id.menu_load:
                // dbService.loadCharacter();
                return true;
            case R.id.menu_help:
                // TODO: Implement help documentation fragemnt
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.creator_menu, menu);
        return true;
    }

    class DatabaseServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            dbService = binder.getService();
            isBound = true;
            Toast.makeText(getApplicationContext(), "Service Bound",Toast.LENGTH_SHORT);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    }
}