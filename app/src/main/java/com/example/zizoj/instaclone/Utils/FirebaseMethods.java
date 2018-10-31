package com.example.zizoj.instaclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.zizoj.instaclone.Home.MainActivity;
import com.example.zizoj.instaclone.Models.Photo;
import com.example.zizoj.instaclone.Models.UserSettings;
import com.example.zizoj.instaclone.Models.Users;
import com.example.zizoj.instaclone.Models.UsersAccountSettings;
import com.example.zizoj.instaclone.Profile.AccountSettingActivity;
import com.example.zizoj.instaclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    private StorageReference mStorageReference;

    //vars
    private Context mContext;
    private double mPhotoUploadProgress = 0;


    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mStorageReference = FirebaseStorage.getInstance().getReference();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }



    }

    public void uploadNewPhoto(String photoType, final String caption, final int count,final  String imgUrl ,Bitmap bm){
        Log.d(TAG, "uploadNewPhoto: attempting to uplaod new photo.");

        final FilePaht filePaths = new FilePaht();

        //case1) new photo
        if(photoType.equals(mContext.getString(R.string.new_photo))){
            Log.d(TAG, "uploadNewPhoto: uploading NEW photo.");

            final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count + 1));

            //convert image url to bitmap
            if(bm == null){
                bm = ImageManager.getBitmap(imgUrl);
            }
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    mStorageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count + 1)).
                            getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //add the new photo to 'photos' node and 'user_photos' node

                            addPhotoToDatabase(caption, uri.toString());

                            //navigate to the main feed so the user can see their photo
                            Intent intent = new Intent(mContext, MainActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();
                        }
                });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Photo upload failed.");
                    Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    if(progress - 15 > mPhotoUploadProgress){
                        Toast.makeText(mContext, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
                }
            });


        }

        //case new profile photo
        else if(photoType.equals(mContext.getString(R.string.profile_photo))){
            Log.d(TAG, "uploadNewPhoto: uploading new PROFILE photo");



            final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/profile_photo");

            //convert image url to bitmap
            if(bm == null){
                bm = ImageManager.getBitmap(imgUrl);
            }
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ((AccountSettingActivity)mContext).setViewPager(
                            ((AccountSettingActivity)mContext).pagerAdapter
                                    .getFragmentNumber(mContext.getString(R.string.edit_profile_fragment))
                    );

                    mStorageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/profile_photo").
                            getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //insert into 'user_account_settings' node
                            setProfilePhoto(uri.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Photo upload failed.");
                    Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    if(progress - 15 > mPhotoUploadProgress){
                        Toast.makeText(mContext, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
                }
            });

        }
    }

    private void setProfilePhoto(String url){
        Log.d(TAG, "setProfilePhoto: setting new profile image: " + url);
        mDatabaseReference.child(mContext.getString(R.string.dbname_users_account_setting))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mContext.getString(R.string.profile_photo))
                .setValue(url);
    }


    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }

    private void addPhotoToDatabase(String caption, String url){
        Log.d(TAG, "addPhotoToDatabase: adding photo to database.");

        String tags = StringManipulation.getTags(caption);
        String newPhotoKey = mDatabaseReference.child(mContext.getString(R.string.dbname_photos)).push().getKey();
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimestamp());
        photo.setImage_path(url);
        photo.setTags(tags);
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setPhoto_id(newPhotoKey);

        //insert into database
        mDatabaseReference.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child(newPhotoKey).setValue(photo);

        mDatabaseReference.child(mContext.getString(R.string.dbname_photos)).child(newPhotoKey).setValue(photo);
    }


    /**
     * Register a new email and password to Firebase Authentication
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            sendEmailVerifiction();

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            userID = mAuth.getCurrentUser().getUid();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    public void sendEmailVerifiction(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){

            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                    }else {

                        Toast.makeText(mContext, "Couldn't send email Verification", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void updateUsername(String username){
        Log.d(TAG, "updateUsername: upadting username to: " + username);

        mDatabaseReference.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        mDatabaseReference.child(mContext.getString(R.string.dbname_users_account_setting))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }


    /**
     * update the email in the 'user's' node
     * @param email
     */
    public void updateEmail(String email){
        Log.d(TAG, "updateEmail: upadting email to: " + email);

        mDatabaseReference.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);

    }

    /**
     * Update 'user_account_settings' node for the current user
     * @param displayName
     * @param website
     * @param description
     * @param phoneNumber
     */
    public void updateUserAccountSettings(String displayName, String website, String description, long phoneNumber){
        Log.d(TAG, "updateUserAccountSettings: updating user account settings.");
        if(displayName != null){
            mDatabaseReference.child(mContext.getString(R.string.dbname_users_account_setting))
                    .child(userID)
                    .child(mContext.getString(R.string.field_display_name))
                    .setValue(displayName);
        }
        if(website != null) {
            mDatabaseReference.child(mContext.getString(R.string.dbname_users_account_setting))
                    .child(userID)
                    .child(mContext.getString(R.string.field_website))
                    .setValue(website);
        }
        if(description != null) {
            mDatabaseReference.child(mContext.getString(R.string.dbname_users_account_setting))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }
        if(phoneNumber != 0) {
            mDatabaseReference.child(mContext.getString(R.string.dbname_users_account_setting))
                    .child(userID)
                    .child(mContext.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);
        }
    }




    public int getImageCount(DataSnapshot dataSnapshot){
        int count = 0;
        for(DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()){
            count++;
        }
        return count;
    }

//    public boolean checkIfUsernameExists(String username , DataSnapshot dataSnapshot){
//        Log.d(TAG,"checkIfUsernameExists : checking if " + username + "already exists.");
//
//        Users users = new Users();
//
//        for (DataSnapshot ds : dataSnapshot.child(userID).getChildren()){
//            Log.d(TAG,"checkIfUsernameExists : datasnapshot: " + ds);
//
//            users.setUsername(ds.getValue(Users.class).getUsername());
//            Log.d(TAG,"checkIfUsernameExists : datasnapshot: " + users.getUsername());
//
//
//            if (StringManipulation.expandUsername(users.getUsername()).equals(username)){
//                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + users.getUsername());
//                return true;
//            }
//
//        }

//        return true;
//    }

    public void addNewUser(String email, String username, String description, String website, String profile_photo){

        Users users = new Users( userID,  1,  email,  StringManipulation.condenseUsername(username) );

        mDatabaseReference.child(mContext.getString(R.string.dbname_users)).child(userID).setValue(users);

        UsersAccountSettings usersAccountSettings = new UsersAccountSettings(
                description,
                username,
                0,
                0,
                0,
                "",
                StringManipulation.condenseUsername(username),
                website);

        mDatabaseReference.child(mContext.getString(R.string.dbname_users_account_setting))
                .child(userID)
                .setValue(usersAccountSettings);

    }

    /**
     * Retrieves the account settings for teh user currently logged in
     * Database: user_acount_Settings node
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserAccountSettings(DataSnapshot dataSnapshot){
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");


        UsersAccountSettings settings  = new UsersAccountSettings();
        Users user = new Users();

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            // user_account_settings node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_users_account_setting))){
                Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);

                try{

                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UsersAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UsersAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UsersAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UsersAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UsersAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UsersAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UsersAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UsersAccountSettings.class)
                                    .getFollowers()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                }catch (NullPointerException e){
                    Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage() );
                }

            }

            // users node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(Users.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(Users.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(Users.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(Users.class)
                                .getUser_id()
                );

                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }
        }
        return new UserSettings(user, settings);

    }





}