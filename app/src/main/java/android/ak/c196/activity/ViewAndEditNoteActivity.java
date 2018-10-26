package android.ak.c196.activity;

import android.ak.c196.R;
import android.ak.c196.database.AppDatabase;
import android.ak.c196.database.dao.NoteDao;
import android.ak.c196.entity.Note;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class ViewAndEditNoteActivity extends AppCompatActivity {

    private Note selectedNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedNote = (Note)getIntent().getSerializableExtra("selectedNote");

        setTitle("Edit");

        initializeScreen();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.clear();

        getMenuInflater().inflate(R.menu.update_delete_share_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.share_icon:
                share();
                return true;
            case R.id.delete_icon:
                showConfirmation();
                return true;
            case R.id.edit_icon:
                enableEditing();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void share(){
        EditText content =findViewById(R.id.editText_content);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    private void showConfirmation(){

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAndEditNoteActivity.this);

        builder
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteNote();
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

    private void saveNote(String fileName){
        try {

            File folder = getFilesDir();
            File file= new File(folder, "course" + selectedNote.getCourseID() + "/" + fileName);

            OutputStreamWriter out =
                    new OutputStreamWriter(new FileOutputStream(file));
            EditText content =findViewById(R.id.editText_content);
            out.write(content.getText().toString());
            out.close();
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }

        finish();

    }

    private void deleteNote(){

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());

        NoteDao noteDao = database.getNoteDao();

        noteDao.delete(selectedNote);

        File folder = getFilesDir();
        File file= new File(folder, "course"+ selectedNote.getCourseID() +"/"+selectedNote.getLocation()+ ".txt");
        file.delete();

        finish();

    }

    private void enableEditing(){
        EditText notepad =findViewById(R.id.editText_content);

        notepad.setEnabled(true);
        notepad.requestFocus();

    }

    private void initializeScreen(){
        String content = getNoteContents();

        EditText notepad =findViewById(R.id.editText_content);
       // notepad.setFocusable(false);
        notepad.setEnabled(false);

        notepad.setText(content);

        EditText noteName =findViewById(R.id.editText_fileName);
        noteName.setEnabled(false);

        noteName.setText(selectedNote.getLocation());

    }

    private String getNoteContents(){

        String content = "";
        File folder = getFilesDir();
        File file= new File(folder, "course"+ selectedNote.getCourseID() +"/"+selectedNote.getLocation()+ ".txt");

        try {
               BufferedReader reader = new BufferedReader(new FileReader(file));
                String str;
                StringBuilder buf = new StringBuilder();
                while ((str = reader.readLine()) != null) {
                    buf.append(str + "\n");
                } reader .close();
                content = buf.toString();
        } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }

        return content;
    }

    private void initializeListeners(){
        final FloatingActionButton fab = findViewById(R.id.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText fileName =findViewById(R.id.editText_fileName);

                File folder = getFilesDir();
                File file= new File(folder, "course" + selectedNote.getCourseID() + "/" + fileName.getText().toString() + ".txt");

                saveNote(fileName.getText().toString() + ".txt");
            }
        });

    }
}
