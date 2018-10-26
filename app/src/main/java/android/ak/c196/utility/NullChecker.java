package android.ak.c196.utility;

import android.ak.c196.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NullChecker{

    public int checkForNull(View view){
        List<View> chidren = getAllChildrenBFS(view);

        for(View child: chidren){
            if( child instanceof EditText){

                EditText temp = (EditText) child;

                if( temp.getText().toString().matches("")){
                    return -1;
                }
            }
        }

        return 1;
    }

    private List<View> getAllChildrenBFS(View v) {
        List<View> visited = new ArrayList();
        List<View> unvisited = new ArrayList();
        unvisited.add(v);

        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            visited.add(child);
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
        }

        return visited;
    }




}
