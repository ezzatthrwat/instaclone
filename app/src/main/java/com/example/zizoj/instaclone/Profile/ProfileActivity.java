package com.example.zizoj.instaclone.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.zizoj.instaclone.Models.Photo;
import com.example.zizoj.instaclone.R;
import com.example.zizoj.instaclone.Utils.ViewCommentsFragment;
import com.example.zizoj.instaclone.Utils.ViewPostFragment;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnGridImageSelectedListener ,
ViewPostFragment.OnCommentThreadSelectedListener{

    private static final String TAG = "ProfileActivity";

    @Override
    public void onGridImageSelected(Photo photo, int ActivityNumber) {

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        args.putInt(getString(R.string.activity_number), ActivityNumber);

        fragment.setArguments(args);
        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerprofile, fragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();

    }

    @Override
    public void onCommentThreadSelectedListener(Photo photo) {

        ViewCommentsFragment fragment = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);


        fragment.setArguments(args);
        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerprofile, fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();
    }


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



}
