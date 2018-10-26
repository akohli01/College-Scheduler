package android.ak.c196.database.dao;

import android.ak.c196.entity.EndDateNotification;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


@Dao
public abstract class EndDateNotificationDao extends BaseDao<EndDateNotification> {

    @Query("Select * FROM end_date_notification where courseID = :courseID")
    public abstract EndDateNotification getEndDateNotification(int courseID);

    @Insert
    public abstract long insertWithReturn(EndDateNotification endDateNotification);
}
