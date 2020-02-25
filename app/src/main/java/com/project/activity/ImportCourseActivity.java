package com.project.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImportCourseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SEMESTER_NUM = 8;
    private static final String TAG = "TimeTable";
    private static final String CQU_JXGL_HOME_URL = "http://jxgl.cqu.edu.cn/home.aspx";
    private static final String CQU_JXGL_LOGIN_URL = "http://jxgl.cqu.edu.cn/_data/index_login.aspx";
    private static final String CQU_JXGL_GET_TABLE_URL = "http://jxgl.cqu.edu.cn/znpk/Pri_StuSel_rpt.aspx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_course);
        initMember();
        bindListener();
        getSupportActionBar().hide();
    }
    Context context;
    EditText userName;
    EditText passWord;
    EditText verifyCode;
    Button button_confirm;
    Button button_back;
    TextView tv_info;
    Spinner spinner_schoolSemester;
    int schoolSemester;
    EditText schoolStartYear;
    int int_schoolStartYear;
    private void initMember(){
        context = this;

        tv_info = (TextView)findViewById(R.id.textView_import_info);
        userName = (EditText)findViewById(R.id.editText_stu_no);
        passWord = (EditText)findViewById(R.id.editText_stu_passwd);
        verifyCode = (EditText)findViewById(R.id.editText_Verification_code);

        spinner_schoolSemester = (Spinner) findViewById(R.id.spinner_semester);
        schoolStartYear = (EditText) findViewById(R.id.editText_schoolYear);
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
                if (!schoolStartYear.getText().toString().isEmpty()){
                    int_schoolStartYear = Integer.parseInt(schoolStartYear.getText().toString());
                }
                getTimeTable();
            }break;
            case R.id.button_importCourse_back: {
                finish();
            }break;
        }
    }

    private void getTimeTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> cookies = new HashMap<String, String>();
                try {
                    Document tableHtml = null;
                    cookies = getCookie();
                    simulatedLanding(cookies);
                    tableHtml = getTableHtml(cookies);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Map<String,String> getCookie() throws IOException {
        Map<String, String> cookie = new HashMap<String, String>();
        String url = CQU_JXGL_HOME_URL;
        Connection.Response response = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .method(Connection.Method.GET)
                .execute();
        cookie = response.cookies();
        System.out.println(response.statusCode());
        System.out.println(response.cookies());
        for (Map.Entry<String, String> entry : cookie.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        return  cookie;
    }


    private void simulatedLanding( Map<String, String> cookies) throws IOException {
        Map<String, String> datas = new HashMap<String,String>();
        datas.put("Sel_Type", "STU");
        datas.put("txt_dsdsdsdjkjkjc", userName.getText().toString());
        String passWd = enCodePassWd(passWord.getText().toString());
        Log.d(TAG, "simulatedLanding: 密码解析："+passWd);
        // 密码被加密为 "715811A625F35C25A341294C12B3E6"
        datas.put("efdfdfuuyyuuckjg", passWd);

        Connection connection = Jsoup.connect(CQU_JXGL_LOGIN_URL);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
        connection.cookies(cookies);
        connection.ignoreHttpErrors(true);
        connection.ignoreContentType(true);

        Connection.Response res = connection
                .data(datas)
                .method(Connection.Method.POST)
                .execute();
        Document doc = res.parse();
        Log.d(TAG, "simulatedLanding: "+"状态码："+res.statusCode());
        Log.d(TAG, "simulatedLanding: "+"状态消息："+res.statusMessage());
        Log.d(TAG, "simulatedLanding: "+"body.："+doc.body().text());
    }

    private String enCodePassWd( String str){
        Log.d(TAG, "enCodePassWd: "+userName.getText().toString() + " ------ "+str);
        if (userName.getText().toString().equals("20164276")  && str.equals("541298") )
            return "715811A625F35C25A341294C12B3E6";
        return "";
    }

    private Document getTableHtml(Map<String,String> cookies) throws IOException {
        Map<String, String> datas = new HashMap<String,String>();
        //取出第几年，比如2016年入学 第1、2学期为 20160 20161
        String semester = (int_schoolStartYear+(schoolSemester+1)/2)-1+"";
        //取出学期
        if (schoolSemester % 2 == 0){
            semester = semester + "1";
        }else{
            semester = semester + "0";
        }
        Log.d(TAG, "getTableHtml: 学期代码"+semester);
        datas.put("Sel_XNXQ", semester);
        datas.put("rad", "on");
        datas.put("px", "1");
        System.out.println("开始取得课程表。。。");
        Connection connection = Jsoup.connect(CQU_JXGL_GET_TABLE_URL);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
        connection.cookies(cookies);

        Connection.Response res = connection
                .data(datas)
                .method(Connection.Method.POST)
                .execute();
        Document doc = res.parse();
        String sessionId = res.cookie("ASP.NET_SessionId"); //需要检查正确的cookie名称
        Log.d(TAG, "getTableHtml: "+doc.body().text());
        return doc;
    }
}
