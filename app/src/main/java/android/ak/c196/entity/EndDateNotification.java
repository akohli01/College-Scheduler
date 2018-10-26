package android.ak.c196.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName= "end_date_notification",
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "courseID",
                        childColumns = "courseID",
                        onDelete = CASCADE, onUpdate = CASCADE),
        })
public class EndDateNotification {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int ID = 0;

    @ColumnInfo(name = "courseID")
    private int courseID;

    @ColumnInfo(name = "status")
    private String status;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}