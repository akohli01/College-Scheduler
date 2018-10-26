package android.ak.c196.database.dao;

import android.ak.c196.entity.GoalDateNotification;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public abstract class GoalDateNotificationDao extends BaseDao<GoalDateNotification> {

    @Query("Select * FROM goal_date_notification where assessmentID = :assessmentID")
    public abstract GoalDateNotification getGoalDateNotification(int assessmentID);

    @Query("Select status FROM goal_date_notification where assessmentID = :assessmentID")
    public abstract String getGoalDateNotificationStatus(int assessmentID);

    @Insert
    public abstract long insertWithReturn(GoalDateNotification goalDateNotification);
}
