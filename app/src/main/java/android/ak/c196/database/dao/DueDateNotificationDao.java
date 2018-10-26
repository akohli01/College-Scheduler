package android.ak.c196.database.dao;

import android.ak.c196.entity.DueDateNotification;;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public abstract class DueDateNotificationDao extends BaseDao<DueDateNotification> {

    @Query("Select * FROM due_date_notification where assessmentID = :assessmentID")
    public abstract DueDateNotification getDueDateNotification(int assessmentID);

    @Query("Select status FROM due_date_notification where assessmentID = :assessmentID")
    public abstract String getDueDateNotificationStatus(int assessmentID);

    @Insert
    public abstract long insertWithReturn(DueDateNotification dueDateNotification);
}
