package android.ak.c196.database.dao;

import android.ak.c196.entity.StartDateNotification;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


@Dao
public abstract class StartDateNotificationDao extends BaseDao<StartDateNotification> {

    @Query("Select * FROM start_date_notification where courseID = :courseID")
    public abstract StartDateNotification getStartDateNotification(int courseID);

    @Insert
    public abstract long insertWithReturn(StartDateNotification startDateNotification);
}
