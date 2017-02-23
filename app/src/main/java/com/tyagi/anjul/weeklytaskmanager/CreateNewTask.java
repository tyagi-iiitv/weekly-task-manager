package com.tyagi.anjul.weeklytaskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class CreateNewTask extends AppCompatActivity {
    private Intent intent;
    static final String TASK_NAME = "Task name";
    static final String TASK_HOURS = "Task hours";
    static final String TASK_MINS = "Task mins";
    static final String NEW_ACTIVITY = "New activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);
        intent = getIntent();
    }

    public void add_task_to_list(View v){
        EditText mTaskName = (EditText) findViewById(R.id.editText);
        EditText mHours = (EditText) findViewById(R.id.editText2);
        EditText mMins = (EditText) findViewById(R.id.editText3);
        String taskName = mTaskName.getText().toString();
        String taskHours = mHours.getText().toString();
        String taskMins = mMins.getText().toString();
        String newActivity = "false";
        if(!taskName.equals("") && !taskHours.equals("") && !taskMins.equals("")) {
            intent.putExtra(TASK_NAME, taskName);
            intent.putExtra(TASK_HOURS, taskHours);
            intent.putExtra(TASK_MINS, taskMins);
            intent.putExtra(NEW_ACTIVITY, newActivity);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_task, menu);
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
