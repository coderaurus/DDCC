package tikoot.lauri.ddcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CreatorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //TODO: All the Spinners need something to select the randomized value
    private Spinner classes, levels, races, backgrounds, alignments;
    private Spinner [] attributes;

    private int [] attributeIds;
    private int [] attributeModifiers;
    private int [] skills;

    private TextView health;

    private DDCharacter character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        character = (DDCharacter) getIntent().getSerializableExtra("character");
        Log.i("CreatorActivity",character.toString());

        attributes = new Spinner[6];
        attributeIds = new int[]{
                R.id.creator_attribute_str_score,
                R.id.creator_attribute_dex_score,
                R.id.creator_attribute_con_score,
                R.id.creator_attribute_int_score,
                R.id.creator_attribute_wis_score,
                R.id.creator_attribute_cha_score };
        attributeModifiers = new int[6];
        skills = new int[18];

        setContentView(R.layout.activity_creator);
        initializeSpinners();
        setHealth();
    }

    public void setHealth(){
        health = (TextView) findViewById(R.id.creator_health);
        health.setText(getHealthString());
    }

    public void initializeSpinners(){
        classes = (Spinner) findViewById(R.id.creator_class);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classes, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        classes.setAdapter(adapter);
        classes.setSelection(findSelection(classes.getAdapter(), character.getCharacterClass()));

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

        backgrounds = (Spinner) findViewById(R.id.creator_background);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.backgrounds, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        backgrounds.setAdapter(adapter);
        backgrounds.setSelection(findSelection(backgrounds.getAdapter(),
                character.getBackground()));

        alignments = (Spinner) findViewById(R.id.creator_alignment);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.alignments, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        alignments.setAdapter(adapter);
        alignments.setSelection(findSelection(alignments.getAdapter(), character.getAlignment()));

        initializeAttributeSpinners(adapter);
    }

    public void initializeAttributeSpinners(ArrayAdapter<CharSequence> adapter) {
        // Set attributes
        for(int i=0; i < attributeIds.length; i++) {
            attributes[i] = (Spinner) findViewById(attributeIds[i]);
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.attributes, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            attributes[i].setAdapter(adapter);
            attributes[i].setSelection(findSelection(attributes[i].getAdapter(),
                    String.valueOf(i)));
            //TODO: update modifier
        }
    }

    public int findSelection(SpinnerAdapter adapter, String wanted){
        for(int i=0; i<adapter.getCount(); i++){
            if(adapter.getItem(i).equals(wanted)){
                return i;
            }
        }
        return -1;
    }

    public String getHealthString() {
        return character.getHealth() + " (d" + character.getHitDie() + ")";
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch (parent.getId()){
            case R.id.creator_level:
                Log.i("CreatorActivity", "New level:" + parent.getItemAtPosition(pos).toString());
                int newLevel = Integer.parseInt(parent.getItemAtPosition(pos).toString());
                character.setHealth(newLevel);
                character.setLevel(newLevel);
                health.setText(getHealthString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}