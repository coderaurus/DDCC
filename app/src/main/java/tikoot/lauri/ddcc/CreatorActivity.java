package tikoot.lauri.ddcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreatorActivity extends AppCompatActivity {
    private Spinner classes, levels, races, backgrounds, alignments;
    private Spinner [] attributes = new Spinner[6];
    private int [] attributeModifiers = new int[6];
    private int [] skills = new int[18];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }
}
