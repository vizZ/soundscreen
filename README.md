Project requirements
---

* **[Done]** Android 2.1+ compatible

	The project is Android 2.1 compatibile. 


* **[Done]** Don't unnecessarily drain battery.

	It does not unnecessary drain battery life. 
	
	The SyncService is spawned on wallpaper's engine creation, it fetches the initial data (for example: "/users/stevevaihimself/tracks.json"), puts it into the db (ContentProvider + SQLite3) and schedules itself within AlarmManager for periodic updates (*default is 1h, but it can be set with the app settings*). After a successful update it schedules a download task with the FileService.
	
	File Service checks for any *left* (CACHED=0 and USED=0) tracks stored in the db. It takes some number of left tracks (*the default number is 10, but can be changed with settings*) and downloads the tracks waveform and artwork and caches them in the internal memory (*this will be changed and made settings dependent*). After the first pair is downloaded the service broadcasts (localy) the ACTION_FILE_AVAILABLE intent which is received by wallpaper engine's receiver. Then the receiver triggers the wallpaper update.
	
	The application downloads the files in a batch and it syncs using AlarmManager.setInexactRepeating() to prevent the radio from being used inefficiently.


* **[Done]** Make sure network settings are honoured.

	The network settings are honored.
	
	The user can set the downlaod policy with the app's settings. The app makes sure that this policy does not violate the global network settings.


* **[Done]** Use the SoundCloud API to obtain the PNG with the waveform

* **[Done]** The displayed waveforms should change from time to time

	The user can set the display time in wallpaper's settings.


* **[Done]** The waveform should look nice, so some form of post-processing is probably needed. It should also fit the screen.

	The waveform is processed to fit the screensize.


* **[Done]** Optional: the user should be able to interact with the screen background

	The user can double-tap the wallpaper to be redirected to the track's url. If Soundcloud app is present on the device - the track will be displayed with the Soundcloud app itself. If there is no Soundcloud app present on the device the track will be opened within the browser.
	

* *[To be done]* Optional: display the name of the track on the home screen

Notes on the project
---
Doing the same thing the same way over and over again was never my favourite part of every-day programming, so I wanted to try out some new ideas while having this opportunity. And I wanted to have fun ;)

**SQLBuilder**

Writing native SQL code is tedious and error-prone. The idea was/is to introduce a lightweight, flexible and convenient wrapper (probably as a small utility library someday) around native SQL code that helps you generate your SQL code with ease and confidence.

Please treat this idea as an experiment/proof-of-concept rather than finished/polished product/code.

*The next step woud be to add support for constraints in table definitions.*

**ContentProvider, SQLiteOpenHelper and UriMatcher**

Have you ever seen a ContentProvider, SQLiteOpenHelper or UriMatcher serving tens of models in tens of configurations, thus growing and growing to some huge volume? Well, I have, so I wanted to tackle this problem with a first small step - decomposition.

So I've created specialized, model-specific classes encapsulating appropriate functionalities (like TracksHelper for DataSQLiteOpenHelper, TracksMatcher for DataUriMatcher) and made the delegation easy by having them wrapped with a Table enum and enabled iteration over the existing models (by "model" I mean a set of all db-specific classes).

Again, please treat this idea as an experiment/proof-of-concept rather than finished/polished product/code.

*The next step is to try out Provider-specific class and polish handling db migradions with Helper classes (onUpgrade()).*


How to build the project
---

Open the project with Android Studio or delete the ".idea" directory and import the project to your IDE of choice.

Remember to replace the CLIENT_ID and CLIENT_SECRET in the build.gradle file with your own:

	android {
	    compileSdkVersion 18
	    buildToolsVersion "19"
	
	    defaultConfig {
	        minSdkVersion 7
	        targetSdkVersion 18
	
	        buildConfig "public static final String CLIENT_ID = \"YOUR_CLIENT_ID\";", \
	                    "public static final String CLIENT_SECRET = \"YOUR_CLIENT_SECRET\";"
	    }
	}

**Gradle**

If you happen to use Java 6 keep in mind that System.console() call returns null if running "gradle --daemon", so please remove/comment appropriate line in gradle.properties or build with Java 7 (**needs to be fixed/verified, investigating the issue**).

	org.gradle.daemon=true

References:

* [GUI should allow some way to provide character input to interactive child processes](http://issues.gradle.org/browse/GRADLE-1147)

**Produard**

*ActionBarSherlock:*

Warning: com.actionbarsherlock.internal.ActionBarSherlockCompat: can't find referenced class com.actionbarsherlock.BuildConfig

References:

* [[ProGuard] ActionBarSherlockCompat: can't find referenced class com.actionbarsherlock.BuildConfig](https://github.com/JakeWharton/ActionBarSherlock/issues/1001)
* [Replaced last BuildConfig.DEBUG with ActionBarSherlock.DEBUG](https://github.com/JakeWharton/ActionBarSherlock/pull/1027)


Things to improve
---

* Load CLIENT_ID and CLIENT_SECRET from a configuration file
* Check for storage left when caching files
* Add some nice exponential backoff mechanism to SyncService
* Enable wallpaper scrolling
* Add some nice paralax effect to wallpaper (try using AndEngine)
* Try reusing/enabling SoundCloud auth to fetch user's data
* Introduce better cache management (Memory/Internal/External)
* Add some nice transition effect when switching wallpapers
* Think of adding some showcase for better discoverability (for example the double tap)
* Enable adding aome picture from the Gallery as the background fillin 
* Create some custom IntegerListPreference
