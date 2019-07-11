package com.example.myinstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myinstagram.EndlessRecyclerViewScrollListener;
import com.example.myinstagram.ProfileAdapter;
import com.example.myinstagram.R;
import com.example.myinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;



public class ProfileFragment extends Fragment {


    private ParseUser currentUser;

    private RecyclerView rvProfileViews;

    private ProfileAdapter profileAdapter;
    private ArrayList<Post> mPosts;


    public CircleImageView ivProfilePic;
    public TextView tvProfileName;
    public Button btnUpate;
    public TextView tvFirst;
    public TextView tvLast;
    public Button btnLogOut;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;

    public MenuItem miActionProgressItem;

    private EndlessRecyclerViewScrollListener scrollListener;

    public ProfileFragment(ParseUser currentUser) {
        this.currentUser = currentUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);


        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvProfileName = view.findViewById(R.id.tvProfileName);
        btnUpate = view.findViewById(R.id.btnUpdateProfile);
        tvFirst = view.findViewById(R.id.tvFirst);
        tvLast = view.findViewById(R.id.tvLast);
        btnLogOut = view.findViewById(R.id.btnLogOut);



        tvFirst.setText(currentUser.getString("FirstName"));
        tvLast.setText(" "+ currentUser.getString("LastName"));


        tvProfileName.setText(currentUser.getUsername());

        rvProfileViews = view.findViewById(R.id.rvProfileViews);
        getProfilePic();


        mPosts = new ArrayList<>();

        profileAdapter = new ProfileAdapter(getContext(),mPosts);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        rvProfileViews.setAdapter(profileAdapter);
        rvProfileViews.setLayoutManager(gridLayoutManager);

        btnUpate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });


        if (this.currentUser == ParseUser.getCurrentUser()) {
            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser.logOut();

                }
            });
        }else{
            btnLogOut.setVisibility(View.INVISIBLE);
        }




//        showProgressBar();
        queryPosts();
    }

    private void launchCamera(){
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ComposeFragment");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("ComposeFragment", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview

                ivProfilePic.setImageBitmap(rotateBitmapOrientation(photoFile.getAbsolutePath()));

                // Compressing the bitmap

                showProgressBar();
                saveProfile(currentUser, photoFile);


                // Do the intent





            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void saveProfile(ParseUser parseUser, File photoFile){

        parseUser.put("profilePic",new ParseFile(photoFile));


        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Toast.makeText(getContext(), "Saving error", Toast.LENGTH_SHORT).show();
                    Log.d("descriptionActivity","Error while saving");
                    e.printStackTrace();
                    hideProgressBar();
                    return;
                }else{
                    Toast.makeText(getContext(), "Saving success", Toast.LENGTH_SHORT).show();
                    Log.d("descriptionActivity", "saving success");
                    hideProgressBar();

                }
            }
        });

    }

    private void getProfilePic(){

        ParseFile file = currentUser.getParseFile("profilePic");
        if(file != null) {
            Glide.with(getContext()).load(file.getUrl()).into(ivProfilePic);


        }else{
            Glide.with(getContext()).load(getResources().getDrawable(R.drawable.instagram_user_outline_24)).into(ivProfilePic);
        }


    }
    private void queryPosts(){
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.whereEqualTo(Post.KEY_USER, currentUser);
        postQuery.addDescendingOrder("createdAt");

        if (mPosts.size() > 0){
            postQuery.whereLessThan("createdAt", mPosts.get(mPosts.size() - 1).getCreatedAt());
        }



        postQuery.findInBackground(new FindCallback<Post>(){
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!=null){
                    Log.e("HomeFragment", "Error with query");
                    e.printStackTrace();
                    return;

                }else{


                    for(int i = 0; i < posts.size();i++) {

                        Post post = posts.get(i);
                        mPosts.add(post);
                        profileAdapter.notifyItemInserted(mPosts.size() - 1);


                        Log.d("HomeFragment", "Posts: "+ post.getDescription() + ",username: " + post.getUser().getUsername());

                    }

                }


            }
        });

    }


    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }





    public void showProgressBar() {
        // Show progress item

        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main_menu, menu);



        super.onCreateOptionsMenu(menu, inflater);


    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
//        miActionProgressItem.setVisible(true);
        super.onPrepareOptionsMenu(menu);


    }








}
