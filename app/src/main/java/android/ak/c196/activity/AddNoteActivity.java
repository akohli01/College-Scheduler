package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.NoteDao;
import android.ak.c196.entity.Note;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class AddNoteActivity extends AppCompatActivity {

    private int courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit_note);
        setTitle("Add Note");

        courseID = getIntent().getExtras().getInt("courseID");

        initializeListeners();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void saveNote(String fileName){
        try {

            File folder = getFilesDir();
            File file= new File(folder, "course" + courseID + "/" + fileName);

            OutputStreamWriter out =
                    new OutputStreamWriter(new FileOutputStream(file));
            EditText content =findViewById(R.id.editText_content);
            out.write(content.getText().toString());
            out.close();
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }

    }

   private void createDirectory(){

       File folder = getFilesDir();
       File directory= new File(folder, "course" + courseID);

       if (! directory.exists()){
           directory.mkdir();
       }

   }

   private void insertIntoDatabase(){

       AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

       EditText fileName =findViewById(R.id.editText_fileName);

       Note note = new Note();
       note.setLocation("course" + courseID + "/" + fileName.getText().toString() + ".txt");
       note.setCourseID(courseID);

       NoteDao noteDAO = database.getNoteDao();
       noteDAO.insert(note);

       finish();

   }

   private void initializeListeners(){
       final FloatingActionButton fab = findViewById(R.id.save);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               EditText fileName =findViewById(R.id.editText_fileName);
               if(fileName.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(), "Please enter a note name", Toast.LENGTH_SHORT).show();
               }
               else{
                   createDirectory();

                   File folder = getFilesDir();
                   File file= new File(folder, "course" + courseID + "/" + fileName.getText().toString().trim() + ".txt");


                   if(file.exists() && !file.isDirectory()) {
                       Toast.makeText(getApplicationContext(),"Error, this note name is already in use. Use a different note name", Toast.LENGTH_SHORT).show();
                   }
                   else{
                       saveNote(fileName.getText().toString().trim() + ".txt");
                       insertIntoDatabase();
                   }

               }
           }
       });
   }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
