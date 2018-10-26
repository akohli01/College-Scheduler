package android.ak.c196.activity;

import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.AssessmentDao;
import android.ak.c196.entity.Assessment;
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
import android.ak.c196.R;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class AssessmentActivity extends AppCompatActivity {

    private List<Assessment> assessments;
    private ListView listView;
    private int courseID;

    private Assessment selectedAssessment;

    private boolean isListViewItemSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.assessment_main);
        setTitle("Assessments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.assessmentList);

        courseID = Objects.requireNonNull(getIntent().getExtras()).getInt("courseID");

        onRefreshHandler();

    }
    private void onRefreshHandler() {

        isListViewItemSelected = false;
        supportInvalidateOptionsMenu();

        final FloatingActionButton fab = findViewById(R.id.fab);
        enableAddButton(fab);

        initializeListeners();
        getAssessments();
        updateCustomViewAdapter();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        if(isListViewItemSelected) {
            getMenuInflater().inflate(R.menu.update_delete_cancel_info_menu,menu);

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
                Intent i = new Intent(AssessmentActivity.this, EditAssessmentActivity.class);

                i.putExtra("selectedAssessment", selectedAssessment);

                startActivity(i);
                return true;
            case R.id.info_icon:
                i = new Intent(AssessmentActivity.this, DetailedAssessmentActivity.class);

                i.putExtra("selectedAssessment", selectedAssessment);

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

    private void showConfirmation(){

        AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentActivity.this);

        builder
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAssessment();
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

    private void disableAddButton(FloatingActionButton fab){
        fab.setEnabled(false);
        fab.getBackground().setAlpha(0);
    }

    private void enableAddButton(FloatingActionButton fab){
        fab.setEnabled(true);
        fab.getBackground().setAlpha(255);
    }

    private void initializeSelectedAssessment(View arg1){
        selectedAssessment = new Assessment();

        selectedAssessment.setTitle(((TextView) arg1.findViewById(R.id.textView_assessmentTitle)).getText().toString());
        selectedAssessment.setGoalDate(((TextView) arg1.findViewById(R.id.textView_goalDate)).getText().toString());
        selectedAssessment.setDueDate(((TextView) arg1.findViewById(R.id.textView_dueDate)).getText().toString());
        selectedAssessment.setType(((TextView) arg1.findViewById(R.id.textView_type)).getText().toString());
        selectedAssessment.setCourseID(courseID);

        String assessmentID = ((TextView) arg1.findViewById(R.id.textView_ID)).getText().toString();

        selectedAssessment.setAssessmentID(Integer.parseInt(assessmentID));

        isListViewItemSelected = true;
        supportInvalidateOptionsMenu();
    }

    private void deleteAssessment(){

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        AssessmentDao assessmentDao = database.getAssessmentDao();

        assessmentDao.delete(selectedAssessment);

        onRefreshHandler();
    }

    private void getAssessments(){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        AssessmentDao assessmentDAO = database.getAssessmentDao();
        this.assessments = assessmentDAO.getAssessments(courseID);
    }

    private void updateCustomViewAdapter(){

        CustomAdapter customAdapter = new CustomAdapter(){

            @Override
            public int getCount() {
                return assessments.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.individual_assessment,null);

                TextView assessmentTitle = convertView.findViewById(R.id.textView_assessmentTitle);
                TextView goalDate = convertView.findViewById(R.id.textView_goalDate);
                TextView dueDate = convertView.findViewById(R.id.textView_dueDate);
                TextView id = convertView.findViewById(R.id.textView_ID);
                TextView type = convertView.findViewById(R.id.textView_type);

                assessmentTitle.setText(assessments.get(position).getTitle());
                goalDate.setText(assessments.get(position).getGoalDate());
                dueDate.setText(assessments.get(position).getDueDate());
                type.setText(assessments.get(position).getType());
                id.setText(Integer.toString(assessments.get(position).getAssessmentID()));
                id.setVisibility(View.INVISIBLE);

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
                Intent i = new Intent(AssessmentActivity.this, AddAssessmentActivity.class);

                Bundle extras = new Bundle();

                extras.putInt("courseID", courseID);

                i.putExtras(extras);

                startActivity(i);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                disableAddButton(fab);
                initializeSelectedAssessment(arg1);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                        initializeSelectedAssessment(view);

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
