package android.ak.c196.database.dao;

import android.ak.c196.entity.Assessment;
import android.ak.c196.entity.Course;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public abstract class AssessmentDao extends BaseDao<Assessment> {

    @Query("Select * FROM assessment where courseID = :courseID")
    public abstract List<Assessment> getAssessments(int courseID);

    @Insert
    public abstract long insertWithReturn(Assessment assessment);
}
