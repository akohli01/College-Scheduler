package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.CourseDao;
import android.ak.c196.database.dao.CourseMentorJoinDao;
import android.ak.c196.database.dao.EndDateNotificationDao;
import android.ak.c196.database.dao.MentorDao;
import android.ak.c196.database.dao.StartDateNotificationDao;
import android.ak.c196.entity.Course;
import android.ak.c196.entity.CourseMentorJoin;
import android.ak.c196.entity.EndDateNotification;
import android.ak.c196.entity.Mentor;
import android.ak.c196.entity.StartDateNotification;
import android.ak.c196.utility.NullChecker;
import android.ak.c196.utility.ReusableDatePicker;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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
import java.util.Objects;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int termID;
    private String status;
    private boolean isStartDateNotificationEnabled = false;
    private boolean isEndDateNotificationEnabled = false;
    private long generatedCourseID;
    private long generatedStartDateNotificationID;
    private long generatedEndDateNotificationID;
    private long generatedMentorID;
    private Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit_course);
        setTitle("Add Course");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termID = Objects.requireNonNull(getIntent().getExtras()).getInt("termID");

        initializeListeners();
        initializeSpinner();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void createCourse(){

        EditText courseName = findViewById(R.id.courseName);
        EditText startDate = findViewById(R.id.startDate);
        EditText endDate = findViewById(R.id.endDate);

        course = new Course();
        course.setCourseName(courseName.getText().toString());
        course.setStartDate(startDate.getText().toString());
        course.setEndDate(endDate.getText().toString());
        course.setStatus(status);
        course.setTermID(termID);

        insertIntoDatabase(course);
    }


    private void createMentor(){
        EditText mentorName = findViewById(R.id.mentorName);
        EditText mentorEmail = findViewById(R.id.mentorEmail);
        EditText mentorPhoneNumber = findViewById(R.id.mentorPhone);

        Mentor mentor = new Mentor();
        mentor.setMentorName(mentorName.getText().toString());
        mentor.setEmail(mentorEmail.getText().toString());
        mentor.setPhoneNumber(mentorPhoneNumber.getText().toString());

        insertIntoDatabase(mentor);
    }

    private void createCourseAndMentorBridge(){

        CourseMentorJoin courseMentorJoin = new CourseMentorJoin();
        courseMentorJoin.setCourseID((int)generatedCourseID);
        courseMentorJoin.setMentorID((int)generatedMentorID);

        insertIntoDatabase(courseMentorJoin);
    }

    private void insertIntoDatabase(Course course){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        CourseDao courseDAO = database.getCourseDao();
        generatedCourseID = courseDAO.insertWithReturn(course);

    }

    private void insertIntoDatabase(Mentor mentor){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        MentorDao mentorDao = database.getMentorDao();
        generatedMentorID = mentorDao.insertWithReturn(mentor);
    }

    private void insertIntoDatabase(CourseMentorJoin courseMentorJoin){

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        CourseMentorJoinDao courseMentorJoinDao = database.getCourseMentorJoinDao();
        courseMentorJoinDao.insert(courseMentorJoin);
    }


    private void createNotification() throws ParseException {

            if(isStartDateNotificationEnabled)
                createStartDateNotification();
            if(isEndDateNotificationEnabled)
                createEndDateNotification();
            if(!isStartDateNotificationEnabled){
                StartDateNotification startDateNotification = new StartDateNotification();
                startDateNotification.setCourseID((int)generatedCourseID);
                startDateNotification.setStatus("Disabled");
                insertStartDateNotificationIntoDatabase(startDateNotification);
            }
            if(!isEndDateNotificationEnabled){
                EndDateNotification endDateNotification = new EndDateNotification();
                endDateNotification.setCourseID((int)generatedCourseID);
                endDateNotification.setStatus("Disabled");
                insertEndDateNotificationIntoDatabase(endDateNotification);
            }
    }

    private void createStartDateNotification() throws ParseException {
        StartDateNotification startDateNotification = new StartDateNotification();
        startDateNotification.setCourseID((int)generatedCourseID);
        startDateNotification.setStatus("Enabled");

        insertStartDateNotificationIntoDatabase(startDateNotification);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        EditText startDate =findViewById(R.id.startDate);


        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(startDate.getText().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.setTime(date1);

        Intent intent = new Intent("startDateAction");
        intent.putExtra("title", "Course Start Notification");
        intent.putExtra("contentText", course.getCourseName() + " starts today!");
        intent.putExtra("notificationID", generatedStartDateNotificationID);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);

    }

    private void createEndDateNotification() throws ParseException {
        EndDateNotification endDateNotification = new EndDateNotification();
        endDateNotification.setCourseID((int)generatedCourseID);
        endDateNotification.setStatus("Enabled");

        insertEndDateNotificationIntoDatabase(endDateNotification);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        EditText endDate =findViewById(R.id.endDate);


        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(endDate.getText().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.setTime(date1);


        Intent intent = new Intent("endDateAction");
        intent.putExtra("title", "Course End Notification");
        intent.putExtra("contentText", course.getCourseName() + " ends today!");
        intent.putExtra("notificationID", generatedEndDateNotificationID);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);

    }

    private void insertStartDateNotificationIntoDatabase(StartDateNotification startDateNotification){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        StartDateNotificationDao startDateNotificationDao = database.getStartDateNotificationDao();
        generatedStartDateNotificationID = startDateNotificationDao.insertWithReturn(startDateNotification);
    }

    private void insertEndDateNotificationIntoDatabase(EndDateNotification endDateNotification){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        EndDateNotificationDao endDateNotificationDao = database.getEndDateNotificationDao();
        generatedEndDateNotificationID = endDateNotificationDao.insertWithReturn(endDateNotification);
    }

    private int checkForNull(){
        NullChecker nullChecker = new NullChecker();

        if(nullChecker.checkForNull(findViewById(R.id.parent_view)) == -1){
            Toast.makeText(this, "Please fill in all null fields or press the back button to cancel", Toast.LENGTH_SHORT).show();
            return -1;
        }

        return 1;
    }


    private void initializeListeners(){
        Button addTerm = findViewById(R.id.add);
        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkForNull() !=-1){
                    createCourse();
                    createMentor();
                    createCourseAndMentorBridge();
                    try {
                        createNotification();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    finish();
                }

            }
        });

        EditText startDate =findViewById(R.id.startDate);
        EditText endDate =findViewById(R.id.endDate);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new ReusableDatePicker();
                Bundle args = new Bundle();
                args.putInt("key", v.getId());
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "date picker");
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ReusableDatePicker();
                Bundle args = new Bundle();
                args.putInt("key", v.getId());
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "date picker");
            }
        });

        ToggleButton startDateToggle =  findViewById(R.id.startDate_notification);
        startDateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isStartDateNotificationEnabled = true;
                    Toast.makeText(getApplicationContext(), "Start Date notification is enabled", Toast.LENGTH_SHORT).show();
                } else {
                    isStartDateNotificationEnabled = false;
                    Toast.makeText(getApplicationContext(), "Start Date notification is disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ToggleButton endDateToggle =  findViewById(R.id.endDate_notification);
        endDateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isEndDateNotificationEnabled = true;
                    Toast.makeText(getApplicationContext(), "End Date notification is enabled", Toast.LENGTH_SHORT).show();
                } else {
                    isEndDateNotificationEnabled = false;
                    Toast.makeText(getApplicationContext(), "End Date notification is disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initializeSpinner(){
        Spinner spinner = findViewById(R.id.status_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.course_status_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        status = (String)parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
