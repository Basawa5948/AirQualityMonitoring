PROBLEM STATEMENT
A single activity app to display live air quality monitoring data.


# AirQualityMonitoring
# PART A 
- Live City wise AQI (Air Quality Index) Table
- AQI highlighted by different colors based on level (Good, Poor, Severe, etc, refer to chart on next page)
- AQI upto 2 decimal places
# PART B
- Clicking on the city should open a real time (30s interval or more) chart of the AQI of the selected city

DATA
App will subscribe for updates via WebSockets
Server url: ws://city-ws.herokuapp.com/

Updates (WebSocket messages) will be provided in the following form:
[ { city:”Mumbai”, aqi:”150.23145”}, { city:”Delhi”, aqi:”250.23145”} , . . . ]
Each update will contain 0 or more cities/AQI pairs. You may assume that update messages will have no more than 12 cities/AQI pairs.

# APP ARCHITECTURE
- App is designed on MVVM Design Architectural Pattern
- One Activity with two fragments, Fragment A(PART A) to display the table with the live response from webSocket, Fragment B(PART B) to display the graph of the selected city from the table.
- One Single ViewModel shared between two fragments which talks to the network layer and provides the view with the latest data using LiveData
- One Network Layer where the websocket is initiated and is fetching the response, parsing the response, forming the Model Class for the view to use.

# CORE LOGIC
- Loading Fragment A when the app is launched, Fragment A will register itself to the ViewModel present.
- ViewModel interacts with the network layer of the app where the websocket is initiated and the listeners are created to fetch the response
- once the response is received, the network layer parses the data and forms the Object/Model class of the responseType.
- This is passed on to the viewModel through liveData modules.
- Frgagment A is already observing for the latest data and hence the latest info is fetched and displayed in the table

# PART B APPROACH
- A Small interface between the two fragments for communication is created, on clicking the city name(it is a textView, hence the callback is easily obtained).
- A dataStrcutre can be used to store info of all the AQIValues for each city from the response.
- Pass this dataStructure and the city name from Fragment A to Fragment B
- Use the MPAndroidChart library to display the graph in the Fragment B

# Library's Used
- okHttp for webSocket Listeners
- gson for forming the model class from the response
- recyclerView and CardView for the UI
- lottie for the animations
- mockito and jUnit for Unit Test cases

# Unit Test Cases
- A Helper Utility Class which holds the functions for displaying data based on requirements was considered for UnitTest Cases

# Time Taken
- 12 - 15 Hours
