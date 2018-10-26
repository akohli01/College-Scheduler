package android.ak.c196.database.dao;

import android.ak.c196.entity.Course;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public abstract class CourseDao extends BaseDao<Course>{

    @Query("Select * FROM course where termID = :termID")
    public abstract List<Course> getCourses(int termID);

    @Query("Select * FROM course where courseID = :courseID")
    public abstract Course getCourse(int courseID);

    @Delete
    public abstract void delete(Course course);

    @Insert
    public abstract long insertWithReturn(Course course);
}
