package android.ak.c196.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "course",
        foreignKeys = @ForeignKey(entity = Term.class,
        parentColumns = "termID",
        childColumns = "termID",
        onDelete = CASCADE, onUpdate = CASCADE))

public class Course implements Serializable {

    @ColumnInfo(name = "name")
    private String courseName;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "courseID")
    private int courseID = 0;

    @ColumnInfo(name = "start_date")
    private String startDate;
    @ColumnInfo(name = "end_date")
    private String endDate;
    @ColumnInfo(name ="status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private int termID;

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
