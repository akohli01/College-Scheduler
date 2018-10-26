package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.CourseDao;
import android.ak.c196.entity.Course;
import android.ak.c196.utility.CustomAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;


public class CourseActivity extends AppCompatActivity {

    private List<Course> courses;
    private ListView listView;
    private int termID;
    private Course selectedCourse;
    private boolean isListViewItemSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRefreshHandler();
    }

    private void onRefreshHandler() {

        setContentView(R.layout.course_main);
        setTitle("Courses");

        listView = findViewById(R.id.courseList);

        termID = Objects.requireNonNull(getIntent().getExtras()).getInt("termID");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isListViewItemSelected = false;
        supportInvalidateOptionsMenu();

        getCourses();
        setCustomViewAdapter();

        initializeListeners();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        if(isListViewItemSelected)
        {
            getMenuInflater().inflate(R.menu.update_delete_cancel_menu,menu);
        }else{
           menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case  R.id.close_icon:
                onRefreshHandler();
                return true;
            case R.id.delete_icon:
                showConfirmation();
                onRefreshHandler();
                return true;
            case R.id.edit_icon:
                Intent i = new Intent(CourseActivity.this, EditCourseActivity.class);

                i.putExtra("selectedCourse",selectedCourse);

                startActivity(i);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void disableAddButton(FloatingActionButton fab){
        fab.setEnabled(false);
        fab.getBackground().setAlpha(0);
    }

    private void initializeSelectedCourse(View arg1){
        selectedCourse = new Course();

        selectedCourse.setCourseName(((TextView) arg1.findViewById(R.id.textView_assesmentName)).getText().toString());
        selectedCourse.setStartDate(((TextView) arg1.findViewById(R.id.textView_startDate)).getText().toString());
        selectedCourse.setEndDate(((TextView) arg1.findViewById(R.id.textView_endDate)).getText().toString());
        selectedCourse.setStatus(((TextView) arg1.findViewById(R.id.textView_status)).getText().toString());
        selectedCourse.setTermID(termID);

        String courseID = ((TextView) arg1.findViewById(R.id.textView_ID)).getText().toString();

        selectedCourse.setCourseID(Integer.parseInt(courseID));

    }

    private void showConfirmation(){

        AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);

        builder
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteCourse();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void deleteCourse(){

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        CourseDao courseDAO = database.getCourseDao();

        courseDAO.delete(selectedCourse);

        onRefreshHandler();
    }

    private void getCourses(){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        CourseDao courseDAO = database.getCourseDao();
        this.courses = courseDAO.getCourses(termID);
    }

    private void setCustomViewAdapter(){

        CustomAdapter customAdapter = new CustomAdapter(){

            @Override
            public int getCount() {
                return courses.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.individual_course,null);

                TextView courseName = convertView.findViewById(R.id.textView_assesmentName);
                TextView startDate = convertView.findViewById(R.id.textView_startDate);
                TextView endDate = convertView.findViewById(R.id.textView_endDate);
                TextView id = convertView.findViewById(R.id.textView_ID);
                TextView status = convertView.findViewById(R.id.textView_status);

                courseName.setText(courses.get(position).getCourseName());
                startDate.setText(courses.get(position).getStartDate());
                endDate.setText(courses.get(position).getEndDate());
                id.setText(Integer.toString(courses.get(position).getCourseID()));
                status.setText(courses.get(position).getStatus());
                id.setVisibility(View.INVISIBLE);
                status.setVisibility(View.INVISIBLE);

                return convertView;
            }

        };

        listView.setAdapter(customAdapter);
    }

    private void initializeListeners(){
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CourseActivity.this, AddCourseActivity.class);

                Bundle extras = new Bundle();

                extras.putInt("termID", termID);

                i.putExtras(extras);

                startActivity(i);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                if (!isListViewItemSelected) {
                    Intent intent = new Intent(getApplicationContext(), DetailedCourseActivity.class);

                    initializeSelectedCourse(view);

                    intent.putExtra("selectedCourse", selectedCourse);
                    startActivity(intent);

                }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                disableAddButton(fab);
                initializeSelectedCourse(arg1);

                isListViewItemSelected = true;
                supportInvalidateOptionsMenu();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                        initializeSelectedCourse(view);

                    }
                });

                return true;
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        onRefreshHandler();
    }

}
