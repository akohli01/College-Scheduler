package android.ak.c196.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName= "mentor")
public class Mentor {

    @ColumnInfo(name = "name")
    private String mentorName;
    @ColumnInfo(name = "phone")
    private String phoneNumber;
    @ColumnInfo(name = "email")
    private String email;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mentorID")
    private int mentorID = 0;


    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMentorID() {
        return mentorID;
    }

    public void setMentorID(int mentorID) {
        this.mentorID = mentorID;
    }
}
