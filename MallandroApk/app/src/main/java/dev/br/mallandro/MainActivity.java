package dev.br.mallandro;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends Activity {
//public class MainActivity extends Activity {
    private Spinner imageSpinner;
    private ImageView imageView;
    private Spinner timeSpinner;
    private Button scareButton;
    private Button shareButton;
    private int currentTime;
    public static final String TIME_EXTRA = "TimeExtra";
    public static final String IMAGE_EXTRA = "ImageExtra";
    public static final String SOUND_EXTRA = "SoundExtra";
    private int currentImageID = 0;
    private InterstitialAd mInterstitialAd;
    private static MainActivity Instance;

    public MainActivity(){
        super();
        Instance = this;
    }

    public static MainActivity getInstance(){
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadAdware();

        imageSpinner = (Spinner)findViewById(R.id.spinner_image);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.image_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(adapter);
        //when an item is selected call the selectSensor method
        //imageSpinner.setOnItemSelectedListener(new ImageOnItemSelectedListener());

        imageView = (ImageView)findViewById(R.id.imageView);

        scareButton = (Button)findViewById(R.id.button_scare);

        shareButton = (Button)findViewById(R.id.share_button);

        timeSpinner = (Spinner)findViewById(R.id.spinner_time);
        ArrayAdapter adapterTime = ArrayAdapter.createFromResource(this, R.array.time_arrays, android.R.layout.simple_spinner_item);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapterTime);

        currentTime = 15;
        configureViews();
    }

    private void loadAdware(){
        //Adware Main Activity
        AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        /*AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E112885C2D32D31690C7B60F25C89356")
                .addTestDevice("13E7A5DDF2981F979D554ED02BC571B3")
                .build();*/
        mAdView.loadAd(adRequest);

        //Adware interstitial ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getString(R.string.banner_ad_unit_id));
        /*AdRequest adRequestInterstitialAd = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E112885C2D32D31690C7B60F25C89356")
                .addTestDevice("13E7A5DDF2981F979D554ED02BC571B3")
                .build();*/
        AdRequest adRequestInterstitialAd = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequestInterstitialAd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureViews(){
        imageSpinner.setOnItemSelectedListener(new ImageOnItemSelectedListener());

        timeSpinner.setOnItemSelectedListener(new TimeOnItemSelectedListener());

        //Scare Button
        View.OnClickListener scareListener = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                doScare();
            }
        };
        scareButton.setOnClickListener(scareListener);

        //Share Button
        View.OnClickListener shareListener = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String message = "Pegadinha no celular - https://play.google.com/store/apps/details?id=dev.br.pegadinha";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Pegadinha no celular"));

            }
        };
        shareButton.setOnClickListener(shareListener);

    }

    private void doScare(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, currentTime);
        Intent myIntent = new Intent(MainActivity.this, PegadinhaReceiver.class);
        myIntent.putExtra(TIME_EXTRA,currentTime);
        myIntent.putExtra(IMAGE_EXTRA, currentImageID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        this.finish();

    }

    public class ImageOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

            int imageResource = 0;
            if(pos==0){
                imageResource = R.drawable.gorila;
            } else if(pos==1){
                imageResource = R.drawable.mallandro;
            }
            currentImageID = imageResource;
            Drawable image = getResources().getDrawable(imageResource);
            if(imageView!=null)  //imageView is a ImageView
                imageView.setImageDrawable(image);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }

    public class TimeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

            if(pos==0){
                currentTime = 10;
            } else if(pos==1){
                currentTime = 15;
            } else if(pos==2){
                currentTime = 20;
            } else if(pos==3){
                currentTime = 25;
            } else if(pos==4){
                currentTime = 30;
            } else if(pos==5){
                currentTime = 35;
            } else if(pos==6){
                currentTime = 60;
            } else if(pos==7){
                currentTime = 75;
            } else if(pos==8){
                currentTime = 90;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }



    public InterstitialAd getInterstitialAd(){
        return mInterstitialAd;
    }
}


