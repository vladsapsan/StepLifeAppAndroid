package com.StepLife.steplifeapp.StafFunction.Add;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.MyRecyclerViewTagsAdapter;
import com.StepLife.steplifeapp.ui.AddArticleViewModel;
import com.StepLife.steplifeapp.Model.Article;
import com.StepLife.steplifeapp.ui.ArticleListAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddNewArticle extends AppCompatActivity implements MyRecyclerViewTagsAdapter.ItemClickListener{

    private static final int SELECT_PICTURE = 1;
    private AddArticleViewModel mViewModel;
    RecyclerView RecycleviewTags ;
    BottomSheetDialog bottomSheetWaitDialog;
    private WebView WebRedactor;
    MyRecyclerViewTagsAdapter adapterArticleTags;
    ArticleListAdapter ArticleListAdapter;
    private ScrollView addArticleScrollView;
    private HorizontalScrollView ImageAddArticleScrolView;
    private DatabaseReference mDataBase;
    ImageView ExitBtn,NextBtn;
    ArrayAdapter ChooseArticleListAdapter;
    ContentResolver cr;
    InputStream is;
    private ImageView LoadPicture;
    private FrameLayout EditTextButtonsFrame;
    LinearLayout linearLayout;
    private EditText Header,Main;
    private TextView HeaderView,MainView,CleanFocus;
    private RadioButton HeaderButton,TextButton,CircleButton,NumericButton;
    private ImageView Downloadpreviewimage;
    private static final String Tags_Key ="AllTags";
    Button AddRecomendationButton,AddTagsArticle;
    private Uri uploadArticleMainTextUri = null;
    DisplayMetrics displayMetrics;
    List<String> mTags,mNewArticleTags;
    private ArrayList <Article> listTemp = new ArrayList<Article>();
    ArrayList<String> ArticleRecomendationList = new ArrayList<>();
    ListView TagsList,ListView,ListView1;
    private Uri uploadArticlePhotoUri = null;
    StorageReference storageRef;
    AlertDialog alertDialog;
    ProgressBar progresscheck;
    FirebaseStorage storage;
    private String Article_Key ="AllArticle";
    private RadioGroup EditTextRG;
    private final static String Non_Public_Article_Key ="AllNonPublicArticle";
    private boolean DoneArticle,bottomsheetstart,DoneDowndloadPhoto;

    public static int getDeviceWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getRealMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static int getDeviveHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getRealMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        return heightPixels;
    }


    private void GetImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }




    //загрузка фото
    @SuppressLint("SetTextI18n")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                //
                Uri selectedImageUri = data.getData();
                if (bottomsheetstart) {
                    if (selectedImageUri != null) {
                        ImageView Image = new ImageView(AddNewArticle.this);
                        Image.setImageURI(selectedImageUri);
                        Downloadpreviewimage.setImageURI(selectedImageUri);
                    }
                } else {

                    progresscheck.setVisibility(View.VISIBLE);
                    NextBtn.setVisibility(View.GONE);

                    alertDialog.show();

                    ImageView Image = new ImageView(AddNewArticle.this);
                    Image.setImageURI(selectedImageUri);

                    displayMetrics = new DisplayMetrics();
                    Main.append("\n");
                    Main.append("|");
                    SpannableString MainSpannabletext = new SpannableString(Main.getText());

                    getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
                    try {
                        if (selectedImageUri != null) {
                            is = AddNewArticle.this.getContentResolver().openInputStream(selectedImageUri.normalizeScheme());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    //оптимизация высоты загруженной фотографии
                    Drawable photo = Drawable.createFromStream(is, "Photo");


                    double OptimizationHeight = ((double) photo.getIntrinsicWidth() / (double) photo.getIntrinsicHeight());


                    double DownloadPhotoHeight = ((getDeviceWidth(AddNewArticle.this)) / OptimizationHeight);


                    Bitmap PhotoPreviewBitMap = ((BitmapDrawable) photo).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PhotoPreviewBitMap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] PhotoPreviewByteArray = baos.toByteArray();
                    StorageReference MainPhotoRef = storageRef.child(System.nanoTime() + "PreviewImage");

                    UploadTask uploadTaskPhoto = MainPhotoRef.putBytes(PhotoPreviewByteArray);
                    uploadTaskPhoto.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewArticle.this, "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
                            progresscheck.setVisibility(View.INVISIBLE);
                            NextBtn.setVisibility(View.VISIBLE);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progresscheck.setVisibility(View.INVISIBLE);
                            NextBtn.setVisibility(View.VISIBLE);
                        }
                    });

                    Task<Uri> taskPhoto = uploadTaskPhoto.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return MainPhotoRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            uploadArticlePhotoUri = task.getResult();
                            try {
                                photo.setBounds(0, 0, getDeviceWidth(AddNewArticle.this), (int) DownloadPhotoHeight);
                                ImageSpan span = new ImageSpan(photo, String.valueOf(uploadArticlePhotoUri), ImageSpan.ALIGN_BASELINE);

                                MainSpannabletext.setSpan(span, MainSpannabletext.length() - 1, MainSpannabletext.length(), 0);

                                Main.setText(MainSpannabletext);
                                Main.append("\n");

                                Main.clearFocus();
                                alertDialog.dismiss();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }
            }
        }
    }

    //Инициализация компонентов базы данных
    void initialization(){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("AllArticleBase");
        mDataBase = FirebaseDatabase.getInstance().getReference("AllArticle");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_article);


        //инициализация бд
        initialization();

        progresscheck = findViewById(R.id.progressBaraddArticle);
        linearLayout = findViewById(R.id.PictureLayout);

        EditTextRG = findViewById(R.id.RadioGroupTextEdit);

        //Отображение тегов в списке новой статьи
        RecycleviewTags = findViewById(R.id.RecycleviewTags);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        RecycleviewTags.setLayoutManager(layoutManager);
        mNewArticleTags = new ArrayList<>();
        adapterArticleTags = new MyRecyclerViewTagsAdapter(AddNewArticle.this,mNewArticleTags);
        adapterArticleTags.setClickListener(AddNewArticle.this);
        RecycleviewTags.setAdapter(adapterArticleTags);
        

        //Диалог Загрузки обложки
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddNewArticle.this, R.style.BottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.sheetdownloadpreviewphotoarticle,
                        (FrameLayout) findViewById(R.id.SheetDialogPhotoContainer)
                );
        //Загрузка фото
        bottomSheetView.findViewById(R.id.buttonDowloandphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Downloadpreviewimage = bottomSheetView.findViewById(R.id.Downloadpreviewimage);
                bottomsheetstart = true;
                GetImage();
            }
        });
        bottomSheetView.findViewById(R.id.buttonNotDowloandphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                DoneArticle = true;
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);


        //Диалог выбора тега для статьи
        BottomSheetDialog bottomSheetAddDialog = new BottomSheetDialog(AddNewArticle.this, R.style.BottomSheetDialog);
        bottomSheetAddDialog.setDismissWithAnimation(true);
        bottomSheetAddDialog.setCanceledOnTouchOutside(false);
        View bottomSheetViewAddTags = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.bottom_sheet_add_tags_dialog,
                        (FrameLayout) findViewById(R.id.SheetDialogAddArticleTagContainer)
                );
        TagsList = bottomSheetViewAddTags.findViewById(R.id.ListView);
        bottomSheetAddDialog.setContentView(bottomSheetViewAddTags);
        //Добавление тега в лист всех тегов новой статьи
        TagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mNewArticleTags.add(mTags.get(position));
                adapterArticleTags.notifyDataSetChanged();
                bottomSheetAddDialog.dismiss();
            }
        });

        EditTextRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.HeaderText:
                        SpannableStringBuilder spannable = new SpannableStringBuilder(Main.getText());
                        StyleSpan b = new StyleSpan(Typeface.BOLD);
                        spannable.setSpan(b,Main.getSelectionStart(),Main.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        spannable.setSpan(new AbsoluteSizeSpan(45),Main.getSelectionStart(),Main.getSelectionEnd(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        Main.setText(spannable);
                        Main.setSelection(Main.getText().length());

                        break;
                    case R.id.UsualText:
                        SpannableStringBuilder spannable1 = new SpannableStringBuilder(Main.getText());
                        StyleSpan b1 = new StyleSpan(Typeface.NORMAL);
                        spannable1.setSpan(b1,Main.getSelectionStart(),Main.getSelectionEnd(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        spannable1.setSpan(new AbsoluteSizeSpan(30),Main.getSelectionStart(),Main.getSelectionEnd(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        Main.setText(spannable1);
                        Main.setSelection(Main.getText().length());
                        break;
                    default:
                        break;
                }
            }
        });



        EditTextRG = findViewById(R.id.RadioGroupTextEdit);

        //Кнопки Редактирования текста не используется!!!
        HeaderButton = (RadioButton) findViewById(R.id.HeaderText);
        TextButton = (RadioButton)  findViewById(R.id.UsualText);

        ImageAddArticleScrolView = findViewById(R.id.ImageAddArticleScrolView);
        addArticleScrollView  = findViewById(R.id.addArticleScrollView);
        EditTextButtonsFrame = findViewById(R.id.EditTextButtonsFrame);

        Header = findViewById(R.id.TextEditHeader);
        CleanFocus = findViewById(R.id.CleanFocus);
        Main = findViewById(R.id.TextEditMainText);
        //Кнопка (Готово) снимает фокус с текста и убирает клавиатуру
        CleanFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = AddNewArticle.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                CleanFocus.setTextColor(getResources().getColor(R.color.MainGrayText));
                Main.clearFocus();
            }
        });

        //Кнопка добавления тегов
        AddTagsArticle = findViewById(R.id.AddTagsArticle);
        AddTagsArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetAddDialog.show();
            }
        });


        //Окно загрузки фото
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewArticle.this);
        View layout_dialog = LayoutInflater.from(AddNewArticle.this).inflate(R.layout.download_image_dialog,null);
        builder.setView(layout_dialog);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        //Кнопка загрузки фото
        LoadPicture = findViewById(R.id.addPicturetoArticle);
        LoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetImage();
            }
        });


        //Кнопка далее позволяющая загрзить статью
        NextBtn = findViewById(R.id.GoToFinalViewButton);
        NextBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                if (Main.getText().length() > 0 && Header.length() > 0) {
                    if (DoneArticle == false) {
                        bottomSheetDialog.show();
                    }
                    if (DoneArticle == true) {
                        //Подключение storage
                        mDataBase = FirebaseDatabase.getInstance().getReference(Non_Public_Article_Key);
                        progresscheck = findViewById(R.id.progressBaraddArticle);
                        progresscheck.setVisibility(View.VISIBLE);
                        NextBtn.setVisibility(View.GONE);
                        EditTextButtonsFrame.setClickable(false);
                        EditTextButtonsFrame.setEnabled(false);


                        //Текст с сохраненным форматированием
                        Spanned HeadSpanned = Html.fromHtml(Header.getText().toString());
                        Editable HeadViewText = (Editable) HeadSpanned;
                        String HeadString = Html.toHtml(HeadViewText);


                        //Фото превью
                        Bitmap PhotoPreviewBitMap = ((BitmapDrawable) Downloadpreviewimage.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PhotoPreviewBitMap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                        byte[] PhotoPreviewByteArray = baos.toByteArray();
                        StorageReference MainPhotoRef = storageRef.child(System.nanoTime() + "PreviewImage");

                        UploadTask uploadTaskPhoto = MainPhotoRef.putBytes(PhotoPreviewByteArray);
                        uploadTaskPhoto.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });

                        Task<Uri> taskPhoto = uploadTaskPhoto.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                return MainPhotoRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                uploadArticlePhotoUri = task.getResult();
                                String idArticle = mDataBase.push().getKey();
                                Article newArticle;
                                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                                String Simpledate = df.format(Calendar.getInstance().getTime());
                                if(mNewArticleTags!=null){
                                    if(ArticleRecomendationList.size()>0){
                                        newArticle = new Article(idArticle, Simpledate, HeadString, (Html.toHtml(Main.getText())), uploadArticlePhotoUri.toString(), (ArrayList<String>) mNewArticleTags,ArticleRecomendationList);
                                    }else {
                                        newArticle = new Article(idArticle, Simpledate, HeadString, (Html.toHtml(Main.getText())), uploadArticlePhotoUri.toString(), (ArrayList<String>) mNewArticleTags);
                                    }
                                }else {
                                    newArticle = new Article(idArticle, Simpledate, HeadString, (Html.toHtml(Main.getText())), uploadArticlePhotoUri.toString());
                                }
                                mDataBase.child(idArticle).setValue(newArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progresscheck.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "Статья успешно добавлена в неопубликованные", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }
                        });

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Не полная публикация...", Toast.LENGTH_SHORT).show();
                }
            }

        });


        //Редактор заголовка

        Header.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(Header.hasFocus())
                {
                    EditTextButtonsFrame.setVisibility(View.INVISIBLE);
                }
                else {
                    EditTextButtonsFrame.setVisibility(View.VISIBLE);
                }
            }
        });

        //Проверка основного текста
        Main.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CleanFocus.setClickable(true);
                CleanFocus.setTextColor(getResources().getColor(R.color.MainBlue));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });



        //Кнопка выхода
        ExitBtn = findViewById(R.id.imagebackAddArticle);
        ExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void DataRefresh(){
        mDataBase = FirebaseDatabase.getInstance().getReference(Tags_Key);
        mDataBase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> map = new HashMap<String, String>();
                map = (HashMap<String, String>) task.getResult().getValue();
                mTags = new ArrayList<>(map.values());
                ArrayAdapter adapter = new ArrayAdapter<>(AddNewArticle.this, android.R.layout.simple_list_item_1, mTags);
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

    @Override
    public void onItemClick(View view, int position) {
        mNewArticleTags.remove(position);
        adapterArticleTags.notifyDataSetChanged();
    }
}