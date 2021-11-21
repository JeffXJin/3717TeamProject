package ca.bcit.androidProject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    private Button mButtonSave;
    private CircleImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private String fullName;
    private String email;
    private String phone;
    private String imageUrl;
    private User userProfile;

    private Button editProfile;
    private TextView fullNameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;


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


        uploadToFirebase();

        editProfile = findViewById(R.id.edit);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialog(fullName, email, phone);
            }
        });


    }

    /**
     * Helper function that will be called to upload images to Firebase
     */
    public void uploadToFirebase() {

        // Display user name, email and default blank phone number codes//
        fullNameTextView = (TextView) findViewById(R.id.user_name);
        emailTextView = (TextView) findViewById(R.id.user_email);
        phoneTextView = (TextView) findViewById(R.id.user_phone);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    fullName = userProfile.getName();
                    email = userProfile.getEmail();
                    phone = userProfile.getPhone();
                    imageUrl = userProfile.getImage();

                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                    phoneTextView.setText(phone);

                    // this should be code to retrieve image URL from firebase and
                    // assign it to imageView
                    Picasso.get().load(imageUrl).into(mImageView);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Cannot display the profile information", Toast.LENGTH_SHORT).show();
            }
        });

        // Image upload codes in on create method ///
        mButtonChooseImage = findViewById(R.id.edit_image);
        mButtonSave = findViewById(R.id.save_image);
        mImageView = findViewById(R.id.profile_image);
        mButtonSave.setVisibility(View.INVISIBLE);
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
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

    /**
     * This method uploads image
     */
    private void uploadFile() {

        if(mImageUri != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference sRef = mStorageRef.child(userID + "." + getFileExtension(mImageUri));

            sRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadUrl =  uri.toString();

                            User user = new User(fullName,email,phone, downloadUrl);
                            reference.child(userID).setValue(user);
                        }
                    });
                    Toast.makeText(getApplicationContext(), "File Upload", Toast.LENGTH_SHORT).show();
                    mButtonSave.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");

                }
            });
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

            mButtonSave.setVisibility(View.VISIBLE);
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


    /**
     * This method will update the firebase database
     * @param Username as string for user name
     * @param email as string for user email
     * @param phone as string for phone number
     */
    private void updateUser(String Username, String email, String phone) {
        DatabaseReference dbRef = reference.child(userID);

        User user = new User(Username,email,phone, imageUrl);

        Task setValueTask = dbRef.setValue(user);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(ProfileActivity.this,
                        "Your profile is Updated.",Toast.LENGTH_LONG).show();

                Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * This method will show a dialog to allow user to edit their email and name
     * @param userName user name
     * @param email user email
     * @param phone user phone number
     */
    private void showUpdateDialog(String userName, String email, String phone) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUserName = dialogView.findViewById(R.id.editTextUserName);
        editTextUserName.setText(userName);

        final EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        editTextEmail.setText(email);

        final EditText editTextPhoneNumber = dialogView.findViewById(R.id.editPhoneNumber);
        editTextPhoneNumber.setText(phone);


        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        dialogBuilder.setTitle("Change profile information");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    editTextUserName.setError("User Name is required");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("email is required");
                    return;
                }

                updateUser(userName, email, phone);

                alertDialog.dismiss();
            }
        });


    }
}