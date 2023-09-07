package com.example.steplifeapp.ui;

import static android.app.Activity.RESULT_OK;
import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steplifeapp.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.common.primitives.Chars;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.input.CharSequenceReader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.IntStream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class AddArticleFragment extends Fragment {

    private static final int SELECT_PICTURE = 1;
    private AddArticleViewModel mViewModel;
    private WebView WebRedactor;
    private ScrollView addArticleScrollView;
    private HorizontalScrollView ImageAddArticleScrolView;
    private DatabaseReference mDataBase;
    ImageView ExitBtn,NextBtn;
    ContentResolver cr;
    InputStream is;
    private ImageView LoadPicture;
    private FrameLayout EditTextButtonsFrame;
    LinearLayout linearLayout;
    private EditText Header,Main;
    private TextView HeaderView,MainView,CleanFocus;
    private RadioButton HeaderButton,TextButton,CircleButton,NumericButton;
    private ImageView Downloadpreviewimage;
    private Uri uploadArticleMainTextUri = null;
    private Uri uploadArticlePhotoUri = null;
    StorageReference storageRef;
    ProgressBar progresscheck;
    FirebaseStorage storage;
    private RadioGroup EditTextRG;
    private boolean DoneArticle,bottomsheetstart,DoneDowndloadPhoto;



    public static AddArticleFragment newInstance() {
        return new AddArticleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_article, container, false);
    }


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
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                if(bottomsheetstart)
                {
                    if (selectedImageUri != null) {
                        ImageView Image = new ImageView(getContext());
                        Image.setImageURI(selectedImageUri);
                        Downloadpreviewimage.setImageURI(selectedImageUri);

                    }
                }
                else {


                    progresscheck.setVisibility(View.VISIBLE);
                    NextBtn.setVisibility(View.GONE);

                    ImageView Image = new ImageView(getContext());
                    Image.setImageURI(selectedImageUri);


                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        Main.append("\n");
                        Main.append("|");
                        SpannableString MainSpannabletext = new SpannableString(Main.getText());


                        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);


                        try {
                            if (selectedImageUri != null) {
                                is = getContext().getContentResolver().openInputStream(selectedImageUri.normalizeScheme());
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        //оптимизация высоты загруженной фотографии
                        Drawable photo = Drawable.createFromStream(is, "Photo");


                        double OptimizationHeight = ((double) photo.getIntrinsicWidth()/(double)photo.getIntrinsicHeight());


                        double DownloadPhotoHeight = ((getDeviceWidth(getContext())) / OptimizationHeight);


                    Bitmap PhotoPreviewBitMap = ((BitmapDrawable)photo).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PhotoPreviewBitMap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] PhotoPreviewByteArray = baos.toByteArray();
                    StorageReference MainPhotoRef = storageRef.child(System.nanoTime()+"PreviewImage");

                    UploadTask uploadTaskPhoto = MainPhotoRef.putBytes(PhotoPreviewByteArray);
                    uploadTaskPhoto.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
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
                                photo.setBounds(0, 0, getDeviceWidth(getContext()),(int) DownloadPhotoHeight);
                                ImageSpan span = new ImageSpan(photo, String.valueOf(uploadArticlePhotoUri), ImageSpan.ALIGN_BASELINE);


                                MainSpannabletext.setSpan(span, MainSpannabletext.length() - 1, MainSpannabletext.length(), 0);

                                //MainSpannabletext.setSpan(span, MainSpannabletext.length() - 2, MainSpannabletext.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                Main.setText(MainSpannabletext);
                                Main.append("\n");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                        // (int)DownloadPhotoHeight




                    // MainSpannabletext.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


                //    CardView cardview = new CardView(getContext());
                //    FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(
                //            FrameLayout.LayoutParams.WRAP_CONTENT,
                //            FrameLayout.LayoutParams.WRAP_CONTENT
                //    );
                //    layoutparams.rightMargin = 50;

                //    cardview.setLayoutParams(layoutparams);

                //    cardview.setRadius(18);
                //    cardview.setMinimumHeight(550);
              //      cardview.setMinimumWidth(410);


                //    Image.setLayoutParams(new ViewGroup.LayoutParams(410, 550));

                //    cardview.addView(Image);

                 //   ImageView ImageButton = new ImageView(getContext());
                 //   ImageButton.setOnClickListener(new View.OnClickListener() {
                //        @Override
                //        public void onClick(View v) {
               //             linearLayout.removeView(cardview);
                //        }
              //      });
              //      ImageButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_close_foreground));
              //      FrameLayout.LayoutParams layoutparams1 = new FrameLayout.LayoutParams(
              //              FrameLayout.LayoutParams.WRAP_CONTENT,
              //              FrameLayout.LayoutParams.WRAP_CONTENT
              //      );
              //      layoutparams1.gravity = 5;
               //     layoutparams1.topMargin =20;

               //     layoutparams1.rightMargin =20;
              //      layoutparams1.width = 80;
               //     layoutparams1.height = 80;

              //      ImageButton.setLayoutParams(layoutparams1);
              //      cardview.addView(ImageButton);

              //      linearLayout.addView(cardview);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddArticleViewModel.class);

    }


    //Текст урока в байтовый поток данных
    public static  byte[] toByteArray(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        byte[] barr = new byte[charSequence.length()];
        for (int i = 0; i < barr.length; i++) {
            barr[i] = (byte) charSequence.charAt(i);
        }

        return barr;
    }



    //Смена шрифта текста
    public void setTextWithSpan(TextView textView, String text, String spanText, StyleSpan style) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        int start = text.indexOf(spanText);
        int end = start + spanText.length();
        sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("AllArticleBase");

        progresscheck = view.findViewById(R.id.progressBaraddArticle);
        linearLayout = view.findViewById(R.id.PictureLayout);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.sheetdownloadpreviewphotoarticle,
                        (FrameLayout) view.findViewById(R.id.SheetDialogPhotoContainer)
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





        mDataBase = FirebaseDatabase.getInstance().getReference("AllArticle");

        EditTextRG = view.findViewById(R.id.RadioGroupTextEdit);

        //Кнопки Редактирования текста
        HeaderButton = (RadioButton) view.findViewById(R.id.HeaderText);
        TextButton = (RadioButton)  view.findViewById(R.id.UsualText);

        ImageAddArticleScrolView = view.findViewById(R.id.ImageAddArticleScrolView);
        OverScrollDecoratorHelper.setUpOverScroll(ImageAddArticleScrolView);

        addArticleScrollView   = view.findViewById(R.id.addArticleScrollView);
        OverScrollDecoratorHelper.setUpOverScroll( addArticleScrollView);


        Header = view.findViewById(R.id.TextEditHeader);
        CleanFocus = view.findViewById(R.id.CleanFocus);

        Main = view.findViewById(R.id.TextEditMainText);
        HeaderView = view.findViewById(R.id.TextViewHeader);
        MainView = view.findViewById(R.id.TextViewMainTextEditor);

        CleanFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager inputManager = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                }
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);

                CleanFocus.setTextColor(getResources().getColor(R.color.MainGrayText));
                CleanFocus.setClickable(false);
                Main.clearFocus();

            }
        });

        LoadPicture = view.findViewById(R.id.addPicturetoArticle);
        LoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetImage();
            }
        });


        TextButton.setSelected(true);

        EditTextRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {

                    case R.id.HeaderText:
                        SpannableStringBuilder spannable = new SpannableStringBuilder(Main.getText());
                        StyleSpan b = new StyleSpan(Typeface.BOLD);
                        spannable.setSpan(b,Main.getSelectionStart(),Main.getSelectionEnd(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
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

        TextButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                {
                    //Spannable spannable = new SpannableString(Main.getText());
                   // StyleSpan b = new StyleSpan(Typeface.NORMAL);
                   // spannable.setSpan(b,Main.getSelectionStart(),Main.getSelectionEnd(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                  //  Main.setText(spannable);
                }
                else{ }
            }
        });

        HeaderButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                {
                   // Spannable spannable = new SpannableString(Main.getText());
                   // StyleSpan b = new StyleSpan(Typeface.BOLD);
                  //  spannable.setSpan(b,Main.getSelectionStart(),Main.getSelectionEnd(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                   // Main.setText(spannable);

                }
                else{ }
            }
        });


        //Переход на следующий фрагмент
        NextBtn = view.findViewById(R.id.GoToFinalViewButton);
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


                        progresscheck = view.findViewById(R.id.progressBaraddArticle);
                        progresscheck.setVisibility(View.VISIBLE);
                        NextBtn.setVisibility(View.GONE);
                        EditTextButtonsFrame.setClickable(false);
                        EditTextButtonsFrame.setEnabled(false);



                        //Fragment fragment = new CheckNewArticleFragment();

                        //Заголовок
                        // Bundle Bundle = new Bundle();
                        //Bundle.putString("Head", Header.getText().toString());
                        // fragment.setArguments(Bundle);


                        //Основной текст
                      //  Editable e = Main.getText();
                      //  SpannableStringBuilder e1 = (SpannableStringBuilder) Main.getText();
                     //   CharSequence MainTextChar = Main.getText();

                        // String s2 = Html.toHtml(e1);
                        // Bundle.putString("Main", s2);

                        //Текст с сохраненным форматированием
                        Spanned HeadSpanned = Html.fromHtml(Header.getText().toString());
                        Editable HeadViewText = (Editable) HeadSpanned;
                        String HeadString = Html.toHtml(HeadViewText);



                        //Фото превью
                        Bitmap PhotoPreviewBitMap = ((BitmapDrawable)Downloadpreviewimage.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PhotoPreviewBitMap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                        byte[] PhotoPreviewByteArray = baos.toByteArray();
                        StorageReference MainPhotoRef = storageRef.child(System.nanoTime()+"PreviewImage");

                        UploadTask uploadTaskPhoto = MainPhotoRef.putBytes(PhotoPreviewByteArray);
                        uploadTaskPhoto.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
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
                                String idArticle = mDataBase.getKey();

                                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                                String Simpledate = df.format(Calendar.getInstance().getTime());

                                Article newArticle = new Article(idArticle, Simpledate, HeadString, (Html.toHtml(Main.getText())),uploadArticlePhotoUri.toString());
                                mDataBase.push().setValue(newArticle);

                                getActivity().getSupportFragmentManager().popBackStackImmediate("AddArticle", POP_BACK_STACK_INCLUSIVE);
                                progresscheck.setVisibility(View.INVISIBLE);

                            }
                        });

                        // Байтовый массив данных основного текста
                        //byte[] Mainstream = toByteArray(MainTextChar);


                     //   File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "Article");
                       // try {
                        //    file.createNewFile();
                        //    if (file.exists()) {
                       //         //fo.write(Mainstream);
                       //     }
                      //  } catch (Exception exception) {
                      //  }

                        //Загрузка файла в базу
                      //  StorageReference MainTextArticleRef = storageRef.child(file.getName() + System.nanoTime());
                      //  UploadTask uploadTask = MainTextArticleRef.putBytes(Mainstream);
                      //  uploadTask.addOnFailureListener(new OnFailureListener() {
                       //     @Override
                      //      public void onFailure(@NonNull Exception e) {
                       //         Toast.makeText(getActivity(), "Ошибка загрузки публикации", Toast.LENGTH_SHORT).show();
                      //      }
                     //   }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     //       @Override
                     //       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      //          DoneDowndloadPhoto = true;
                     //       }
                    //    });


                 //       Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                   //         @Override
                     //       public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                     //           return MainTextArticleRef.getDownloadUrl();
                     //       }
                     //   }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                      //      @Override
                       //     public void onComplete(@NonNull Task<Uri> task) {
                        //        uploadArticleMainTextUri = task.getResult();
                        //        String idArticle = mDataBase.getKey();
