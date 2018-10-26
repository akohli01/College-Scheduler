package android.ak.c196.utility;


import android.ak.c196.R;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class ReusableDatePicker extends DialogFragment {

    private int id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

         int year,month,day;

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Bundle b = getArguments();
        this.id = b.getInt("key");


        if(b.getString("startDate") != null){

            String startDate = b.getString("startDate");

            String[] parts = startDate.split("/");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];

            month = Integer.parseInt(part1) -1;
            day = Integer.parseInt(part2);
            year = Integer.parseInt(part3);

        }

        if( b.getString("endDate") != null){

            String endDate = b.getString("endDate");

            String[] parts = endDate.split("/");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];

            month = Integer.parseInt(part1) -1;
            day = Integer.parseInt(part2);
            year = Integer.parseInt(part3);

        }


        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {

                    month = month +1;
                    Tester tester = new Tester();
                    tester.onPostExecute(month + "/" + day + "/" + year);

                }
            };

    private class Tester extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            EditText text = getActivity().findViewById(id);
            text.setText(s);
        }

    }
}