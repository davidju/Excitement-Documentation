# Excitement Documentation Android Application
 
This application lets users document things that make them excited on Twitter! This project explores smartwatch integration with mobile devices, specifically on the Android and Android Wear, as well as developing familiarity with Android sensors and fluency with Twitter API for storing and retrieving information.
 
If the smartwatch detects volatile movement from the user (via a wearable device), it assumes the user is excited and notifies the user of his or her excited state via a notification sent to both the handheld and wearable device. Tapping on either notification opens the camera on the handheld device to allow the user to take a picture of what is making him or her excited. If a picture is taken, the user is automatically directed to an interface for writing a tweet, with the photo he or she just took automatically loaded in with a predefined hashtag. After submitting the tweet, the wearable device will display the most recent photo taken by another user tagged with the same hashtag.


## Usage

The APK files for both the mobile and wear app are provided in the git repository, available to install on the device or emulator of your choice. This application uses Twitter API, so if you would like to build this app locally, you must provide your own Consumer Key and Consumer Secret. Insert both key and secret into the strings.xml file under the name key_twitter and key_twitter_secret, respectively.