package com.project.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ImportCourseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SEMESTER_NUM = 8;
    private static final String TAG = "TimeTable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_course);
        initMember();
        bindListener();
        getSupportActionBar().hide();
    }

    EditText userName;
    EditText passWord;
    EditText verifyCode;
    Button button_confirm;
    Button button_back;
    TextView tv_info;
    Spinner spinner_schoolSemester;
    int schoolSemester;
    EditText schoolYear;
    private void initMember(){
        tv_info = (TextView)findViewById(R.id.textView_import_info);
        userName = (EditText)findViewById(R.id.editText_stu_no);
        passWord = (EditText)findViewById(R.id.editText_stu_passwd);
        verifyCode = (EditText)findViewById(R.id.editText_Verification_code);

        spinner_schoolSemester = (Spinner) findViewById(R.id.spinner_semester);
        schoolYear = (EditText) findViewById(R.id.editText_schoolYear);
        initSpinner();
        button_confirm  = (Button)findViewById(R.id.button_confrim_import);
        button_back =(Button)findViewById(R.id.button_importCourse_back);

        verifyCode.setVisibility(View.INVISIBLE);
    }

    private void initSpinner(){
        List<String> semester= new ArrayList<String>();
        for (int i = 1 ; i <= SEMESTER_NUM ; i++){
            semester.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,semester);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_schoolSemester.setAdapter(adapter);
        spinner_schoolSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                schoolSemester = Integer.parseInt(str);
                Log.d(TAG, "onItemClick: "+schoolSemester);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void bindListener(){
        button_back.setOnClickListener(this);
        button_confirm.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_confrim_import:{
                getTimeTable();
            }break;
            case R.id.button_importCourse_back: {

            }break;
        }
    }

    private void getTimeTable() {
    }
}
