package com.example.judav_000.pgr02excitementdocumentation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.TextView;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

/* Mobile class that opens the camera to take an excited picture, and automatically sets up a
 * tweet for the user to edit and send out onto social media. */
public class Camera extends Activity {

    private Uri fileUri;
    private Intent intent;
    private TextView textView;
    private int cameraRequestCode = 10, tweetRequestCode = 20;
    private String picLocation = "/sdcard/pic.png", hashtag = "#cs160excited";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handheld_loggedin);

        // Since camera is activated, excitement notification can be dismissed.
        NotificationManagerCompat np = NotificationManagerCompat.from(getApplicationContext());
        np.cancel(100);

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Save image locally to the phone.
        fileUri = Uri.fromFile(new File(picLocation));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, cameraRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Create tweet once an exciting picture has been captured.
        if (requestCode == cameraRequestCode && resultCode == RESULT_OK) {
            // Fetch picture from location.
            File myImageFile = new File(picLocation);
            Uri myImageUri = Uri.fromFile(myImageFile);
            // Compose tweet.
            Intent makeTweet = new TweetComposer.Builder(Camera.this)
                    .text(hashtag)
                    .image(myImageUri).createIntent();
            startActivityForResult(makeTweet, tweetRequestCode);

        // Starts intent to find a related tweet once the user's tweet has been published.
        } else if (requestCode == tweetRequestCode && resultCode == RESULT_OK) {
            Intent findTweet = new Intent(this, FindTweet.class);
            startActivity(findTweet);
        }
    }
}
