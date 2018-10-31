package com.example.zizoj.instaclone.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.zizoj.instaclone.Profile.AccountSettingActivity;
import com.example.zizoj.instaclone.R;
import com.example.zizoj.instaclone.Utils.FilePaht;
import com.example.zizoj.instaclone.Utils.FileSearch;
import com.example.zizoj.instaclone.Utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    //constants
    private static final int NUM_GRID_COLUMNS = 3 ;

    //widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    //var

    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage ;


    private static final String TAG = "GalleryFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        Log.d(TAG, "onCreateView: started.");

        galleryImage = (ImageView) view.findViewById(R.id.galleryImageView);
        gridView = (GridView) view.findViewById(R.id.gridView);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        directories = new ArrayList<>();

        ImageView shareClose = (ImageView) view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                getActivity().finish();
            }
        });

        TextView nextScreen = (TextView) view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");

                if(isRootTask()){
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selectedimage), mSelectedImage);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                    intent.putExtra(getString(R.string.selectedimage), mSelectedImage);
                    intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        init();

        return view;
    }


    private boolean isRootTask(){
        if(((ShareActivity)getActivity()).getTask() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    private void  init(){

        FilePaht filePaht = new FilePaht();

        if (FileSearch.getDirectoryPaths(filePaht.PICTURE) != null){
            directories = FileSearch.getDirectoryPaths(filePaht.PICTURE);
        }

        ArrayList<String> directoryNames = new ArrayList<>();
        for(int i = 0; i < directories.size(); i++){
            int index = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(index).replace("/" , " ");
            directoryNames.add(string);
        }

        directories.add(filePaht.CAMERA);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setupGridView(directories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void setupGridView(String selectedDirectory){

        final ArrayList<String> imgURLS = FileSearch.getFilePaths(selectedDirectory);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;

        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLS);
        gridView.setAdapter(adapter);


        setImage(imgURLS.get(0), galleryImage, mAppend);
        mSelectedImage = imgURLS.get(0);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLS.get(position));
                setImage(imgURLS.get(position), galleryImage, mAppend);
                mSelectedImage = imgURLS.get(position);
            }
        });
    }


    private void setImage(String imgURL, ImageView image, String append){
        Log.d(TAG, "setImage: setting image");
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
