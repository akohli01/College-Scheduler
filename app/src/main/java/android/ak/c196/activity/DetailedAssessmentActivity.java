package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.DueDateNotificationDao;
import android.ak.c196.database.dao.GoalDateNotificationDao;
import android.ak.c196.entity.Assessment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ToggleButton;

public class DetailedAssessmentActivity extends AppCompatActivity {

    private Assessment assessment;
    String goalDateNotificationStatus;
    String dueDateNotificationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_assessment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assessment = (Assessment) getIntent().getSerializableExtra("selectedAssessment");

        setTitle("Assessment Details");

        initializeData();
        getNotificationStatus();
        setNotificationStatus();
    }


    private void initializeData(){

        TextView assessmentTitle = findViewById(R.id.assessmentTitle);
        TextView goalDate = findViewById(R.id.goalDate);
        TextView dueDate = findViewById(R.id.dueDate);
        TextView type = findViewById(R.id.type);

        assessmentTitle.setText(assessment.getTitle());
        goalDate.setText(assessment.getGoalDate());
        dueDate.setText(assessment.getDueDate());
        type.setText(assessment.getType());

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void getNotificationStatus(){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        GoalDateNotificationDao goalDateNotificationDao= database.getGoalDateNotificationDao();
        goalDateNotificationStatus = goalDateNotificationDao.getGoalDateNotificationStatus(assessment.getAssessmentID());

        DueDateNotificationDao dueDateNotificationDao= database.getDueDateNotificationDao();
        dueDateNotificationStatus = dueDateNotificationDao.getDueDateNotificationStatus(assessment.getAssessmentID());
    }

    private void setNotificationStatus(){

        ToggleButton dueDateToggle =  findViewById(R.id.dueDate_notification);
        ToggleButton goalDateToggle =  findViewById(R.id.goalDate_notification);

        if(goalDateNotificationStatus.equals("Enabled")){
            goalDateToggle.setChecked(true);
        }

        if(dueDateNotificationStatus.equals("Enabled")){
            dueDateToggle.setChecked(true);
        }
    }
}
