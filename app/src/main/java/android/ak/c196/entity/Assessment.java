package android.ak.c196.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName= "assessment",
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "courseID",
                        childColumns = "courseID",
                        onDelete = CASCADE, onUpdate = CASCADE),
        })
@SuppressWarnings("serial")
public class Assessment implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "assessmentID")
    private int assessmentID = 0;

    @ColumnInfo(name = "courseID")
    private int courseID;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "due_date")
    private String dueDate;

    @ColumnInfo(name = "goal_date")
    private String goalDate;

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String goalDate) {
        this.goalDate = goalDate;
    }
}
