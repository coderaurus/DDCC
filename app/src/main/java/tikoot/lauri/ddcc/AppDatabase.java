package tikoot.lauri.ddcc;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {DDCharacter.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DDCharacterDao characterDao();

}
