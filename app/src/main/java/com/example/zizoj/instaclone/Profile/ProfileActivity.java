package com.example.zizoj.instaclone.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.zizoj.instaclone.R;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG , "onCreat: Starting.");

//        setupBottomNavigationView();
//        setupToolbar();
//        setupActivityWidgets();
//        setProfileImage();
//
//        tempGridSetup();

        init();

    }

    private void init(){
        Log.d(TAG, "init: inflating " + getString(R.string.profile_fragment));

        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerprofile, fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }

//    private void tempGridSetup(){
//        ArrayList<String> imgURLs = new ArrayList<>();
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
//        imgURLs.add("http://i.imgur.com/EwZRpvQ.jpg");
//        imgURLs.add("http://i.imgur.com/JTb2pXP.jpg");
//        imgURLs.add("https://i.redd.it/59kjlxxf720z.jpg");
//        imgURLs.add("https://i.redd.it/pwduhknig00z.jpg");
//        imgURLs.add("https://i.redd.it/clusqsm4oxzy.jpg");
//        imgURLs.add("https://i.redd.it/svqvn7xs420z.jpg");
//        imgURLs.add("http://i.imgur.com/j4AfH6P.jpg");
//        imgURLs.add("https://i.redd.it/89cjkojkl10z.jpg");
//        imgURLs.add("https://i.redd.it/aw7pv8jq4zzy.jpg");
//
//        setupImageGrid(imgURLs);
//    }
//
//    private void setupImageGrid(ArrayList<String> imgURLs){
//        GridView gridView = (GridView) findViewById(R.id.gridView);
//
//        int gridwidth = getResources().getDisplayMetrics().widthPixels;
//        int imagewidth = gridwidth/3 ;
//        gridView.setColumnWidth(imagewidth);
//
//        GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this, R.layout.layout_grid_imageview, "", imgURLs);
//        gridView.setAdapter(adapter);
//    }
//
//
//    private void setProfileImage(){
//        Log.d(TAG, "setProfileImage: setting profile photo.");
//        String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
//        UniversalImageLoader.setImage(imgURL, profilePhoto, null, "https://");
//    }
//
//    private void setupActivityWidgets(){
//        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
//    }
//
//    private void setupToolbar(){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.profiletoolbar);
//        setSupportActionBar(toolbar);
//
//        ImageView profileMenu = (ImageView) findViewById(R.id.profilemenu);
//        profileMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: go to acc setting");
//
//                startActivity(new Intent(ProfileActivity.this,AccountSettingActivity.class));
//            }
//        });
//    }

    /*
         BottomNavigationView setup
      */
//    private void setupBottomNavigationView(){
//        Log.d(TAG , "setupBottomNavigationView : setting elview");
//
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomnavigation);
//
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//
//        BottomNavigationViewHelper.enableNavigation(ProfileActivity.this,bottomNavigationViewEx);
//
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//
//    }


}
