package com.tyagi.anjul.weeklytaskmanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.Date;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Task_activity extends AppCompatActivity {
    private Intent intent;
    private ArrayList<Task> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_activity);
        TextView mName = (TextView) findViewById(R.id.textView10);
        TextView mRemaining = (TextView) findViewById(R.id.textView12);
        TextView mTotal = (TextView) findViewById(R.id.textView14);
        intent = getIntent();
        Integer position = intent.getIntExtra("task_pos", 0);
        tasks = (ArrayList<Task>)intent.getSerializableExtra("tasks");
        Task task = tasks.get(position);
        mName.setText(task.getName());
        mRemaining.setText(task.get_remaining_hours()+":"+task.get_remaining_mins());
        mTotal.setText(task.getHours()+":"+task.getMins());
    }

    public void task_on(View v){
        setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_activity, menu);
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
