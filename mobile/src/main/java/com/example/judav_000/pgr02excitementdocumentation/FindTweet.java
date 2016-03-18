package com.example.judav_000.pgr02excitementdocumentation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* Mobile activity class that searches for a related tweet with hashtag #cs160excited.  */
public class FindTweet extends Activity {

    private Bitmap picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handheld_loggedin);

        TwitterApiClient twitterClient = TwitterCore.getInstance().getApiClient();
        SearchService service = twitterClient.getSearchService();
        // Only parameter that is necessary to specify is the query; everything else is fine by default.
        service.tweets("#cs160excited", null, null, null, null, null, null, null, null, true, new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                long userID = Twitter.getSessionManager().getActiveSession().getUserId();
                List<Tweet> tweets = result.data.tweets;

                // Iterate through tweet search results.
                for (Tweet tweet : tweets) {
                    // Checks to make sure related tweet is not from the user.
                    if (tweet.user.id != userID) {
                        String url;
                        // Cannot retrieve tweet image url for some tweets. If so, ignore and try the next one.
                        try {
                            url = tweet.entities.media.get(0).mediaUrl;
                        } catch (NullPointerException e) {
                            continue;
                        }

                        final String tweetUrl = url;
                        // Dedicates single worker thread to retrieve tweet image, since processing cannot occur on main thread.
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                picture = retrieveImage(tweetUrl);
                            }
                        });

                        // Creates delay so tweet verification has time to be displayed before related tweet
                        // notification is displayed.
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                        }
                        // Creates and builds notification for related tweet.
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(FindTweet.this)
                                .setSmallIcon(R.drawable.cast_ic_notification_2)
                                .setLargeIcon(picture)
                                .setContentTitle("Hey!")
                                .setContentTitle("Someone else is also excited!")
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(picture));
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(FindTweet.this);
                        notificationManager.notify(2, notificationBuilder.build());
                        break;
                    }
                }
            }
            @Override
            public void failure(TwitterException e) {
            }
        });
    }

    /* Method to decode the tweet image given its url and returns in Bitmap format for notification. */
    private Bitmap retrieveImage(String tweetUrl) {
        try {
            return BitmapFactory.decodeStream((InputStream) new URL(tweetUrl).getContent());
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return null;
    }
}
