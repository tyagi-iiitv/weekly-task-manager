package com.tyagi.anjul.weeklytaskmanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DeleteTask extends AppCompatActivity {
    private Intent intent;
    private ArrayList<Task> tasks;
    private ListView listView;
    private ArrayList<Task> to_delete_tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_task);
        intent = getIntent();
        TextView textView = (TextView) findViewById(R.id.textView9);
        Button button = (Button) findViewById(R.id.button8);
        if(Boolean.parseBoolean(intent.getStringExtra("for_delete")) == false) {
            textView.setText("Select the completed task/s");
            button.setText("Done");
        }
        else{
            textView.setText("Select the task/s to delete");
            button.setText("Delete");
        }
        tasks = (ArrayList<Task>)intent.getSerializableExtra("tasks");
        to_delete_tasks = new ArrayList<Task>();
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MyDeleteAdapter(this, tasks));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView ctv = (CheckedTextView)view.findViewById(R.id.checkedTextView);
                ctv.toggle();
                Task temp = tasks.get(position);
                tasks.remove(position);
                if(ctv.isChecked() == true){
                    temp.set_delete_status(true);
                    tasks.add(position, temp);
                }
                else{
                    temp.set_delete_status(false);
                    tasks.add(position, temp);
                }
            }
        });
    }

    public void delete(View v){
        intent.putExtra("tasks", tasks);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_task, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
