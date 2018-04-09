package tikoot.lauri.ddcc;

import android.content.Intent;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/*
 * Main Activity
 * This activity serves as the main hub (menu).
 */
public class MainActivity extends AppCompatActivity {

    private DDCharacter ddCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void clicked(View view) {
        if(view.getId() == R.id.menu_button_creator) {
            Log.d("MainActivity", "Clicked the creator!");
            Intent i = new Intent(this, CreatorActivity.class);
            ddCharacter = new DDCharacter();
            i.putExtra("character", ddCharacter);
            startActivity(i);
        }
    }
}
