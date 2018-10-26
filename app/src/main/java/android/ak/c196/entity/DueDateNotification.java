package android.ak.c196.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName= "due_date_notification",
        foreignKeys = {
                @ForeignKey(entity = Assessment.class,
                        parentColumns = "assessmentID",
                        childColumns = "assessmentID",
                        onDelete = CASCADE, onUpdate = CASCADE),
        })
public class DueDateNotification {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int ID = 0;

    @ColumnInfo(name = "assessmentID")
    private int assessmentID;

    @ColumnInfo(name = "status")
    private String status;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}