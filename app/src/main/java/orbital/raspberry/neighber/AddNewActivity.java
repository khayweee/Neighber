package orbital.raspberry.neighber;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddNewActivity extends AppCompatActivity {

    private TextView browse, records, addnew, chat, profile;
    private TextView worktools, kitchen, cleaning, more;
    private Uri filePath;
    private ImageView photo;
    private Button submitBtn, lendtype, borrowtype, uploadphoto;
    private EditText itemnameTxt, postdescTxt;
    private String userName;
    //1 for borrow, 2 for lending
    private int posttype;
    //1 for worktool, 2 for kitchen, 3 for furniture, 4 for others
    private int categorytype;
    public static final int GRID_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1888;
    private String username;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://neighber-b5ee0.appspot.com");
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        //////////////Navigations/////////////
        records = (TextView) findViewById(R.id.action_records);
        addnew = (TextView) findViewById(R.id.action_addnew);
        chat = (TextView) findViewById(R.id.action_chat);
        profile = (TextView) findViewById(R.id.action_profile);
        browse = (TextView) findViewById(R.id.action_browse);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewActivity.this, MainActivity.class));
            }
        });

        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewActivity.this, BorrowerRecordsActivity.class));
            }
        });

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewActivity.this, ChatListActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewActivity.this, ProfileActivity.class));
            }
        });

        //////////////////////End Navigation////////////////////////////

        submitBtn = (Button)findViewById(R.id.submitRequest);
        itemnameTxt = (EditText)findViewById(R.id.itemname);
        postdescTxt = (EditText)findViewById(R.id.postdesc);
        lendtype = (Button)findViewById(R.id.lendtype);
        borrowtype = (Button)findViewById(R.id.borrowtype);

        worktools = (TextView) findViewById(R.id.worktools);
        kitchen = (TextView) findViewById(R.id.kitchen);
        cleaning = (TextView) findViewById(R.id.cleaning);
        more = (TextView) findViewById(R.id.more);

        photo = (ImageView) findViewById(R.id.imgView);
        uploadphoto = (Button)findViewById(R.id.takephoto);

        posttype = 1;

        categorytype = 0;

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        final FirebaseUser currentFirebaseUser = auth.getCurrentUser() ;
        final String userid = currentFirebaseUser.getUid();

        final DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference("users");
        uDatabase.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                username = user.getDisplayname();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddNewActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });


        worktools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorytype = 1;
                worktools.setBackground(ContextCompat.getDrawable(AddNewActivity.this,R.drawable.navborder));

                kitchen.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                kitchen.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                cleaning.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                cleaning.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                more.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                more.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));

            }
        });

        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorytype = 2;
                kitchen.setBackground(ContextCompat.getDrawable(AddNewActivity.this,R.drawable.navborder));

                worktools.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                worktools.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                cleaning.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                cleaning.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                more.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                more.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));

            }
        });

        cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorytype = 3;
                cleaning.setBackground(ContextCompat.getDrawable(AddNewActivity.this,R.drawable.navborder));

                worktools.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                worktools.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                kitchen.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                kitchen.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                more.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                more.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));

            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                categorytype = 4;*/

                worktools.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                worktools.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                kitchen.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                kitchen.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                cleaning.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                cleaning.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));

                Intent intent = new Intent(AddNewActivity.this, GridActivity.class);
                startActivityForResult(intent,GRID_REQUEST);


            }
        });

        borrowtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postdescTxt.setHint("Provide description for your request. Eg. Duration of borrow?");
                submitBtn.setText("Post Request");
                posttype = 1;
                lendtype.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                lendtype.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                borrowtype.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                borrowtype.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
            }
        });

        lendtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postdescTxt.setHint("Provide description for your offer. Eg. Size of item?");
                submitBtn.setText("Post Offer");
                posttype = 2;
                lendtype.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));
                lendtype.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                borrowtype.setBackgroundColor(ContextCompat.getColor(AddNewActivity.this,R.color.fadeorange));
                borrowtype.setTextColor(ContextCompat.getColor(AddNewActivity.this,R.color.colorPrimary));

            }
        });
        /*
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
               if(position == 0){
                   postdescTxt.setHint("Provide description for your request. Eg. Duration of borrow?");
                   submitBtn.setText("Post Request");
               }else{
                   postdescTxt.setHint("Provide description for your offer. Eg. Size of item?");
                   submitBtn.setText("Post Offer");
               }
            }
        }); */


        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("posts");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pd = new ProgressDialog(AddNewActivity.this);
                pd.setMessage("Posting...");
                pd.show();

                // get unique post id from firebase
                final String postid = mDatabase.push().getKey();

                //Get the post type based on which item is selected in the spinner
            //    int postType = spinner.getSelectedIndex() + 1;


                //UPLOAD IMAGE
                if (filePath != null) {

                    StorageReference childRef = storageRef.child(postid + ".jpg");

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                    byte[] idata = outputStream.toByteArray();

                    //uploading the image
                    UploadTask uploadTask = childRef.putBytes(idata);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            @SuppressWarnings("VisibleForTests") String dlurl = taskSnapshot.getDownloadUrl().toString();

                            //Create newpost object
                            Post newpost = new Post(postid, userid, itemnameTxt.getText().toString().trim(),
                                    username, postdescTxt.getText().toString().trim(), posttype, categorytype);

                            //Add post to database
                            mDatabase.child(postid).setValue(newpost);

                            //Add timestamp to the post
                            mDatabase.child(postid).child("timestamp").setValue(ServerValue.TIMESTAMP);
                            mDatabase.child(postid).child("imguri").setValue(dlurl);


                            Toast.makeText(AddNewActivity.this, "Post Submitted! You may edit/delete the post in My Profiles tab", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddNewActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddNewActivity.this, "Fail to upload image, please try again." + e, Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{

                    //Create newpost object
                    Post newpost = new Post(postid, userid, itemnameTxt.getText().toString().trim(),
                            username, postdescTxt.getText().toString().trim(), posttype, categorytype);

                    //Add post to database
                    mDatabase.child(postid).setValue(newpost);

                    //Add timestamp to the post
                    mDatabase.child(postid).child("timestamp").setValue(ServerValue.TIMESTAMP);
                    mDatabase.child(postid).child("imguri").setValue("");

                     Toast.makeText(AddNewActivity.this, "Post Submitted! You may edit/delete the post in My Profiles tab", Toast.LENGTH_LONG).show();
                     pd.dismiss();
                     startActivity(new Intent(AddNewActivity.this, MainActivity.class));
                     finish();

                }


            }
        });


        uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GRID_REQUEST) {
            if(resultCode == RESULT_OK) {
                int num = data.getIntExtra("categorynum", 0);

                more.setBackground(ContextCompat.getDrawable(AddNewActivity.this,R.drawable.navborder));


                //Toast.makeText(AddNewActivity.this, "Category: " + num, Toast.LENGTH_LONG).show();

                switch(num) {
                    case 4:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.office, 0, 0);
                        more.setText("Office");
                        categorytype = 4;
                        break;
                    case 5:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.party, 0, 0);
                        more.setText("Party");
                        categorytype = 5;
                        break;
                    case 6:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.furniture, 0, 0);
                        more.setText("Furniture");
                        categorytype = 6;
                        break;
                    case 7:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.shirtf, 0, 0);
                        more.setText("Women's");
                        categorytype = 7;
                        break;
                    case 8:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.shirtm, 0, 0);
                        more.setText("Men's");
                        categorytype = 8;
                        break;
                    case 9:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.sports, 0, 0);
                        more.setText("Sports");
                        categorytype = 9;
                        break;
                    case 10:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.electrical, 0, 0);
                        more.setText("Electronics");
                        categorytype = 10;
                        break;
                    case 11:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.food, 0, 0);
                        more.setText("Food");
                        categorytype = 11;
                        break;
                    case 0:
                        more.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.others, 0, 0);
                        more.setText("Others");
                        categorytype = 0;
                        break;

                }

            }
            if (resultCode == RESULT_CANCELED) {
                //showError("The selection was cancelled");
            }
        }else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
           // Bitmap img = (Bitmap) data.getExtras().get("data");

            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                photo.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

           // photo.setImageBitmap(img);
        }



    }

    //////////////////Top Right Menu//////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // to do logout action
                auth.signOut();
                Intent i = new Intent(AddNewActivity.this, LoginpageActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                break;
            case R.id.action_settings:
                startActivity(new Intent(AddNewActivity.this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //////////////////End top menu////////////////////////
}
