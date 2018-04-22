package tikoot.lauri.ddcc;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DDCharacterDao {
    @Query("SELECT * FROM DDCharacter")
    List<DDCharacter> getAll();

    @Query("SELECT * FROM DDCharacter WHERE id IN (:id)")
    List<DDCharacter> loadAllByIds(int[] id);

    @Query("SELECT * FROM DDCharacter WHERE id LIKE :id LIMIT 1")
    DDCharacter findById(int id);

    @Update
    public void updateUsers(DDCharacter... characters);

    @Insert
    void insertAll(DDCharacter... characters);

    @Delete
    void delete(DDCharacter character);
}
