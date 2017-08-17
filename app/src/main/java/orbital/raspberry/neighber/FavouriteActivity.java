package orbital.raspberry.neighber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FavouriteActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView browse, records, addnew, chat, profile;
    private List<Post> posts;
    private List<String> favids;
    private ListView listViewRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        getSupportActionBar().setTitle("Favourites");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        posts = new ArrayList<>();
        favids = new ArrayList<>();

        listViewRequests = (ListView) findViewById(R.id.listRequest);

        //////////////Navigations/////////////
        records = (TextView) findViewById(R.id.action_records);
        addnew = (TextView) findViewById(R.id.action_addnew);
        chat = (TextView) findViewById(R.id.action_chat);
        profile = (TextView) findViewById(R.id.action_profile);
        browse = (TextView) findViewById(R.id.action_browse);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, MainActivity.class));
            }
        });

        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, BorrowerRecordsActivity.class));
            }
        });

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, AddNewActivity.class));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, ChatListActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, ProfileActivity.class));
            }
        });

        //////////////////////End Navigation////////////////////////////

        final String currentuserid = auth.getCurrentUser().getUid().toString();

        final DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference("favourite").child(currentuserid);

        //attaching value event listener
        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous list
                favids.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Favourite fav = postSnapshot.getValue(Favourite.class);

                        //adding to the fav list
                        favids.add(fav.getPostid());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("posts");

        //attaching value event listener
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous list
                posts.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Post post = postSnapshot.getValue(Post.class);

                    //If post type is offer aka 2
                    if(post.getPosttype() == 2) {

                        for(int j=0; j<favids.size();j++){

                            //If post is favourited by this user
                            if(post.getPostid().equals(favids.get(j))){
                                String datetime = getDate(post.getTimestamp());

                                post.setDatetime(datetime);

                                //adding to the list
                                posts.add(post);

                            }

                        }

                    }
                }

                //creating adapter
                FavouriteList reqAdapter = new FavouriteList(FavouriteActivity.this, posts);
                //attaching adapter to the listview
                listViewRequests.setAdapter(reqAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = posts.get(position);
                //Toast.makeText(MainActivity.this, "Item: " + post.getItemname() + " selected.", Toast.LENGTH_SHORT).show();
                String requesterid = post.getUserid();
                String postid = post.getPostid();

                if(post.getStatus() == 1) {
                    Intent i = new Intent(FavouriteActivity.this, PostActivity.class);
                    i.putExtra("ruserid", requesterid);
                    i.putExtra("rpostid", postid);
                    startActivity(i);
                }else{
                    Toast.makeText(FavouriteActivity.this, "Item is not available", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a", cal).toString();
        return date;
    }

    ///////////////////Top Right Menu//////////////////////
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
                Intent i = new Intent(FavouriteActivity.this, LoginpageActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                break;
            case R.id.action_settings:
                startActivity(new Intent(FavouriteActivity.this, SettingsActivity.class));
                break;
            case R.id.action_feedback:
                startActivity(new Intent(FavouriteActivity.this, FeedbackActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //////////////////End top menu////////////////////////


}