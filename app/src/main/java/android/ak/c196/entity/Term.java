package android.ak.c196.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName= "term")
public class Term implements Serializable {

    @ColumnInfo(name = "name")
    private String termName;
    @ColumnInfo(name = "start_date")
    private String startDate;
    @ColumnInfo(name = "end_date")
    private String endDate;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "termID")
    private int termID = 0;

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
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

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }
}
