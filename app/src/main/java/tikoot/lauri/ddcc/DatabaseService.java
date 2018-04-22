package tikoot.lauri.ddcc;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class DatabaseService extends Service{

    private AppDatabase db;
    private IBinder mBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "character").build();
        return mBinder;
    }

    @Override
    public void onCreate() {
        mBinder = new LocalBinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void loadCharacter(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... contexts) {
                DDCharacter loaded = db.characterDao().findById(1);
                Log.d("DatabaseService", "\n\n--- Loaded Character:\n");
                Log.d("DatabaseService", loaded.toString());
                return null;
            }
        }.execute();
    }

    public void saveCharacter(DDCharacter character){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... contexts) {
                String message = " --- Character " + character.getId();
                if (db.characterDao().findById(character.getId()) != null) {
                    db.characterDao().updateUsers(character);
                    message += " added";
                } else {
                    // Add character to database
                    db.characterDao().insertAll(character);
                    message += " updated";
                }
                Log.d("DatabaseService", message);
                return null;
            }
        }.execute();
        Toast.makeText(getApplicationContext(), "Save successful", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
