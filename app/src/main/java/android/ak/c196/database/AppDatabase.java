package android.ak.c196.database;

import android.ak.c196.database.dao.AssessmentDao;
import android.ak.c196.database.dao.CourseDao;
import android.ak.c196.database.dao.CourseMentorJoinDao;
import android.ak.c196.database.dao.DueDateNotificationDao;
import android.ak.c196.database.dao.EndDateNotificationDao;
import android.ak.c196.database.dao.GoalDateNotificationDao;
import android.ak.c196.database.dao.MentorDao;
import android.ak.c196.database.dao.NoteDao;
import android.ak.c196.database.dao.StartDateNotificationDao;
import android.ak.c196.entity.Assessment;
import android.ak.c196.entity.Course;
import android.ak.c196.entity.CourseMentorJoin;
import android.ak.c196.entity.DueDateNotification;
import android.ak.c196.entity.EndDateNotification;
import android.ak.c196.entity.GoalDateNotification;
import android.ak.c196.entity.Mentor;
import android.ak.c196.entity.Note;
import android.ak.c196.entity.StartDateNotification;
import android.ak.c196.entity.Term;
import android.ak.c196.database.dao.TermDao;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Term.class, Course.class, Mentor.class, CourseMentorJoin.class, Note.class, Assessment.class, StartDateNotification.class, EndDateNotification.class, GoalDateNotification.class,
        DueDateNotification.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract TermDao getTermDao();
    public abstract CourseDao getCourseDao();
    public abstract NoteDao getNoteDao();
    public abstract AssessmentDao getAssessmentDao();
    public abstract StartDateNotificationDao getStartDateNotificationDao();
    public abstract EndDateNotificationDao getEndDateNotificationDao();
    public abstract GoalDateNotificationDao getGoalDateNotificationDao();
    public abstract MentorDao getMentorDao();
    public abstract CourseMentorJoinDao getCourseMentorJoinDao();
    public abstract DueDateNotificationDao getDueDateNotificationDao();


    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
