# Drama Starter — Multilingual Android APK

A starter Android short-video app built with Kotlin, Jetpack Compose and AndroidX Media3 ExoPlayer.

## Language support
The UI includes language filters for English, Malayalam, Hindi, Tamil, Telugu, Kannada, Bengali, Marathi, Punjabi, Gujarati, Urdu, Arabic, Chinese, Japanese, Korean, Spanish, French, German, Portuguese, Russian, Indonesian, Thai, Turkish, Vietnamese, Persian, Italian, Dutch, Swahili and Other. Add further languages to `supportedLanguages` as needed.

The demo catalogue currently contains one English playback sample. Other language filters will show an empty state until actual licensed videos are added. Language support is a UI capability, not a claim that a full catalogue is already available.

## Build a test APK for free
1. Create a GitHub repository.
2. Upload the contents of this project.
3. Open the **Actions** tab.
4. Run **Build Android APK**.
5. Download `drama-starter-debug-apk` from the workflow run's Artifacts.

The generated debug APK is for testing/sideloading. A Play Store release needs a signed release build and a Play Console developer account.

## Content and monetization
Only upload content you own or have explicit permission to redistribute. Check the license of each individual video and keep required credits. The sample video URL is for playback integration testing only; do not assume it is cleared for commercial redistribution.

AdMob has not yet been integrated. Add it after the core app and content rights are ready, using Google's test ad IDs during development.

## Code license
This starter app code is MIT-licensed. AndroidX and other dependencies retain their respective licenses.
