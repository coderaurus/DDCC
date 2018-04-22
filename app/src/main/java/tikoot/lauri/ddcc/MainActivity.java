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

    /**
     * On this method connection to database service is declared.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceConnection = new DatabaseServiceConnection();
    }

    /**
     * Method binds database service to package ocntext.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, DatabaseService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Method unbinds the connection to database service.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if(isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    /**
     * Method handles clicking on menu buttons.
     * - Creator creates DDCharacter (bad practicse, needs to be fixed) and sends it to CreatorActivity
     * - Exit simply closes the software
     * @param view clicked button
     */
    public void clicked(View view) {
        if(view.getId() == R.id.menu_button_creator) {
            Intent i = new Intent(this, CreatorActivity.class);
            // Create random character for next activity
            // TODO: fix this shit
            ddCharacter = new DDCharacter();
            i.putExtra("character", ddCharacter);
            startActivity(i);
        }
        else if(view.getId() == R.id.menu_button_exit) {
            finishAndRemoveTask();
        }
    }

    /**
     * Inner class for handling database connection
     */
    class DatabaseServiceConnection implements ServiceConnection {

        /**
         * Method is called when service is connected. Binds the service through LocalBinder
         * @param componentName
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            dbService = binder.getService();
            isBound = true;
        }

        /**
         * Method ticks isBound check off.
         * @param componentName
         */
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    }
}
