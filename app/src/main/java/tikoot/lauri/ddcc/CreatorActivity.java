package tikoot.lauri.ddcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CreatorActivity extends AppCompatActivity {
    private Spinner classes, levels, races, backgrounds, alignments;
    private Spinner [] attributes;
    private int [] attributeIds;
    private int [] attributeModifiers;
    private int [] skills;
    private TextView health;
    private TextView skillsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        classes = (Spinner) findViewById(R.id.creator_class);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classes, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        classes.setAdapter(adapter);

        levels = (Spinner) findViewById(R.id.creator_level);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.levels, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        levels.setAdapter(adapter);

        races = (Spinner) findViewById(R.id.creator_race);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.races, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        races.setAdapter(adapter);

        health = (TextView) findViewById(R.id.creator_health);
        health.setText("1");

        backgrounds = (Spinner) findViewById(R.id.creator_background);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.backgrounds, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        backgrounds.setAdapter(adapter);

        alignments = (Spinner) findViewById(R.id.creator_alignment);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.alignments, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        alignments.setAdapter(adapter);

        // Set attributes
        for(int i=0; i < attributeIds.length; i++) {
            attributes[i] = (Spinner) findViewById(attributeIds[i]);
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.attributes, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            attributes[i].setAdapter(adapter);
            //TODO: update modifier
        }

        //TODO: update skills <-- requires attribute modifiers
        //TODO: print skills
    }
}