package tikoot.lauri.ddcc;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class DatabaseService extends Service {

    private AppDatabase db;
    private IBinder mBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "character").build();
        return START_STICKY;
    }


    public void saveCharacter(DDCharacter character){
        // Character is already in database => UPDATE the information
        String message = "";
        if(db.characterDao().findById(character.getId())!= null) {
            db.characterDao().updateUsers(character);
            message = "Character added";
        }
        else {
            // Add character to database
            db.characterDao().insertAll(character);
            message = "Character updated";
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
