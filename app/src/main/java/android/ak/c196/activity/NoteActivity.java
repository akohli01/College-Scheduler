package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.NoteDao;
import android.ak.c196.entity.Note;
import android.ak.c196.utility.CustomAdapter;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    private List<Note> notes;
    private ListView listView;
    private int courseID;

    private Note selectedNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRefreshHandler();
    }

    private void onRefreshHandler() {

        setContentView(R.layout.note_main);
        setTitle("Notes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseID = Objects.requireNonNull(getIntent().getExtras()).getInt("courseID");

        supportInvalidateOptionsMenu();

        getNotes();
        updateListView();

        initializeListeners();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void getNotes(){
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        NoteDao noteDAO = database.getNoteDao();
        this.notes = noteDAO.getNotes(courseID);
    }

    private void updateListView(){

        listView = findViewById(R.id.noteList);

        CustomAdapter customAdapter = new CustomAdapter(){

            @Override
            public int getCount() {
                return notes.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.individual_note,null);

                TextView noteName = convertView.findViewById(R.id.textView_noteName);
                TextView id = convertView.findViewById(R.id.textView_ID);

                String temp = notes.get(position).getLocation();
                temp = temp.substring(temp.lastIndexOf("/") + 1, temp.lastIndexOf("."));

                noteName.setText(temp);
                id.setText(Integer.toString(notes.get(position).getNoteID()));
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
                Intent i = new Intent(NoteActivity.this, AddNoteActivity.class);

                Bundle extras = new Bundle();

                extras.putInt("courseID", courseID);

                i.putExtras(extras);

                startActivity(i);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                Intent intent = new Intent(getApplicationContext(), ViewAndEditNoteActivity.class);

                String noteID = ((TextView) view.findViewById(R.id.textView_ID)).getText().toString();
                String noteName = ((TextView) view.findViewById(R.id.textView_noteName)).getText().toString();

                selectedNote = new Note();
                selectedNote.setNoteID(Integer.parseInt(noteID));
                selectedNote.setLocation(noteName);
                selectedNote.setCourseID(courseID);

                intent.putExtra("selectedNote", selectedNote);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        onRefreshHandler();
    }

}

