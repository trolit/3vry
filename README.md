<h2>3vry</h2>

<img src="https://img.shields.io/badge/Platform-Android-red?color=2DE22B&style=flat-square" alt="Platform badge"/> <img src="https://img.shields.io/badge/Supported language-EN-red?color=E41570&style=flat-square" alt="Language badge"/>

<p align="center"><img src="https://raw.githubusercontent.com/trolit/3vry/images/images/logo.png" width="550" alt="3vry logo"></p>

<p align="justify">3vry is a project made for Android OS that promotes listening to music. User adds his/her favourite artists/playlists and receives each day single track offer from randomly chosen object. Songs are picked from YouTube service using Android Player library and can be watched through application. Track searching rules can be adjusted through settings section. It's also possible to include app author playlist in the artists pool(contains wide and interesting range of genres:>). Main purpose of the 3vry was to find out Kotlin language and IntelliJ IDEA IDE from JetBrains after making mobile app <a href="https://github.com/trolit/document-and-compare">docAndCom</a> in Xamarin.Forms. On the other side, music has great impact on mind, determines feelings. It's crucial element in video games and cinematography. 3vry can be treated as option to discover tracks that app user did not hear before. Short demonstration can be found at YouTube <a href="https://www.youtube.com/watch?v=ZJ2wglKpu5M">here</a>.</p>

<h2>Screens</h2>

| | | |
| :---: | :---: | :---: |
| <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/1.PNG" alt="#toadd" height="300"/> | <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/2.PNG" alt="#toadd" height="300"/> | <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/3.PNG" alt="#toadd" height="300"/> |
| <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/4.PNG" alt="#toadd" height="300"/> | <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/5.PNG" alt="#toadd" height="300"/> | <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/6.PNG" alt="#toadd" height="300"/> |
| <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/7.PNG" alt="#toadd" height="300"/> | <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/8.PNG" alt="#toadd" height="300"/> | <img src="https://raw.githubusercontent.com/trolit/3vry/images/images/9.PNG" alt="#toadd" height="300"/> |
<!-- For image table, it's highly recommended to have the same resolution images. 
 To find best results(no stretches, equal cells), both axis should be adjusted manually. -->

<h2>Tested on</h2>

- Physical device: Huawei Y7, OS. Android 7.0(API 24), res. 720 x 1280
- Physical device: Huawei P20, OS. Android 9.0(API 28), res. 2244 x 1080
- Android emulator: Nexus S 4" phone, OS. Android 8.1(API 27), res. 480 x 800

<h2>Permissions needed</h2>

:white_check_mark: <strong>INTERNET</strong> to make queries to YouTube API and be able to play songs from the list<br><br>
:white_check_mark: <strong>ACCESS NETWORK STATE</strong> to show on main menu screen message that Internet connection is not established <br>
<!-- If you did not specify icon, simply overwrite Id put between : : characters with desired icon name -->
<!-- Supported by GitHub icon list can be found here: https://gist.github.com/rxaviers/7360908 -->

<h2>Used libraries</h2>

- <a href="https://github.com/square/retrofit">Retrofit 2.9.0</a>
- <a href="https://github.com/square/moshi">Moshi JSON Parser 2.9.0</a>
- <a href="https://github.com/sqlite/sqlite">SQLite</a>
- <a href="https://fontawesome.com/">Font Awesome icons</a>
- <a href="https://developers.google.com/youtube/android/player/downloads">YouTube Android Player API</a>
- <a href="https://github.com/koral--/android-gif-drawable">android-gif-drawable</a>
- <a href="https://loading.io/">loading indicator</a>

<h2>Download & info</h2>

Minimum required Android version to use 3vry: <strong>7.0 (Nougat)</strong>

Targetted Android OS: <strong>10.0 (Android Q)</strong>

<p align="justify">You can download the *.apk installation file by clicking <a href="https://github.com/trolit/3vry/releases/download/v1.1/3vry_1.1.apk">here</a>. <strong>Note that the original 3vry solution developed by <a href="https://github.com/trolit">trolit</a></strong> is not currently available on any other media delivering software than this GitHub repository. Guaranteed, safe way to obtain the app without suspicious modifications is to download it through <strong>the link given above</strong>! Other pages are unverified. Any information that user will add/generate through 3vry is kept on local device storage and is not shared further without user's knowledge. App <strong>will never</strong> prompt for permissions other than detailed in the documentation, show advertisements. There is no IOS version of the application. Compiled version is using YouTube standard queries upper limit(10 000). If app is used properly, each user generates from 100(1 page) to 400(4 pages) queries per day(estimated cost on 06.2020 from <a href="https://developers.google.com/youtube/v3/determine_quota_cost">quota calculator</a>). Query cost depends on two aspects: how many tracks may be considered into search from settings section and randomized page number. If user set 150 tracks per search, app can make from 1 to 3 queries, each query returns 50 tracks. Assuming that all users set 200 tracks range in settings, and get the biggest possible page number(4), 25 users will be able to receive their tracks. In the least case of 100 unit cost per user, track would get 10 000 / 100 = <strong>100 users</strong>.</p>

<p align="justify">To proceed with the downloaded apk, refer to the Android documentation <a href="https://developer.android.com/studio/publish#publishing-unknown">HERE</a> on how to enable 3rd party app installation(if you did not enable that option before) and then simply launch apk file while using Android device. </p>

<h2>Possible features</h2>

- Improve Db Handler using <a href="https://developer.android.com/topic/libraries/architecture/room">Room lib</a>
- ~~Change rawQueries into queries(according to stackoverflow, rawQueries are more vulnerable to SQL Injection, however app Db is not that interesting..)~~
- ~~Remove from Settings: include cover, acoustic, live and add instead section where user can define custom list of banned words~~
- ~~Add section where users can add their own playlists to the searching pool~~
- Fix weird behaviour when turning on fullscreen from YouTubePlayerView
- ~~Add indicator that demonstrates song searching~~
- ~~Limit song data wipe(at least 2 tracks)~~

<h2>Changelog</h2>

<h4>22.07.2020 (v1.1)</h4>

- removed include live/cover/acoustic from Settings and added Keyword section instead
- added Playlist section that can hold user defined playlists
- added try..catch block for getting error on nonclarified API response fails
- added to main menu note that indicates process of searching video
- added repeating artist/playlist/keyword validation on insert
- changed the way how video is randomized from artists and/or playlists
- changed rawQueries into queries
- moved app author playlist from Artist section to Playlist
- adjusted playlistResults action to be used by all defined playlist Id's
- restricted wipe songs button(visible when 3vry has at least 2 tracks)

<h2>Running project</h2>

<p align="justify">If you would like to run the app in the debug mode by yourself, open the project and add your own YouTube API key in the SongsActivity(line 56). Simply run the app on AVD that matches required version described above.</p>

<h2>License</h2>

<p align="justify">Standard MIT license (c) 2020 Paweł Idzikowski. Publishing the 1 to 1 solution on other sites must be discussed with it's founder first!</p>

<br/>
<br/>

Template generated using <a href="https://github.com/trolit/EzGitDoc">EzGitDoc</a>
