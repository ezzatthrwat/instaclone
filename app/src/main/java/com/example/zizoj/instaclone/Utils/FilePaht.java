package com.example.zizoj.instaclone.Utils;

import android.os.Environment;

public class FilePaht {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURE = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

    public String FIREBASE_IMAGE_STORAGE = "photos/users/";
}
