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
