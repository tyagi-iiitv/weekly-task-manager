package com.tyagi.anjul.weeklytaskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Boolean task_on = false;
    private Integer task_pos;
    private ListView listView;
    private static final int CREATE = 1;
    private static final int DELETE = 2;
    private static final int DONE = 3;
    private static final int TASK = 4;
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private static final String filename = "my_tasks.txt";
    private boolean new_activity = true;
    private File file;
    private CountDownTimer countDownTimer;
    private boolean time_has_started = false;
    private TextView time_counter;
    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWritable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("SOP : OnCreate");
        String state = Environment.getExternalStorageState();
        file = open_file(state);
        initialise_tasks(file);
        set_task_on();
        if(task_on == true) {
            System.out.println("RESULT B : " + tasks.get(task_pos).get_last_updated());
            setContentView(R.layout.task_on);
            Long start_time_to_send = get_time_to_send();
            time_counter = (TextView) findViewById(R.id.textView4);
            TextView mName = (TextView) findViewById(R.id.textView2);
            countDownTimer = new MyCountDownTimer(start_time_to_send, 1000);
            time_has_started = true;
            mName.setText(tasks.get(task_pos).getName());
            time_counter.setText(tasks.get(task_pos).get_remaining_hours().toString().concat(":").concat(tasks.get(task_pos).get_remaining_mins().toString()));
            countDownTimer.start();

        }
        else {
            populate_view();
        }

    }

    public File open_file(String state){
        if(Environment.MEDIA_MOUNTED.equals(state)){
            mExternalStorageAvailable = mExternalStorageWritable = true;
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+filename);
            return file;
        }
        else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            mExternalStorageAvailable = true;
            mExternalStorageWritable = false;
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+filename);
            return file;
        }
        else{
            mExternalStorageAvailable = mExternalStorageWritable = false;
            return null;
        }
    }

    public void initialise_tasks(File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tasks = (ArrayList<Task>) ois.readObject();
            ois.close();
            for(int i=0; i<tasks.size();i++){
                System.out.println("SOP"+tasks.get(i).getName());
                System.out.println("SOP"+tasks.get(i).get_running_status());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Long get_time_to_send(){
        Long time_temp = System.currentTimeMillis();
        Long time_temp_2 = tasks.get(task_pos).get_last_updated();
        Long diff = time_temp - time_temp_2;
        Long min = (Long) (diff / (1000 * 60) % 60);
        Long hours = (Long) (diff / (1000 * 60 * 60));
        Long new_hours = tasks.get(task_pos).get_remaining_hours() - hours;
        Long new_mins = tasks.get(task_pos).get_remaining_mins() - min;
        Long start_time_to_send = new_hours*60*60*1000 + new_mins*60*1000;
        return start_time_to_send;
    }

    public void populate_view(){
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new MyAdapter(this, tasks));
        final Intent task_intent = new Intent(this, Task_activity.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.task);
                task_pos = position;
                task_intent.putExtra("task_pos", position);
                task_intent.putExtra("tasks", tasks);
                startActivityForResult(task_intent, TASK);
            }
        });
    }

    private void set_task_on(){
        for(int i=0;i<tasks.size();i++){
            if(tasks.get(i).get_running_status() == true){
                task_on = true;
                task_pos = i;
                break;
            }
        }
    }

    public void pause(View v){
        if(time_has_started){
            countDownTimer.cancel();
            String[] text = time_counter.getText().toString().split(":");
            tasks.get(task_pos).set_remaining_hours(Integer.parseInt(text[0]));
            tasks.get(task_pos).set_remaining_mins(Integer.parseInt(text[1]));
            time_has_started = false;
        }
        else{
            long start_time_to_send = (long)(tasks.get(task_pos).get_remaining_hours()*60*60*1000 + tasks.get(task_pos).get_remaining_mins()*60*1000);
            countDownTimer = new MyCountDownTimer(start_time_to_send, 1000);
            countDownTimer.start();
            time_has_started = true;
        }
    }

    public void stop(View v){
        countDownTimer.cancel();
        String[] text = time_counter.getText().toString().split(":");
        tasks.get(task_pos).set_remaining_hours(Integer.parseInt(text[0]));
        tasks.get(task_pos).set_remaining_mins(Integer.parseInt(text[1]));
        time_has_started = false;
        task_on = false;
        tasks.get(task_pos).set_running_status(false);
        new_activity = false;
        populate_view();
    }
    public void create_new_task(View v){
        Intent intent = new Intent(this, CreateNewTask.class);
        startActivityForResult(intent, CREATE);
    }

    public void delete_task(View v){
        Intent intent = new Intent(this, DeleteTask.class);
        intent.putExtra("tasks", tasks);
        intent.putExtra("for_delete", "true");
        startActivityForResult(intent, DELETE);
    }

    public void mark_as_done(View v){
        Intent intent = new Intent(this, DeleteTask.class);
        intent.putExtra("tasks", tasks);
        intent.putExtra("for_delete", "false");
        startActivityForResult(intent, DONE);
    }

    public void done(View v){
        countDownTimer.cancel();
        tasks.get(task_pos).set_remaining_mins(0);
        tasks.get(task_pos).set_remaining_hours(0);
        task_on = false;
        tasks.get(task_pos).set_running_status(false);
        populate_view();

    }

    public void reset(View v){
        for (int i=0;i<tasks.size();i++){
            tasks.get(i).set_remaining_hours(tasks.get(i).getHours());
            tasks.get(i).set_remaining_mins(tasks.get(i).getMins());
        }
        listView.setAdapter(new MyAdapter(this, tasks));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CREATE){
            if(resultCode == RESULT_OK){
                String taskName = data.getStringExtra("Task name");
                String taskHours = data.getStringExtra("Task hours");
                String taskMins = data.getStringExtra("Task mins");
                new_activity = Boolean.parseBoolean(data.getStringExtra("New activity"));
                tasks.add(new Task(taskName, Integer.parseInt(taskHours), Integer.parseInt(taskMins)));
                listView.setAdapter(new MyAdapter(this, tasks));
            }
        }
        else if(requestCode == DELETE){
            if(resultCode == RESULT_OK){
                tasks = (ArrayList<Task>)data.getSerializableExtra("tasks");
                ArrayList<Task> temp = new ArrayList<Task>();
                for(int i=0; i<tasks.size();i++){
                    if(tasks.get(i).get_delete_status() == false){
                        temp.add(tasks.get(i));
                    }
                }
                tasks = temp;
                listView.setAdapter(new MyAdapter(this, tasks));
            }
        }
        else if(requestCode == DONE){
            if(resultCode == RESULT_OK){
                tasks = (ArrayList<Task>)data.getSerializableExtra("tasks");
                for(int i=0;i<tasks.size();i++){
                    if(tasks.get(i).get_delete_status() == true){
                        tasks.get(i).set_remaining_hours(0);
                        tasks.get(i).set_remaining_mins(0);
                        tasks.get(i).set_delete_status(false);
                    }
                }
            }
            listView.setAdapter(new MyAdapter(this,tasks));
        }
        else if(requestCode == TASK){
            if(resultCode == RESULT_OK){
//                Long time_temp = System.currentTimeMillis();
//                Long time_temp_2 = tasks.get(task_pos).get_last_updated();
//                Long diff = time_temp - time_temp_2;
//                Long min = (Long) (diff / (1000 * 60) % 60);long start_time_to_send = (long)(tasks.get(task_pos).get_remaining_hours()*60*60*1000 + tasks.get(task_pos).get_remaining_mins()*60*1000);
//                Long hours = (Long) (diff / (1000 * 60 * 60));
//                Long start_time_to_send = hours*60*60*1000 + min*60*1000;
                long start_time_to_send = (long)(tasks.get(task_pos).get_remaining_hours()*60*60*1000 + tasks.get(task_pos).get_remaining_mins()*60*1000);
                setContentView(R.layout.task_on);
                System.out.println("RESULT : start time"+start_time_to_send);
                TextView mName = (TextView) findViewById(R.id.textView2);
                time_counter = (TextView) findViewById(R.id.textView4);
                countDownTimer = new MyCountDownTimer(start_time_to_send, 1000);
                time_has_started = true;
                mName.setText(tasks.get(task_pos).getName());
                time_counter.setText(tasks.get(task_pos).get_remaining_hours().toString().concat(":").concat(tasks.get(task_pos).get_remaining_mins().toString()));
//                time_counter.setText(tasks.get(task_pos).get_remaining_hours() + ":" + tasks.get(task_pos).get_remaining_mins());
                countDownTimer.start();
                tasks.get(task_pos).set_running_status(true);
                task_on = true;
                System.out.println("RESULT B : "+tasks.get(task_pos).get_last_updated());
                new_activity = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onResume(){
        super.onResume();
        if(time_has_started == false && task_on == true){
            long time_to_send = get_time_to_send();
            countDownTimer = new MyCountDownTimer(time_to_send, 1000);
            time_counter = (TextView) findViewById(R.id.textView4);
            TextView mName = (TextView) findViewById(R.id.textView2);
            time_has_started = true;
            mName.setText(tasks.get(task_pos).getName());
            time_counter.setText(tasks.get(task_pos).get_remaining_hours().toString().concat(":").concat(tasks.get(task_pos).get_remaining_mins().toString()));
            countDownTimer.start();

        }
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("SOP : onPause "+new_activity);
        if(new_activity == false) {
            if(task_on == true){
                countDownTimer.cancel();
                time_has_started = false;
                tasks.get(task_pos).set_last_updated(System.currentTimeMillis());
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(tasks);
                oos.flush();
                oos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(time_has_started == true){
            countDownTimer.cancel();
            time_has_started = false;
            tasks.get(task_pos).set_last_updated(System.currentTimeMillis());
        }
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            time_counter.setText("Time's Up!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Long sec = (Long) (millisUntilFinished / 1000) % 60;
            Long min = (Long) (millisUntilFinished / (1000 * 60) % 60);
            Long hours = (Long) (millisUntilFinished / (1000 * 60 * 60));
            time_counter.setText(hours.toString() + ":" + min.toString() + ":" + sec.toString());
        }
    }
}
