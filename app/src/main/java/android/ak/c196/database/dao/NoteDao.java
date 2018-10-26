package android.ak.c196.database.dao;

import android.ak.c196.entity.Course;
import android.ak.c196.entity.Note;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public abstract class NoteDao extends BaseDao<Note> {

    @Query("Select * FROM note where courseID = :courseID")
    public abstract List<Note> getNotes(int courseID);

    @Query("Select * FROM note where noteID = :noteID")
    public abstract Note getNote(int noteID);
}
