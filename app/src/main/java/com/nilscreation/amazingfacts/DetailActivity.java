package com.nilscreation.amazingfacts;

import static android.os.Environment.DIRECTORY_PICTURES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    PhotoView photoView;
    String[] permission = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    TextToSpeech textToSpeech;
    TextView main_title, main_text, categoryName;
    ImageView btnBack, speakbtn, favourite, share, ttsSetting;
    String mPoster, mTitle, mText, mCategory;
    String title;
    MyDBHelper myDBHelper;

    Boolean fav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        photoView = findViewById(R.id.photoView);
        main_title = findViewById(R.id.main_title);
        main_text = findViewById(R.id.main_text);
        categoryName = findViewById(R.id.categoryName);
        btnBack = findViewById(R.id.btnBack);
        speakbtn = findViewById(R.id.speak);
        favourite = findViewById(R.id.favourite);
        share = findViewById(R.id.share);

        Bundle bundle = getIntent().getExtras();
        mPoster = bundle.getString("poster");
        mCategory = bundle.getString(("category"));
        mTitle = bundle.getString("title");
        mText = bundle.getString("text");

        Glide.with(this).load(mPoster).placeholder(R.drawable.app_logo).into(photoView);
        main_title.setText(mTitle);

//        main_text.setText(HtmlCompat.fromHtml(mText, 0));
        main_text.setText(mText);
        categoryName.setText(mCategory);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.forLanguageTag("ma"));
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        speakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = main_title.getText().toString() + main_text.getText().toString();

                if (textToSpeech.isSpeaking()) {
                    textToSpeech.stop();
                    ImageViewCompat.setImageTintList(speakbtn, ColorStateList.valueOf
                            (ContextCompat.getColor(DetailActivity.this, R.color.ic_color)));
                } else {
                    int speech = textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                    ImageViewCompat.setImageTintList(speakbtn, ColorStateList.valueOf
                            (ContextCompat.getColor(DetailActivity.this, R.color.primary)));
                }
            }
        });

        readData();

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fav) {
                    myDBHelper.deleteData(mTitle);
                    favourite.setImageResource(R.drawable.ic_like);
                    ImageViewCompat.setImageTintList(favourite, ColorStateList.valueOf
                            (ContextCompat.getColor(DetailActivity.this, R.color.ic_color)));
                    Toast.makeText(DetailActivity.this, "Removed from Favourite", Toast.LENGTH_SHORT).show();
                    fav = false;

                } else {
                    myDBHelper.deleteandAdd(mPoster, mCategory, mTitle, mText);
                    favourite.setImageResource(R.drawable.ic_favourite);
                    ImageViewCompat.setImageTintList(favourite, ColorStateList.valueOf
                            (ContextCompat.getColor(DetailActivity.this, R.color.red)));
                    Toast.makeText(DetailActivity.this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                    fav = true;

                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable bitmapDrawable = (BitmapDrawable) photoView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageandText(bitmap);

            }
        });

        //TTS settings
//        ttsSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent();
//                intent.setAction("com.android.settings.TTS_SETTINGS");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//            }
//        });

    }

    private void readData() {
        myDBHelper = new MyDBHelper(DetailActivity.this);
        ArrayList<FactsModel> facts = myDBHelper.readData();

        for (int i = 0; i < facts.size(); i++) {
            title = facts.get(i).title;

            if (mTitle.equals(title)) {
                favourite.setImageResource(R.drawable.ic_favourite);
                ImageViewCompat.setImageTintList(favourite, ColorStateList.valueOf
                        (ContextCompat.getColor(DetailActivity.this, R.color.red)));
                fav = true;
            } else {
                favourite.setImageResource(R.drawable.ic_like);
                ImageViewCompat.setImageTintList(favourite, ColorStateList.valueOf
                        (ContextCompat.getColor(DetailActivity.this, R.color.ic_color)));
                fav = false;
            }
        }
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, mTitle + "\n" + mText + "\n\n" +
                "For more intestring facts download the app now. " +
                "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        // setting type to image
        intent.setType("image/png");

        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    // Retrieving the url to share
    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "fact.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.nilscreation.amazingfacts", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80) {
            if (grantResults[0] == getPackageManager().PERMISSION_GRANTED) {

                BitmapDrawable bitmapDrawable = (BitmapDrawable) photoView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                try {
                    DownloadManager downloadManager = null;
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(mPoster);
                    DownloadManager.Request request = new DownloadManager.Request(uri);

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle("Emoji_wallpapers_" + System.currentTimeMillis() + ".jpg")
                            .setMimeType("image/jpeg")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(DIRECTORY_PICTURES, "Emoji_wallpapers_" + System.currentTimeMillis() + ".jpg");

                    downloadManager.enqueue(request);

                    Toast.makeText(getApplicationContext(), "Image Downloaded", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            Toast.makeText(DetailActivity.this, "Download cancel", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}