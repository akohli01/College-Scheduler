package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.CourseDao;
import android.ak.c196.database.dao.EndDateNotificationDao;
import android.ak.c196.database.dao.MentorDao;
import android.ak.c196.database.dao.StartDateNotificationDao;
import android.ak.c196.entity.Course;
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

public class EditCourseActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private String status;
    private StartDateNotification startDateNotification;
    private EndDateNotification endDateNotification;
    private boolean isStartDateNotificationEnabled = false;
    private boolean isEndDateNotificationEnabled = false;
    private Course course;
    private Mentor mentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_or_edit_course);
        setTitle("Edit Course");

        course = (Course) getIntent().getSerializableExtra("selectedCourse");

        getNotificationStatus();
        getMentorInformation();

        setInputs();

        initializeListeners();
        initializeSpinner();

    }

    private void getNotificationStatus(){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        StartDateNotificationDao startDateNotificationDao = database.getStartDateNotificationDao();
        this.startDateNotification = startDateNotificationDao.getStartDateNotification(course.getCourseID());

        EndDateNotificationDao endDateNotificationDao = database.getEndDateNotificationDao();
        this.endDateNotification = endDateNotificationDao.getEndDateNotification(course.getCourseID());
    }

    private void updateCourse(){

        EditText courseName = findViewById(R.id.courseName);
        EditText startDate = findViewById(R.id.startDate);
        EditText endDate = findViewById(R.id.endDate);

        course.setCourseName(courseName.getText().toString());
        course.setStartDate(startDate.getText().toString());
        course.setEndDate(endDate.getText().toString());
        course.setStatus(status);

        insertIntoDatabase(course);
    }

    private void updateMentor(){
        EditText mentorName = findViewById(R.id.mentorName);
        EditText mentorEmail = findViewById(R.id.mentorEmail);
        EditText mentorPhoneNumber = findViewById(R.id.mentorPhone);

        mentor.setMentorName(mentorName.getText().toString());
        mentor.setEmail(mentorEmail.getText().toString());
        mentor.setPhoneNumber(mentorPhoneNumber.getText().toString());

        insertIntoDatabase(mentor);
    }

    private void insertIntoDatabase(Course course){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        CourseDao courseDAO = database.getCourseDao();
        courseDAO.update(course);

        finish();
    }

    private void insertIntoDatabase(Mentor mentor){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        MentorDao mentorDao = database.getMentorDao();
        mentorDao.update(mentor);

    }


    private void createNotification() throws ParseException {


        if(isStartDateNotificationEnabled) {
            createStartDateNotification();

        }
        if(isEndDateNotificationEnabled) {
            createEndDateNotification();

        }
        if(!isStartDateNotificationEnabled){

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent("startDateAction");
            intent.putExtra("title", "Course Start Notification");
            intent.putExtra("contentText", course.getCourseName() + " starts today!");
            intent.putExtra("notificationID", startDateNotification.getID());

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(broadcast);


              startDateNotification.setStatus("Disabled");
            insertStartDateNotificationIntoDatabase(startDateNotification);
        }
        if(!isEndDateNotificationEnabled){

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


            Intent intent = new Intent("endDateAction");
            intent.putExtra("title", "Course End Notification");
            intent.putExtra("contentText", course.getCourseName() + " ends today!");
            intent.putExtra("notificationID", endDateNotification.getID());

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(broadcast);


            endDateNotification.setStatus("Disabled");
            insertEndDateNotificationIntoDatabase(endDateNotification);
        }

    }

    private void createStartDateNotification() throws ParseException {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent("startDateAction");
        intent.putExtra("title", "Course Start Notification");
        intent.putExtra("contentText", course.getCourseName() + " starts today!");
        intent.putExtra("notificationID", startDateNotification.getID());

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(broadcast);


        startDateNotification.setStatus("Enabled");

        insertStartDateNotificationIntoDatabase(startDateNotification);


        EditText startDate =findViewById(R.id.startDate);


        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(startDate.getText().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.setTime(date1);


        intent = new Intent("startDateAction");
        intent.putExtra("title", "Course Start Notification");
        intent.putExtra("contentText", course.getCourseName() + " starts today!");
        intent.putExtra("notificationID", startDateNotification.getID());

        broadcast = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);

    }

    private void createEndDateNotification() throws ParseException {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        Intent intent = new Intent("endDateAction");
        intent.putExtra("title", "Course End Notification");
        intent.putExtra("contentText", course.getCourseName() + " ends today!");
        intent.putExtra("notificationID", endDateNotification.getID());

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(broadcast);


        endDateNotification.setStatus("Enabled");

        insertEndDateNotificationIntoDatabase(endDateNotification);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        EditText endDate =findViewById(R.id.endDate);


        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(endDate.getText().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.setTime(date1);

       intent = new Intent("endDateAction");
        intent.putExtra("title", "Course End Notification");
        intent.putExtra("contentText", course.getCourseName() + " ends today!");
        intent.putExtra("notificationID", endDateNotification.getID());

        broadcast = PendingIntent.getBroadcast(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);

    }

    private void insertStartDateNotificationIntoDatabase(StartDateNotification startDateNotification){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        StartDateNotificationDao startDateNotificationDao = database.getStartDateNotificationDao();
        startDateNotificationDao.update(startDateNotification);
    }

    private void insertEndDateNotificationIntoDatabase(EndDateNotification endDateNotification){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        EndDateNotificationDao endDateNotificationDao = database.getEndDateNotificationDao();
        endDateNotificationDao.update(endDateNotification);
    }


    private int checkForNull(){
        NullChecker nullChecker = new NullChecker();

        if(nullChecker.checkForNull(findViewById(R.id.parent_view)) == -1){
            Toast.makeText(this, "Please fill in all null fields or press the back button to cancel", Toast.LENGTH_SHORT).show();
            return -1;
        }

        return 1;
    }

    private void getMentorInformation(){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        MentorDao mentorDao = database.getMentorDao();
        mentor = mentorDao.getMentor(course.getCourseID());

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
        Button updateTerm = findViewById(R.id.add);
        updateTerm.setText("Update");
        updateTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForNull() !=-1){
                    updateCourse();
                    updateMentor();
                    try {
                        createNotification();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    finish();
                }
            }
        });

        final EditText startDate =findViewById(R.id.startDate);
        final EditText endDate =findViewById(R.id.endDate);


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new ReusableDatePicker();
                Bundle args = new Bundle();
                args.putInt("key", v.getId());
                args.putString("startDate", startDate.getText().toString());
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
                args.putString("endDate", endDate.getText().toString());
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

        if(startDateNotification.getStatus().equals("Enabled")){
            startDateToggle.setChecked(true);
        }

        if(endDateNotification.getStatus().equals("Enabled")){
            endDateToggle.setChecked(true);
        }

    }

    private void initializeSpinner(){
        Spinner spinner =  findViewById(R.id.status_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.course_status_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void setInputs(){
        EditText courseName = findViewById(R.id.courseName);
        final EditText startDate =findViewById(R.id.startDate);
        final EditText endDate =findViewById(R.id.endDate);

        courseName.setText(course.getCourseName());
        startDate.setText(course.getStartDate());
        endDate.setText(course.getEndDate());

        EditText mentorName = findViewById(R.id.mentorName);
        EditText mentorEmail =findViewById(R.id.mentorEmail);
        EditText mentorPhone =findViewById(R.id.mentorPhone);


        mentorName.setText(mentor.getMentorName());
        mentorEmail.setText(mentor.getEmail());
        mentorPhone.setText(mentor.getPhoneNumber());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        status = (String)parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
