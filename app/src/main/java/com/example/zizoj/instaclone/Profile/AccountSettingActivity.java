package com.example.zizoj.instaclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.zizoj.instaclone.R;
import com.example.zizoj.instaclone.Utils.BottomNavigationViewHelper;
import com.example.zizoj.instaclone.Utils.FirebaseMethods;
import com.example.zizoj.instaclone.Utils.SectionsStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class AccountSettingActivity extends AppCompatActivity{
    private static final String TAG = "AccountSettingActivity";
    Context mcontext;

    public SectionsStatePagerAdapter pagerAdapter;

    private ViewPager mviewPager ;
    private RelativeLayout mRelativeLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Log.d(TAG, "onCreate: starting.");

        mcontext = AccountSettingActivity.this;

        mviewPager = (ViewPager)findViewById(R.id.viewpager_container);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.relLayout1);

        setupBottomNavigationView();

        setupSettingsList();

        setupFragment();

        getIncomingIntent();


        //setup the backarrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
                finish();
            }
        });
    }

    private void getIncomingIntent(){

        Intent intent = getIntent();

        //if there is an imageUrl attached as an extra, then it was chosen from the gallery/photo fragment
        if(intent.hasExtra(getString(R.string.selectedimage))
                || intent.hasExtra(getString(R.string.selected_bitmap))){

            Log.d(TAG, "getIncomingIntent: New incoming imgUrl");


            if(intent.getStringExtra(getString(R.string.return_to_fragment)).equals(getString(R.string.edit_profile_fragment))){

                if(intent.hasExtra(getString(R.string.selectedimage))){
                    //set the new profile picture
                    FirebaseMethods firebaseMethods = new FirebaseMethods(AccountSettingActivity.this);
                    firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo), null, 0,
                            intent.getStringExtra(getString(R.string.selectedimage)), null);
                }
                else if(intent.hasExtra(getString(R.string.selected_bitmap))){
                    //set the new profile picture
                    FirebaseMethods firebaseMethods = new FirebaseMethods(AccountSettingActivity.this);
                    firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo), null, 0,
                            null,(Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap)));
                }

            }

        }
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "getIncomingIntent: received incoming intent from " + getString(R.string.profile_activity));
            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }


    }

    private void setupFragment(){

        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new EditProfileFragment() , getString(R.string.edit_profile_fragment)); // fragment 0
        pagerAdapter.addFragments(new SignOutFragment() , getString(R.string.sign_out_fragment)); // fragment 1
    }

    public void setViewPager(int fragmentnumber){
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setViewPager: start fragment " + fragmentnumber);

        mviewPager.setAdapter(pagerAdapter);
        mviewPager.setCurrentItem(fragmentnumber);
    }

    private void setupBottomNavigationView(){
        Log.d(TAG , "setupBottomNavigationView : setting elview");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomnavigation);

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);

        BottomNavigationViewHelper.enableNavigation(AccountSettingActivity.this, this,bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

    }

    private void setupSettingsList(){
        Log.d(TAG, "setupSettingsList: initializ settinglist");

        ListView listView = (ListView) findViewById(R.id.lvAccountSettings);

        List<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile_fragment)); // fragment 0
        options.add(getString(R.string.sign_out_fragment)); // fragment 1

        ArrayAdapter adapter = new ArrayAdapter(mcontext , android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setViewPager(i);

            }
        });

    }



}
