package tikoot.lauri.ddcc;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Class extending RoomDatabase and holding DAO of DDCharacter class
 */
@Database(entities = {DDCharacter.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DDCharacterDao characterDao();
}