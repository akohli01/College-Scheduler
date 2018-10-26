package android.ak.c196.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "course_mentor_join",
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "courseID",
                        childColumns = "courseID",
                        onDelete = CASCADE, onUpdate = CASCADE),
                @ForeignKey(entity = Mentor.class,
                        parentColumns = "mentorID",
                        childColumns = "mentorID")
        })

public class CourseMentorJoin {

    @ColumnInfo(name = "courseID")
    private int courseID;

    @ColumnInfo(name = "mentorID")
    private int mentorID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bridgeID")
    private int bridgeID = 0;


    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getMentorID() {
        return mentorID;
    }

    public void setMentorID(int mentorID) {
        this.mentorID = mentorID;
    }

    public int getBridgeID() {
        return bridgeID;
    }

    public void setBridgeID(int bridgeID) {
        this.bridgeID = bridgeID;
    }
}
