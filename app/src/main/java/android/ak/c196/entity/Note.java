package android.ak.c196.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName= "note",
        foreignKeys = {
        @ForeignKey(entity = Course.class,
                parentColumns = "courseID",
                childColumns = "courseID",
                onDelete = CASCADE, onUpdate = CASCADE),
         })
public class Note implements Serializable {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteID")
    private int noteID = 0;

    @ColumnInfo(name = "courseID")
    private int courseID;

    @ColumnInfo(name = "location")
    private String location;

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
