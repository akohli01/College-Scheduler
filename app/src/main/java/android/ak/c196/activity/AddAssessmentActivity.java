package android.ak.c196.activity;

import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.AssessmentDao;
import android.ak.c196.database.dao.DueDateNotificationDao;
import android.ak.c196.database.dao.GoalDateNotificationDao;
import android.ak.c196.entity.Assessment;
import android.ak.c196.entity.DueDateNotification;
import android.ak.c196.entity.GoalDateNotification;
import android.ak.c196.utility.NullChecker;
import android.ak.c196.utility.ReusableDatePicker;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.ak.c196.R;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAssessmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int courseID;
    private String type;
    private boolean isGoalDateNotificationEnabled = false;
    private boolean isDueDateNotificationEnabled = false;
    private long generatedAssessmentID;
    private long generatedGoalDateNotificationID;
    private long generatedDueDateNotificationID;
    private Assessment assessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit_assessment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseID = getIntent().getExtras().getInt("courseID");

        initializeListeners();
        initializeSpinner();

    }

    private void createNotification() throws ParseException {

        if(isGoalDateNotificationEnabled)
            createGoalDateNotification();
        if(isDueDateNotificationEnabled)
            createDueDateNotification();

        if(!isGoalDateNotificationEnabled){
            GoalDateNotification goalDateNotification = new GoalDateNotification();
            goalDateNotification.setAssessmentID((int)generatedAssessmentID);
            goalDateNotification.setStatus("Disabled");
            insertGoalDateNotificationIntoDatabase(goalDateNotification);
        }

        if(!isDueDateNotificationEnabled){
            DueDateNotification dueDateNotification = new DueDateNotification();
            dueDateNotification.setAssessmentID((int)generatedAssessmentID);
            dueDateNotification.setStatus("Disabled");
            insertDueDateNotificationIntoDatabase(dueDateNotification);
        }
    }

    private void createGoalDateNotification() throws ParseException {
        GoalDateNotification goalDateNotification = new GoalDateNotification();
        goalDateNotification.setAssessmentID((int)generatedAssessmentID);
        goalDateNotification.setStatus("Enabled");

        insertGoalDateNotificationIntoDatabase(goalDateNotification);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        EditText startDate =findViewById(R.id.goalDate);


        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(startDate.getText().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.setTime(date1);

        Intent intent = new Intent("goalDateAction");
        intent.putExtra("title", "Assessment Notification");
        intent.putExtra("contentText", assessment.getTitle() + "'s goal date is today!");
        intent.putExtra("notificationID", generatedGoalDateNotificationID);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 103, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
    }

    private void createDueDateNotification() throws ParseException {
        DueDateNotification dueDateNotification = new DueDateNotification();
        dueDateNotification.setAssessmentID((int)generatedAssessmentID);
        dueDateNotification.setStatus("Enabled");

        insertDueDateNotificationIntoDatabase(dueDateNotification);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        EditText dueDate =findViewById(R.id.dueDate);


        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(dueDate.getText().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.setTime(date1);

        Intent intent = new Intent("dueDateAction");
        intent.putExtra("title", "Assessment Notification");
        intent.putExtra("contentText", assessment.getTitle() + " ends today!");
        intent.putExtra("notificationID", generatedDueDateNotificationID);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 104, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
    }


    private void insertGoalDateNotificationIntoDatabase(GoalDateNotification goalDateNotification){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        GoalDateNotificationDao goalDateNotificationDao = database.getGoalDateNotificationDao();
        generatedGoalDateNotificationID = goalDateNotificationDao.insertWithReturn(goalDateNotification);
    }

    private void insertDueDateNotificationIntoDatabase(DueDateNotification dueDateNotification){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        DueDateNotificationDao dueDateNotificationDao = database.getDueDateNotificationDao();
        generatedDueDateNotificationID = dueDateNotificationDao.insertWithReturn(dueDateNotification);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void createAssessment(){

        EditText assessmentTitle = findViewById(R.id.assessmentTitle);
        EditText dueDate = findViewById(R.id.dueDate);
        EditText goalDate = findViewById(R.id.goalDate);

        assessment = new Assessment();
        assessment.setTitle(assessmentTitle.getText().toString());
        assessment.setDueDate(dueDate.getText().toString());
        assessment.setGoalDate(goalDate.getText().toString());
        assessment.setType(type);
        assessment.setCourseID(courseID);

        insertIntoDatabase(assessment);
    }

    private void insertIntoDatabase(Assessment assessment){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        AssessmentDao assessmentDao = database.getAssessmentDao();
        generatedAssessmentID = assessmentDao.insertWithReturn(assessment);

        finish();

    }

    private int checkForNull(){
        NullChecker nullChecker = new NullChecker();

        if(nullChecker.checkForNull(findViewById(R.id.parent_view)) == -1){
            Toast.makeText(this, "Please fill in all null fields or press the back button to cancel", Toast.LENGTH_SHORT).show();
            return -1;
        }

        return 1;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void initializeListeners(){
        Button addTerm = findViewById(R.id.add);

        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForNull() != -1 ){
                    createAssessment();
                    try {
                        createNotification();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        EditText dueDate =findViewById(R.id.dueDate);
        EditText goalDate =findViewById(R.id.goalDate);

        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new ReusableDatePicker();
                Bundle args = new Bundle();
                args.putInt("key", v.getId());
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "date picker");
            }
        });

        goalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ReusableDatePicker();
                Bundle args = new Bundle();
                args.putInt("key", v.getId());
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "date picker");
            }
        });

        ToggleButton dueDateToggle =  findViewById(R.id.dueDate_notification);
        dueDateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDueDateNotificationEnabled = true;
                    Toast.makeText(getApplicationContext(), "Due Date notification is enabled", Toast.LENGTH_SHORT).show();
                } else {
                    isDueDateNotificationEnabled = false;
                    Toast.makeText(getApplicationContext(), "Due Date notification is disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ToggleButton goalDateToggle =  findViewById(R.id.goalDate_notification);
        goalDateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isGoalDateNotificationEnabled = true;
                    Toast.makeText(getApplicationContext(), "Goal Date notification is enabled", Toast.LENGTH_SHORT).show();
                } else {
                    isGoalDateNotificationEnabled = false;
                    Toast.makeText(getApplicationContext(), "Goal Date notification is disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initializeSpinner(){
        Spinner spinner = findViewById(R.id.assessment_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_type_array,android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        type = (String)parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
