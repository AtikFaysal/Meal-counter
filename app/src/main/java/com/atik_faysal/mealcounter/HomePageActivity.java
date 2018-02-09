package com.atik_faysal.mealcounter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atik_faysal.backend.GetSearchableGroup;
import com.atik_faysal.backend.InfoBackgroundTask;
import com.atik_faysal.backend.InfoBackgroundTask.OnAsyncTaskInterface;
import com.atik_faysal.backend.SharedPreferenceData;
import com.atik_faysal.model.SearchableModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import interfaces.SearchInterfaces;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

        //component variable
        private Toolbar toolbar;
        private DrawerLayout drawer;
        private ActionBarDrawerToggle toggle;
        private View view;
        private TextView textView;

        //component array object
        private CardView[] cardViews = new CardView[8];
        private int[] cardViewId = {R.id.cardView1,R.id.cardView2,R.id.cardView3,R.id.cardView4,R.id.cardView5,R.id.cardView6,R.id.cardView7,R.id.cardView8};
        private ImageView[] imageViews = new ImageView[8];
        private int[] imageViewId = {R.id.image1,R.id.image2,R.id.image3,R.id.image4,R.id.image5,R.id.image6,R.id.image7,R.id.image8};


        private SharedPreferenceData sharedPreferenceData;
        private CheckInternetIsOn internetIsOn;
        private AlertDialogClass dialogClass;
        private InfoBackgroundTask backgroundTask;
        private NeedSomeMethod someMethod;


        private final static String USER_LOGIN = "userLogIn";
        private final static String USER_INFO = "currentInfo";
        private String currentUser,userType;

        private ArrayList<SearchableModel>groupList ;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.home_page);
                initComponent();
                closeApp();
        }

        private void initComponent()
        {

                toolbar =  findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                drawer =  findViewById(R.id.drawer_layout);
                toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView =  findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);
                view = navigationView.inflateHeaderView(R.layout.nav_header_home_page);
                textView = view.findViewById(R.id.txtUserName);
                initObject();//initialize cardView and imageView

                //other initialize
                sharedPreferenceData = new SharedPreferenceData(this);
                internetIsOn = new CheckInternetIsOn(this);
                dialogClass = new AlertDialogClass(this);
                someMethod = new NeedSomeMethod(this);

                currentUser = sharedPreferenceData.getCurrentUserName(USER_INFO);
                userType = sharedPreferenceData.getUserType();
                textView.setText(currentUser);

        }


        private void initObject()
        {
                int i=0,j=0;

                while (i< cardViews.length)
                {
                        cardViews[i] = findViewById(cardViewId[j]);
                        i++;
                        j++;
                }

                int p=0,q=0;

                while (p<imageViews.length)
                {
                        imageViews[p] = findViewById(imageViewId[q]);
                        p++;
                        q++;
                }
        }


        public void onButtonClick(View view)
        {
                initObject();
                int id = view.getId();

                if(id==cardViewId[0]||id==imageViewId[0])
                {
                        Toast.makeText(this,"click on button 1",Toast.LENGTH_SHORT).show();
                }else if(id==cardViewId[1]||id==imageViewId[1])
                {
                        Toast.makeText(this,"click on button 2",Toast.LENGTH_SHORT).show();
                }else if(id==cardViewId[2]||id==imageViewId[2])
                {
                        Toast.makeText(this,"click on button 3",Toast.LENGTH_SHORT).show();
                }else if(id==cardViewId[3]||id==imageViewId[3])
                {
                        Toast.makeText(this,"click on button 4",Toast.LENGTH_SHORT).show();
                }else if(id==cardViewId[4]||id==imageViewId[4])
                {
                        Toast.makeText(this,"click on button 5",Toast.LENGTH_SHORT).show();
                }else if(id==cardViewId[5]||id==imageViewId[5])
                {
                        Toast.makeText(this,"click on button 6",Toast.LENGTH_SHORT).show();
                }else if(id==cardViewId[6]||id==imageViewId[6])
                {
                        startActivity(new Intent(HomePageActivity.this,NoticeBoard.class));
                        Toast.makeText(this,"click on button 7",Toast.LENGTH_SHORT).show();
                }else if(id==cardViewId[7]||id==imageViewId[7])
                {
                        Toast.makeText(this,"click on button 8",Toast.LENGTH_SHORT).show();
                }else if(id==cardViewId[8]||id==imageViewId[8])
                {
                        Toast.makeText(this,"click on button 9",Toast.LENGTH_SHORT).show();
                }

        }


        @Override
        protected void onStart() {
                super.onStart();

                String fUrl = "http://192.168.56.1/checkMemType.php";
                String postData;
                try {
                        postData = URLEncoder.encode("userName","UTF-8")+"="+URLEncoder.encode(currentUser,"UTF-8");
                        backgroundTask = new InfoBackgroundTask(this);
                        backgroundTask.setOnResultListener(onAsyncTaskInterface);
                        backgroundTask.execute(fUrl,postData);

                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                }
        }

        OnAsyncTaskInterface onAsyncTaskInterface = new OnAsyncTaskInterface() {
                @Override
                public void onResultSuccess(final String message) {
                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        if(message.equals("no result"))
                                        {
                                                someMethod.closeActivity(HomePageActivity.this,LogInActivity.class);
                                                sharedPreferenceData.ifUserLogIn(USER_LOGIN,false);
                                        }
                                        else
                                        {
                                                if(!userType.equals(message))
                                                {
                                                        sharedPreferenceData.userType(message);
                                                        userType = message;
                                                }
                                        }
                                }
                        });
                }
        };


        @Override
        public void onBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit");
                builder.setMessage("Want to exit ?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("flag",true);
                                startActivity(intent);
                        }
                });
                builder.setNegativeButton("no",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.search, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int res_id;
                res_id = item.getItemId();
                if(res_id==R.id.search)
                {
                        if(internetIsOn.isOnline())
                        {
                                groupList = new ArrayList<>();
                                GetSearchableGroup searchableGroup = new GetSearchableGroup(HomePageActivity.this);
                                searchableGroup.setOnResultListener(searchInterfaces);
                                searchableGroup.execute();

                                new SimpleSearchDialogCompat(this, "Search", "enter name", null, groupList, new SearchResultListener<Searchable>() {
                                        @Override
                                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {


                                                baseSearchDialogCompat.dismiss();
                                        }
                                }).show();
                        }else dialogClass.noInternetConnection();
                }
                return true;
        }

        SearchInterfaces searchInterfaces = new SearchInterfaces() {
                @Override
                public void onResultSuccess(final ArrayList<String> list) {
                        runOnUiThread(new Runnable() {
                                public void run() {
                                        for(int i =0;i<list.size();i++)
                                                groupList.add(new SearchableModel(list.get(i)));

                                }
                        });
                }
        };


        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                userType = sharedPreferenceData.getUserType();

                switch (id)
                {
                        case R.id.makeGroup:
                                if(internetIsOn.isOnline())
                                        startActivity(new Intent(HomePageActivity.this,MakeMyGroup.class));
                                else dialogClass.noInternetConnection();
                                break;

                        case R.id.myGroup:
                                if(internetIsOn.isOnline())
                                        startActivity(new Intent(HomePageActivity.this,MyGroupInfo.class));
                                else dialogClass.noInternetConnection();
                                break;

                        case R.id.editProfile:
                                if(internetIsOn.isOnline())
                                        startActivity(new Intent(HomePageActivity.this,EditYourProfile.class));
                                else dialogClass.noInternetConnection();
                                break;

                        case R.id.acceptRequest:
                                if(userType.equals("admin"))
                                {

                                }else dialogClass.error("Only admin can accept request.You are not an admin.");
                                break;

                        case R.id.makeAdmin:
                                if(userType.equals("admin"))
                                {
                                        startActivity(new Intent(HomePageActivity.this,AdminPanel.class));
                                }else dialogClass.error("Only admin can make another admin.You are not an admin.");
                                break;

                        case R.id.member:
                                if(internetIsOn.isOnline())
                                        startActivity(new Intent(HomePageActivity.this,AllMemberList.class));
                                else dialogClass.noInternetConnection();
                                break;

                        case R.id.setAlarm:
                                break;

                        case R.id.setting:
                                break;

                        case R.id.feedback:
                                startActivity(new Intent(HomePageActivity.this,Feedback.class));
                                break;

                        case R.id.aboutUs:
                                break;

                        case R.id.logout:
                                userLogOut();
                                break;

                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }

        protected void userLogOut()
        {
                final ProgressDialog progressDialog = ProgressDialog.show(HomePageActivity.this, "Please wait", "User Log out...", true);
                progressDialog.setCancelable(true);
                new Thread(new Runnable() {
                        @Override
                        public void run() {
                                try {
                                        Thread.sleep(2500);
                                } catch (Exception e) {
                                }
                                progressDialog.dismiss();
                        }
                }).start();
                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                                sharedPreferenceData.ifUserLogIn(USER_LOGIN,false);
                                finish();
                        }
                });
        }

        private void closeApp()
        {
                if(getIntent().getBooleanExtra("flag",false))finish();
        }
}
