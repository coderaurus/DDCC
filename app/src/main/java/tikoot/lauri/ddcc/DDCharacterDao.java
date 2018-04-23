package tikoot.lauri.ddcc;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * DAO class of DDCharacter.
 */
@Dao
public interface DDCharacterDao {
    /**
     * Method returns all characters found in database in List
     * @return list of characters
     */
    @Query("SELECT * FROM DDCharacter")
    List<DDCharacter> getAll();

    /**
     * Method returns character of given index found in database
     * @param id index of character
     * @return character
     */
    @Query("SELECT * FROM DDCharacter WHERE id = :id LIMIT 1")
    DDCharacter findById(int id);

    /**
     * Method updates given character(s) in database
     * @param characters array of characters to update
     */
    @Update
    public void updateUsers(DDCharacter... characters);

    /**
     * Method inserts given characters to database.
     * @param characters
     */
    @Insert
    void insertAll(DDCharacter... characters);

    /**
     * Method deletes given character from database
     * @param character
     */
    @Delete
    void delete(DDCharacter character);
}
