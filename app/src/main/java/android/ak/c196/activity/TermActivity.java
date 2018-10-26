package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.CourseDao;
import android.ak.c196.entity.Course;
import android.ak.c196.entity.Term;
import android.ak.c196.database.dao.TermDao;
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
import android.widget.Toast;

import android.ak.c196.utility.CustomAdapter;


import java.util.List;


public class TermActivity extends AppCompatActivity {

    private List<Term> terms;
    private ListView listView;
    private Term selectedTerm;
    private boolean isListViewItemSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.term_main);
        setTitle("Terms");

        listView = findViewById(R.id.termsList);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onRefreshHandler();

    }

    private void onRefreshHandler() {

        isListViewItemSelected = false;
        supportInvalidateOptionsMenu();

        final FloatingActionButton fab = findViewById(R.id.fab);
        enableAddButton(fab);

        initializeListeners();
        getTerms();
        setCustomViewAdapter();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (isListViewItemSelected) {
            getMenuInflater().inflate(R.menu.update_delete_cancel_info_menu, menu);
        } else {

            menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.close_icon:
                onRefreshHandler();
                return true;
            case R.id.delete_icon:
                if (!hasDependentCourses()) {
                    showConfirmation();
                    onRefreshHandler();
                }
                return true;
            case R.id.edit_icon:
                Intent i = new Intent(TermActivity.this, EditTermActivity.class);

                i.putExtra("selectedTerm",selectedTerm);

                startActivity(i);

                return true;
            case R.id.info_icon:
                i = new Intent(TermActivity.this, DetailedTermActivity.class);

                i.putExtra("selectedTerm", selectedTerm);

                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    private void getTerms() {
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        TermDao termDAO = database.getTermDao();
        this.terms = termDAO.getTerms();
    }

    private void setCustomViewAdapter() {
        CustomAdapter customAdapter = new CustomAdapter() {
            @Override
            public int getCount() {
                return terms.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.individual_term, null);

                TextView termName = convertView.findViewById(R.id.textView_termName);
                TextView startDate = convertView.findViewById(R.id.textView_startDate);
                TextView endDate = convertView.findViewById(R.id.textView_endDate);
                TextView id = convertView.findViewById(R.id.textView_ID);

                termName.setText(terms.get(position).getTermName());
                startDate.setText(terms.get(position).getStartDate());
                endDate.setText(terms.get(position).getEndDate());
                id.setText(Integer.toString(terms.get(position).getTermID()));
                id.setVisibility(View.INVISIBLE);

                return convertView;
            }
        };
        listView.setAdapter(customAdapter);
    }

    private void disableAddButton(FloatingActionButton fab){
        fab.setEnabled(false);
        fab.getBackground().setAlpha(0);
    }

    private void enableAddButton(FloatingActionButton fab){
        fab.setEnabled(true);
       fab.getBackground().setAlpha(255);
    }

    private void initializeSelectedTerm(View arg1){
        selectedTerm = new Term();

        selectedTerm.setTermName(((TextView) arg1.findViewById(R.id.textView_termName)).getText().toString());
        selectedTerm.setStartDate(((TextView) arg1.findViewById(R.id.textView_startDate)).getText().toString());
        selectedTerm.setEndDate(((TextView) arg1.findViewById(R.id.textView_endDate)).getText().toString());

        String termID = ((TextView) arg1.findViewById(R.id.textView_ID)).getText().toString();

        selectedTerm.setTermID(Integer.parseInt(termID));

        isListViewItemSelected = true;
        supportInvalidateOptionsMenu();
    }

    private boolean hasDependentCourses() {

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        CourseDao courseDao = database.getCourseDao();

        List<Course> tempList = courseDao.getCourses(selectedTerm.getTermID());

        if (tempList.isEmpty()) {
            return false;
        }

        Toast.makeText(this, "Error: Please delete all courses from this term in order to perform this operation.", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void showConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(TermActivity.this);

        builder
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTerm();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void deleteTerm() {

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        TermDao termDAO = database.getTermDao();
        termDAO.delete(selectedTerm);

        onRefreshHandler();
    }

    private void initializeListeners(){

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TermActivity.this, AddTermActivity.class);
                startActivity(i);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                if (!isListViewItemSelected) {
                    Intent intent = new Intent(getApplicationContext(), CourseActivity.class);

                    String termID = ((TextView) view.findViewById(R.id.textView_ID)).getText().toString();

                    Bundle extras = new Bundle();

                    extras.putInt("termID", Integer.parseInt(termID));

                    intent.putExtras(extras);

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
                initializeSelectedTerm(arg1);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                        initializeSelectedTerm(view);
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