//
                        //        DateFormat df = new SimpleDateFormat("d MMM yyyy");
                         //       String Simpledate = df.format(Calendar.getInstance().getTime());

                         //       Article newArticle = new Article(idArticle, Simpledate, HeadString, uploadArticleMainTextUri.toString(),uploadArticlePhotoUri.toString());
                         //       mDataBase.push().setValue(newArticle);

                          //      getActivity().getSupportFragmentManager().popBackStackImmediate("AddArticle", POP_BACK_STACK_INCLUSIVE);
                          //      progresscheck.setVisibility(View.INVISIBLE);
                           //     BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                           //     bottomNavigationView.setVisibility(View.VISIBLE);
                     //       }
                     //   });


                    }
                    //FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                    // ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                    //ft.addToBackStack("CheckNewArticle");
                    //ft.add(R.id.AddNewArticleFrame,fragment,"CheckNewArticle").commit();
                }
                else
                {
                    Toast.makeText(getActivity(), "Не полная публикация...", Toast.LENGTH_SHORT).show();
                }
            }

        });


        EditTextButtonsFrame = view.findViewById(R.id.EditTextButtonsFrame);




        HeaderButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(HeaderButton.isChecked())
                {
                    HeaderButton.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackgroung));
                }
                else
                {
                    HeaderButton.setBackground(null);
                }
            }
        });

        TextButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(TextButton.isChecked())
                {
                    TextButton.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackgroung));
                }
                else
                {
                    TextButton.setBackground(null);
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

        Header.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HeaderView.setVisibility(View.GONE);
                if(Header.getText().length()==0)
                {
                    HeaderView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        //Редактор основного текста

        Main.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CleanFocus.setClickable(true);
                CleanFocus.setTextColor(getResources().getColor(R.color.MainBlue));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainView.setVisibility(View.GONE);
                if(Main.getText().length()==0)
                {
                    MainView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        ExitBtn = view.findViewById(R.id.imagebackAddArticle);
        ExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("AddArticle",POP_BACK_STACK_INCLUSIVE);

            }
        });


        // указываем страницу загрузки
    }
}