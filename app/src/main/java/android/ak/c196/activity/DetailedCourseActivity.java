package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.CourseDao;
import android.ak.c196.database.dao.CourseMentorJoinDao;
import android.ak.c196.database.dao.MentorDao;
import android.ak.c196.entity.Course;
import android.ak.c196.entity.Mentor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


public class DetailedCourseActivity extends AppCompatActivity {


    private Course selectedCourse;
    private Mentor mentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedCourse = (Course)getIntent().getSerializableExtra("selectedCourse");

        getMentorInformation();
        setTitle(selectedCourse.getCourseName());
        displayCourseDetails();
        displayMentorDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.clear();

        getMenuInflater().inflate(R.menu.course_specific_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent;
        Bundle extras;

        switch (itemId) {
            case  R.id.note_icon:

                intent = new Intent(getApplicationContext(), NoteActivity.class);

                 extras = new Bundle();

                extras.putInt("courseID", selectedCourse.getCourseID());

                intent.putExtras(extras);
                startActivity(intent);
                
                return true;
            case R.id.assessment_icon:

                 intent = new Intent(getApplicationContext(), AssessmentActivity.class);

                extras = new Bundle();

                extras.putInt("courseID", selectedCourse.getCourseID());

                intent.putExtras(extras);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void displayCourseDetails(){

        TextView start = findViewById(R.id.start);
        TextView end = findViewById(R.id.end);
        TextView status = findViewById(R.id.status);

        start.setText(selectedCourse.getStartDate());
        end.setText(selectedCourse.getEndDate());
        status.setText(selectedCourse.getStatus());
    }

    private void getMentorInformation(){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        MentorDao mentorDao = database.getMentorDao();
        mentor = mentorDao.getMentor(selectedCourse.getCourseID());

    }

    private void displayMentorDetails(){

        TextView mentorName = findViewById(R.id.mentorName);
        TextView mentorEmail =findViewById(R.id.mentorEmail);
        TextView mentorPhone =findViewById(R.id.mentorPhone);


        mentorName.setText(mentor.getMentorName());
        mentorEmail.setText(mentor.getEmail());
        mentorPhone.setText(mentor.getPhoneNumber());

    }
}
