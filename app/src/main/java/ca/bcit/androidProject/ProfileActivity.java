package ca.bcit.androidProject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {


    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private String userID;

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView mButtonChooseImage;
    private Button mButtonEdit;

    private CircleImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;


    private String fullname;
    private String email;
    private String phone;
    private String imageUrl;

    private User userProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // sets the title to the toolbar
        getSupportActionBar().setTitle("Profile");



        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();

        mStorageRef = FirebaseStorage.getInstance().getReference("user");



        // Display user name, email and default blank phone number codes//
        TextView fullNameTextView = (TextView) findViewById(R.id.user_name);
        TextView emailTextView = (TextView) findViewById(R.id.user_email);
        TextView phoneTextView = (TextView) findViewById(R.id.user_phone);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    fullname = userProfile.getName();
                    email = userProfile.getEmail();
                    phone = userProfile.getPhone();
                    imageUrl = userProfile.getImage();

                    fullNameTextView.setText(fullname);
                    emailTextView.setText(email);
                    phoneTextView.setText(phone);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Cannot display the profile information", Toast.LENGTH_SHORT).show();
            }
        });

        // Image upload codes in on create method ///
        mButtonChooseImage = findViewById(R.id.edit_image);
        mButtonEdit = findViewById(R.id.save_image);
        mImageView = findViewById(R.id.profile_image);
        mProgressBar = findViewById(R.id.progress_bar);
        mButtonEdit.setVisibility(View.INVISIBLE);
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(ProfileActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }

            }
        });
    }

    /**
     * TO get teh extension file type of the image.
     * @param uri Uri
     * @return string extension file type
     */
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(ProfileActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                            imageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            userProfile = new User(fullname,email,phone, imageUrl);

                           String uploadId = reference.push().getKey();
                           reference.child(uploadId).setValue(user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int)progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No File selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that will allow device to open image folder directly
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * This will display the image that user choose into the imageview
     * @param requestCode int
     * @param resultCode int
     * @param data int
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
               && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);

            mButtonEdit.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This will allow user to logout to the app.
     * @param menu menu
     */
    public void onLogoutClick(MenuItem menu) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        Intent intent = new Intent(this, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    /**
     * This will display back button and allow user to go back to previous activity.
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}