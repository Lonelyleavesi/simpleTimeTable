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
import androidx.appcompat.app.AppCompatActivity;

import com.project.fragment.ConfirmDialogFragment;
import com.project.item.Course;
import com.project.tools.DebugHelper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


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
    Set<Course> coursesToSave;
    private void initMember(){
        context = this;
        versifyPassWd = false;
        coursesToSave= new TreeSet<>();

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

    Spinner spinner_schoolSemester;
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

    Button button_confirm;
    Button button_back;
    private void bindListener(){
        button_back.setOnClickListener(this);
        button_confirm.setOnClickListener(this);
    }

    int schoolSemester;
    EditText schoolStartYear;
    int int_schoolStartYear;
    Boolean versifyPassWd;
    TextView tv_info;
    Boolean finish_flag ;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_confrim_import:{
                finish_flag = true;
                tv_info.setText("");
                if (schoolStartYear.getText().toString().isEmpty()){
                    tv_info.setText("入学年份不能为空..");
                    return ;
                }
                int_schoolStartYear = Integer.parseInt(schoolStartYear.getText().toString());
                getTimeTable();
                while (finish_flag);
                if (versifyPassWd){
                    showDialog();
                    versifyPassWd = false;
                }
            }break;
            case R.id.button_importCourse_back: {
                finish();
            }break;
        }
    }

    /**
     * 展示确认框
     */
    public void showDialog(){
        StringBuilder Sb_coursesToSave = new StringBuilder();
        Set<String> courseName = new TreeSet<>();
        for (Course course : coursesToSave){
            courseName.add(course.getName());
        }
        for (String name : courseName){
            Sb_coursesToSave.append(name +"\n");
        }
        ConfirmDialogFragment dialog = new ConfirmDialogFragment();
        String str = "即将清除当前所有课程，并导入以下课程：\n"+Sb_coursesToSave.toString()+"\n 确认吗？";
        Log.d(TAG, "onClick: sb is"+str);
        dialog.setContent(str);
        dialog.setDialogClickListener(new ConfirmDialogFragment.onDialogClickListener() {
            @Override
            public void onSureClick() {
                deleteData();
                saveCourse();
            }
            @Override
            public void onCancelClick() {
                //这里是取消操作
            }
        });
        dialog.show(getSupportFragmentManager(),"");
    }
    public void deleteData(){
        LitePal.deleteAll(Course.class);
    }
    public void saveCourse(){
        for (Course course : coursesToSave)
            course.save();
    }

    /**
     * 取得课程表信息步骤，由于禁止主线程访问网络，所以需新启动一个线程进行网络连接取得cookie
     * 取得课程表分为三个步骤，
     * 1、通过get请求对CQU home取得cookie，
     * 2、带上取得的cookie 对LogIN网址进行post请求，带上学号密码请求类型等参数，认证
     * 3、认证成功后，通过ASP.NET_SessionId 这个cookie直接对取得课程表的网址进行get请求，可以返回课程表html
     * */
    private void getTimeTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> cookies = new HashMap<String, String>();
                try {
                    Document tableHtml = null;
                    cookies = getCookie();
                    simulatedLanding(cookies);
                    Log.d(TAG, "run: versifyPassWd is "+versifyPassWd);
                    if (versifyPassWd){
                        tableHtml = getTableHtml(cookies);
                        parseHtmlToTable(tableHtml);
                    }
                    finish_flag = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 第一步
     * @return get请求取得的cookie
     * @throws IOException
     */
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

    /**
     * 第二步
     * 带着取得的cookie进行认证
     * @param cookies
     * @throws IOException
     */
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
        updateTextInfo(doc);
        Log.d(TAG, "simulatedLanding: "+"状态码："+res.statusCode());
        Log.d(TAG, "simulatedLanding: "+"状态消息："+res.statusMessage());
        Log.d(TAG, "simulatedLanding: "+"body.："+doc.body().text());
    }

    /**
     * 模拟加密 密码
     * @param str
     * @return
     */
    private String enCodePassWd( String str){
        Log.d(TAG, "enCodePassWd: "+userName.getText().toString() + " ------ "+str);
        if (userName.getText().toString().equals("20164276")  )
            return "715811A625F35C25A341294C12B3E6";
        return "";
    }

    /**
     * 根据输入密码认证后的情况来更新显示状态的textView
     * @param doc
     */
    private void updateTextInfo(Document doc){
        Element link = doc.select("span#divLogNote").first();
        String standindInfo = link.text();
        tv_info.setText(standindInfo);
        if (standindInfo.equals("正在加载权限数据...")){
            versifyPassWd = true;
        }
    }
    /**
     * 第三步
     * 带着取得的cookie 取得课表的Docment
     * @param cookies   cookie
     * @return 课表的html的Document
     * @throws IOException
     */
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
        Log.d(TAG, "getTableHtml: "+doc.body().text());
        return doc;
    }

    /**
     * 从doc中解析课表内容并储存Course对象到Set中，
     * @param doc
     */
    public void parseHtmlToTable(Document doc){
        getTheoryClass(doc);
        getExperimentClass(doc);
    }

    /**
     * 取得理论课课程信息，添加至courseToSave
     * @param doc
     */
    public void getTheoryClass(Document doc){
        Elements tableLinks = doc.select("TABLE.page_table");
        if (tableLinks.size() == 0){
            tv_info.setText("读取课表失败，请稍后再试。。");
            versifyPassWd = false;
            return ;
        }
        //1 为理论课 3为实验课
        Elements tableRowLinks = tableLinks.get(1).select("tr");
        for (int i = 2 ; i < tableRowLinks.size(); i ++){
            Element classRow = tableRowLinks.get(i);
            Elements classInfos = classRow.select("td");
            String [ ] weekInfos = classInfos.get(10).text().split(",");
            for (String weekInfo : weekInfos){
                String [] beginAndEnd = weekInfo.split("-");
                int begin = Integer.parseInt(beginAndEnd[0]);
                int end = begin;
                if (beginAndEnd.length > 1)
                    end = Integer.parseInt(beginAndEnd[1]);
                for (int weekNo = begin ; weekNo <= end ; weekNo++){
                    Course tempCourse = new Course();
                    tempCourse.setName(classInfos.get(1).text());
                    tempCourse.setTeacherName(classInfos.get(9).text());
                    tempCourse.setClassRoom(classInfos.get(12).text());
                    String [] dayInfo = getDayInfo(classInfos.get(11).text());
                    tempCourse.setDay(Integer.parseInt(dayInfo[0]));
                    tempCourse.setStart_time(Integer.parseInt(dayInfo[1]));
                    tempCourse.setEnd_time(Integer.parseInt(dayInfo[2]));
                    tempCourse.setWeekNo(weekNo);
                    coursesToSave.add(tempCourse);
                }
            }
        }
    }

    /**
     * 根据爬取出的日程信息，取得星期几，第几节课至第几节课
     * @param dayInfo  类似 三[1-4节} 这种格式的信息
     * @return          返回一个数组，包括 星期几，第几节上课，至第几节
     */
    public String[] getDayInfo(String dayInfo){
        String [] info = new String[3];
        String[] parsedInfo = dayInfo.split("\\[");
        int day = parseNumToInt(parsedInfo[0]);
        parsedInfo = parsedInfo[1].split("节");
        parsedInfo = parsedInfo[0].split("-");
        info[0] = day+"";
        info[1] =  parsedInfo[0];
        if (parsedInfo.length > 1){
            info[2] =  parsedInfo[1];
        }else {
            info[2] =  parsedInfo[0];
        }
        return info;
    }

    /**
     * 中文数字转为阿拉伯数字，以便解析课程
     * @param NUM
     * @return
     */
    public int parseNumToInt(String NUM){
        if (NUM.equals("一"))
            return 1;
        if (NUM.equals("二"))
            return 2;
        if (NUM.equals("三"))
            return 3;
        if (NUM.equals("四"))
            return 4;
        if (NUM.equals("五"))
            return 5;
        if (NUM.equals("六"))
            return 6;
        if (NUM.equals("七") || NUM.equals("日"))
            return 7;
        return -1;
    }

    private void getExperimentClass(Document doc) {
    }
}
