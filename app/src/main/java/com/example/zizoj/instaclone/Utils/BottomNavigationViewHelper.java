package com.example.zizoj.instaclone.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.zizoj.instaclone.Home.MainActivity;
import com.example.zizoj.instaclone.Likes.LikeActivity;
import com.example.zizoj.instaclone.Profile.ProfileActivity;
import com.example.zizoj.instaclone.R;
import com.example.zizoj.instaclone.Search.SearchActivity;
import com.example.zizoj.instaclone.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);

    }

    public static void enableNavigation(final Context context , final Activity callingActivity , BottomNavigationViewEx navigationViewEx){

        navigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_search:
                        Intent intent1 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_circl:
                        Intent intent2 = new Intent(context, ShareActivity.class);
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_alert:
                        Intent intent3 = new Intent(context, LikeActivity.class);
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_android:
                        Intent intent4 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent4 );
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                }

                return false;
            }
        });
    }
}
