# Open Weather App with Android Architecture Components

This app write in Java uses the new [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
: Livedata, ViewModel, and Room. 

Retrieves **current weather** using [OpenWeatherMap current API](https://openweathermap.org/current) free account.
## Introduction

### The final architecture
The following diagram shows all the modules in our recommended architecture and how they interact with one another:

![alt text](/images/android%20architecture.jpg)

### Functionalities

The app is composed of 3 main screens.

#### ShowWeatherFragment
Presents a viewpager with the description of the weather of every city the user had selected.
A job scheduled allows this fragment reflects the current weather data through synchronization with OpenWeatherMap servers every 1 hour.
#### ManageCitiesFragment
This fragment displays all the weathers of the cities the user had selected.
#### AddCityFragment
Allows the user to add a new city of interest to know details of the weather. To add a new city the user has two options: 
1) Add his current location
using FusedLocationProvider, making a request to get the current weather by city coordinates; 
2) Search by the name of the city of interest, making a request to get the current weather by city name.

### Building
You can open the project in Android studio and press run.

### Libraries
* [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html)
* [Android Architecture Components](https://developer.android.com/arch)
* [Android Data Binding](https://developer.android.com/topic/libraries/data-binding/index.html)
* [Volley](https://github.com/google/volley) for REST api communication
* [Firebase JobDispatcher](https://github.com/firebase/firebase-jobdispatcher-android) for scheduling background jobs
* [Gson](https://github.com/google/gson) to convert Java Objects into JSON and back
* [Google play services location](https://developers.google.com/location-context/fused-location-provider/) fused location provider
* [Material ViewPager Dots indicator](https://github.com/tommybuonomo/dotsindicator) for ViewPager animation and design
