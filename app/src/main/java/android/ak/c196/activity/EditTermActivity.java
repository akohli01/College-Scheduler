package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.entity.Term;
import android.ak.c196.database.dao.TermDao;
import android.ak.c196.utility.NullChecker;
import android.ak.c196.utility.ReusableDatePicker;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditTermActivity extends AppCompatActivity {

    private Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_or_edit_term);
        setTitle("Edit Term");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeInputs();
        initializeListeners();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void updateTerm(){

        if(checkForNull() == -1)
            return;

        EditText termName = findViewById(R.id.termName);
        EditText startDate = findViewById(R.id.startDate);
        EditText endDate = findViewById(R.id.endDate);

        term.setTermName(termName.getText().toString());
        term.setStartDate(startDate.getText().toString());
        term.setEndDate(endDate.getText().toString());

        insertIntoDatabase(term);

    }

    private void insertIntoDatabase(Term term){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        TermDao termDAO = database.getTermDao();
        termDAO.update(term);
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

    private void initializeListeners(){

        Button updateTerm = findViewById(R.id.add);
        updateTerm.setText("Update");
        updateTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTerm();
            }
        });

        final EditText startDate = findViewById(R.id.startDate);
        final EditText endDate = findViewById(R.id.endDate);

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
    }

    private void initializeInputs(){
        EditText termName = findViewById(R.id.termName);
        final EditText startDate = findViewById(R.id.startDate);
        final EditText endDate = findViewById(R.id.endDate);

        term = (Term) getIntent().getSerializableExtra("selectedTerm");

        termName.setText(term.getTermName());
        startDate.setText(term.getStartDate());
        endDate.setText(term.getEndDate());

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
}
