package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.entity.Term;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailedTermActivity extends AppCompatActivity {

    private Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_term);
        setTitle("Term Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        term = (Term) getIntent().getSerializableExtra("selectedTerm");

        TextView termName = findViewById(R.id.termName);
        TextView startDate = findViewById(R.id.startDate);
        TextView endDate = findViewById(R.id.endDate);

        termName.setText(term.getTermName());
        startDate.setText(term.getStartDate());
        endDate.setText(term.getEndDate());
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
