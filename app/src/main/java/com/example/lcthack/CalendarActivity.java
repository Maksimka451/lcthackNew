package com.example.lcthack;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity {

    RecyclerView slotsRecyclerView;
    SlotAdapter slotAdapter, slotAdapter2;
    List<Slot> slots;
    List<Slot> slotsBuf;

    String[] data = new String[5];

    Spinner spinnerKNO, spinnerControl, spinnerTopic;

    RecyclerView calendarRecyclerView;
    CalendarAdapter calendarAdapter;
    TextView mouth;
    List<Date> dates;
    Set<Integer> coloredPositions;
    String[] daysOfWeek = new String[7];

    String[] dateForBase;

    AdapterView<?> bufParent;
    int bufPosition;
    int mouthJSON;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.lcthack.R.layout.activity_calendar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottm_navigation);
        bottomNavigationView.setSelectedItemId(R.id.services);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.inside_content){
                    startActivity(new Intent(getApplicationContext()
                            , InsideContent.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if(item.getItemId() == R.id.services){

                    return true;
                } else if(item.getItemId() == R.id.chat){
                    startActivity(new Intent(getApplicationContext()
                            , Chat.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


        spinnerKNO = findViewById(com.example.lcthack.R.id.spinnerKNO);
        spinnerControl = findViewById(com.example.lcthack.R.id.spinner_control);
        spinnerTopic = findViewById(com.example.lcthack.R.id.spinner_topic);

        try {
            JSONObject json = new JSONObject(loadJSON("data.json"));
            JSONArray topics = json.getJSONArray("Темы консультирования");
            Map<String, Set<String>> KNOToControlMap = new HashMap<>();
            Map<String, Set<String>> controlToTopicMap = new HashMap<>();
            for (int i = 0; i < topics.length(); i++) {
                JSONObject topic = topics.getJSONObject(i);
                String kno = topic.getString("КНО");
                String control = topic.getString("Вид контроля");
                String topicName = topic.getString("Тема консультирования");

                if (KNOToControlMap.containsKey(kno)) {
                    KNOToControlMap.get(kno).add(control);
                } else {
                    Set<String> controlList = new HashSet<>();
                    controlList.add(control);
                    KNOToControlMap.put(kno, controlList);
                }
                if (controlToTopicMap.containsKey(control)) {
                    controlToTopicMap.get(control).add(topicName);
                } else {
                    Set<String> topicList = new HashSet<>();
                    topicList.add(topicName);
                    controlToTopicMap.put(control, topicList);
                }
            }

            ArrayAdapter<String> KNOAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, KNOToControlMap.keySet().toArray(new String[0]));
            KNOAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerKNO.setAdapter(KNOAdapter);

            spinnerKNO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    List<String> controlsForKNO = new ArrayList<>(KNOToControlMap.get(parent.getItemAtPosition(position)));

                    bufParent = parent;
                    bufPosition = position;
                    startCalendarAdapter(mouthJSON);
                    ArrayAdapter<String> ControlAdapter = new ArrayAdapter<>(CalendarActivity.this, android.R.layout.simple_spinner_item, controlsForKNO);
                    ControlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerControl.setAdapter(ControlAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ArrayAdapter<String> ControlAdapter = new ArrayAdapter<>(CalendarActivity.this, android.R.layout.simple_spinner_item, (List) KNOToControlMap.get(parent.getItemAtPosition(0)));
                    ControlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerKNO.setAdapter(ControlAdapter);
                }
            });

            spinnerControl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    List<String> topicsForControl = new ArrayList<>(controlToTopicMap.get(parent.getItemAtPosition(position)));
                    data[1] = String.valueOf(parent.getItemAtPosition(position));
                    ArrayAdapter<String> TopicAdapter = new ArrayAdapter<>(CalendarActivity.this, android.R.layout.simple_spinner_item, topicsForControl);
                    TopicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTopic.setAdapter(TopicAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    data[1] = String.valueOf(parent.getItemAtPosition(0));
                    ArrayAdapter<String> TopicAdapter = new ArrayAdapter<>(CalendarActivity.this, android.R.layout.simple_spinner_item, (List) controlToTopicMap.get(parent.getItemAtPosition(0)));
                    TopicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTopic.setAdapter(TopicAdapter);
                }
            });

            spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    data[2] = String.valueOf(parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    data[2] = String.valueOf(parent.getItemAtPosition(0));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M");
        Date date = new Date();
        mouthJSON = Integer.parseInt(simpleDateFormat.format(date)) - 1;

        slotsRecyclerView = findViewById(com.example.lcthack.R.id.slotsRecyclerView);
        slots = new ArrayList<>();
        slotsBuf = new ArrayList<>();
        slotAdapter = new SlotAdapter(slotsBuf);


        parseJSONFile(mouthJSON);

        calendarRecyclerView = findViewById(com.example.lcthack.R.id.calendarRecyclerView);
        mouth = findViewById(com.example.lcthack.R.id.mouth);
        String[] months = getResources().getStringArray(com.example.lcthack.R.array.months_array);
        String[] selectedMonth = months[mouthJSON].split(" ");
        mouth.setText(selectedMonth[1]);

        mAuth = FirebaseAuth.getInstance();

    }

    public void startCalendarAdapter(int mouth){
        dates = getDates(mouth);
        coloredPositions = getColoredPositions();
        calendarAdapter = new CalendarAdapter(dates, coloredPositions, daysOfWeek);
        RecyclerView.LayoutManager layoutManagerCalendar = new GridLayoutManager(CalendarActivity.this, 7);
        calendarRecyclerView.setLayoutManager(layoutManagerCalendar);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarAdapter.setItemClickListener(position -> {
            SimpleDateFormat format = new SimpleDateFormat("M/d/yy");
            List<Slot> slotsDay = new ArrayList<>();
            for(Slot item : slotsBuf){
                if(item.getDate().equals(format.format(dates.get(position - 7)))) slotsDay.add(item);
            }
            slotAdapter2 = new SlotAdapter(slotsDay);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CalendarActivity.this);
            slotsRecyclerView.setLayoutManager(layoutManager);
            slotsRecyclerView.setAdapter(slotAdapter2);

            slotAdapter2.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    // Создание и отображение диалогового окна
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                    builder.setTitle("Подтверждение")
                            .setMessage("Вы уверены, что хотите продолжить?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    data[0] = slotsDay.get(position).getDepartment();
                                    data[3] = slotsDay.get(position).getDate();
                                    data[4] = slotsDay.get(position).getTime();

                                    final DatabaseReference RootRef;
                                    RootRef = FirebaseDatabase.getInstance().getReference();
                                    final DatabaseReference RootRef2;
                                    RootRef2 = FirebaseDatabase.getInstance().getReference();

                                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(!(snapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Consulting").child(data[0]).exists()))
                                            {
                                                HashMap<String, Object> userDataMap = new HashMap<>();
                                                userDataMap.put("kno", data[0]);
                                                userDataMap.put("typeofcontrol", data[1]);
                                                userDataMap.put("themeofconsulting", data[2]);
                                                userDataMap.put("date", data[3]);
                                                userDataMap.put("time", data[4]);
//                                                userDataMap.put("useremail", mAuth.getCurrentUser().getEmail());
//                                                userDataMap.put("userid", mAuth.getCurrentUser().getUid());

                                                RootRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Consulting").child(data[0]).updateChildren(userDataMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(CalendarActivity.this, "База данных обновилась!", Toast.LENGTH_SHORT ).show();
                                                                } else {
                                                                    Toast.makeText(CalendarActivity.this, "Не удалось обновить базу данных!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            } else
                                            {
                                                Toast.makeText(CalendarActivity.this, "Произошла ошибка!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(!(snapshot.child("Consulting").child(data[0]).exists()))
                                            {
                                                HashMap<String, Object> userDataMap = new HashMap<>();
                                                userDataMap.put("kno", data[0]);
                                                userDataMap.put("typeofcontrol", data[1]);
                                                userDataMap.put("themeofconsulting", data[2]);
                                                userDataMap.put("date", data[3]);
                                                userDataMap.put("time", data[4]);
//                                                userDataMap.put("useremail", mAuth.getCurrentUser().getEmail());
//                                                userDataMap.put("userid", mAuth.getCurrentUser().getUid());

                                                RootRef.child("Consulting").child(data[0]).updateChildren(userDataMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(CalendarActivity.this, "База данных обновилась!", Toast.LENGTH_SHORT ).show();
                                                                } else {
                                                                    Toast.makeText(CalendarActivity.this, "Не удалось обновить базу данных!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            } else
                                            {
                                                Toast.makeText(CalendarActivity.this, "Произошла ошибка!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            })
                            .setNegativeButton("Отмена", null)
                            .show();
                }









            });
        });
    }
    public void onMonthClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(com.example.lcthack.R.array.months_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Обработка выбора месяца
                        String[] months = getResources().getStringArray(com.example.lcthack.R.array.months_array);
                        String[] selectedMonth = months[which].split(" ");

                        parseJSONFile(Integer.parseInt(selectedMonth[0])-1);

                        mouth.setText(selectedMonth[1]);
                        mouthJSON = Integer.parseInt(selectedMonth[0])-1;

                        startCalendarAdapter(Integer.parseInt(selectedMonth[0])-1);
                    }
                })
                .show();
    }

    private List<Date> getDates(int mouth) {
        List<Date> dates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = mouth;
        int currentYear = calendar.get(Calendar.YEAR);

        calendar.set(currentYear, currentMonth, 1);

        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - firstDayOfMonth + 2);

        while (calendar.get(Calendar.MONTH) != currentMonth) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendar.set(currentYear, currentMonth, 1);

        while (calendar.get(Calendar.MONTH) == currentMonth) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendar.set(currentYear, currentMonth + 1, 1);

        while (dates.size() < 42) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }

    private Set<Integer> getColoredPositions() {
        Set<Integer> coloredPositions = new HashSet<>();
        SimpleDateFormat formatter = new SimpleDateFormat("M/d/yy");
        List<Date> dateList = new ArrayList<>();
        dateList.addAll(dates);
        List<Slot> bufSlots = new ArrayList<>();
        for (Slot slot : slots) {
            if ((slot.getDepartment().toLowerCase().contains(bufParent.getItemAtPosition(bufPosition).toString().toLowerCase()))
                    || (bufParent.getItemAtPosition(bufPosition).toString().toLowerCase().contains(slot.getDepartment().toLowerCase()))){
                bufSlots.add(slot);
            }
        }
        slotsBuf.clear();
        slotsBuf.addAll(bufSlots);
        slotAdapter.notifyDataSetChanged();
        for(int i = 0; i < 42; i++){
            for(Slot item : slotsBuf) {
                if (item.getDate().contains(formatter.format(dateList.get(i)))) coloredPositions.add(i);
            }
        }
        return coloredPositions;
    }
    private void parseJSONFile(int m) {
        try {
            JSONObject json = new JSONObject(loadJSONFromAsset(m));

            JSONArray keys = json.names();

            slots.clear();
            if (keys != null) {
                for (int i = 0; i < keys.length(); i++) {
                    String key = keys.getString(i);
                    JSONArray slotsArray = json.getJSONArray(key);

                    for (int j = 0; j < slotsArray.length(); j++) {
                        JSONObject slotObj = slotsArray.getJSONObject(j);
                        String department = slotObj.keys().next();
                        String dateStr = slotObj.getString(department);
                        String timeStr = slotObj.getString("undefined");

                        Slot slot = new Slot(department, dateStr, timeStr);
                        slots.add(slot);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String loadJSONFromAsset(int m) {
        String json;
        String[] months = getResources().getStringArray(com.example.lcthack.R.array.mouths_en);
        String[] selectedMonth = months[m].split(" ");
        try {
            InputStream inputStream = getAssets().open( selectedMonth[1] + ".json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private String loadJSON(String path) {
        try {
            InputStream inputStream = getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            inputStream.close();
            reader.close();

            String jsonString = jsonStringBuilder.toString();
            return jsonString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}