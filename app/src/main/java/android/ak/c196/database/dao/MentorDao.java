package android.ak.c196.database.dao;


import android.ak.c196.entity.Mentor;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public abstract class MentorDao extends BaseDao<Mentor> {

    @Insert
    public abstract long insertWithReturn(Mentor mentor);

    @Query("Select * FROM mentor where mentorID = (Select mentorID from course_mentor_join where courseID = :courseID )")
    public abstract Mentor getMentor(int courseID);

}
