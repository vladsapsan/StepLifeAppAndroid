package com.StepLife.steplifeapp.StafFunction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.StepLife.steplifeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddTagsArticle extends AppCompatActivity {

    CardView imagebackAddTags,AddTagsText;
    EditText editTextTag;
    private static final String Tags_Key ="AllTags";

    List<String> mTags;
    private DatabaseReference mDataBase;
    ListView TagsList;

    //Иницилизация компонентов
    private void initilization()
    {
        mDataBase = FirebaseDatabase.getInstance().getReference(Tags_Key);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags_article);

        //Дефолт стиль
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        initilization();
        TagsList = findViewById(R.id.TagsList);

        //Плашка добавления тегов
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        View bottomSheetView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.sheet_dialog_add_tag,
                        (FrameLayout) findViewById(R.id.SheetDialogAddTagContainer)
                );
        //Кнопка добавление тега
        editTextTag = bottomSheetView.findViewById(R.id.editTextTag);
        bottomSheetView.findViewById(R.id.ButtonAddTag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextTag.getText().toString()!=null&&!editTextTag.getText().equals("")){
                    mDataBase.push().setValue(editTextTag.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DataRefresh();
                            bottomSheetDialog.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Поле тега пустое...",Toast.LENGTH_SHORT);
                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);


        AddTagsText = findViewById(R.id.AddTagsText);
        AddTagsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        //Закрытие окна
        imagebackAddTags = findViewById(R.id.imagebackAddTags);
        imagebackAddTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void DataRefresh(){
        mDataBase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> map = new HashMap<String, String>();
                map = (HashMap<String, String>) task.getResult().getValue();
                mTags = new ArrayList<>(map.values());
                ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, mTags);
                TagsList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        DataRefresh();
    }
}