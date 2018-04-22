package tikoot.lauri.ddcc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/*
 * Main Activity
 * This activity serves as the main hub (menu).
 */
public class MainActivity extends AppCompatActivity {

    private DDCharacter ddCharacter;

    private DatabaseService dbService;
    private ServiceConnection serviceConnection;
    private boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceConnection = new DatabaseServiceConnection();
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

    public void clicked(View view) {
        if(view.getId() == R.id.menu_button_creator) {
            Intent i = new Intent(this, CreatorActivity.class);
            // Create random character for next activity
            ddCharacter = new DDCharacter();
            i.putExtra("character", ddCharacter);
            startActivity(i);
        }
        else if(view.getId() == R.id.menu_button_exit) {
            finishAndRemoveTask();
        }
    }
    class DatabaseServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            dbService = binder.getService();
            isBound = true;
            Toast.makeText(getApplicationContext(), "Service Bound",Toast.LENGTH_LONG);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    }
}
