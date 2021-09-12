PROBLEM STATEMENT
Create a single activity app to display live air quality monitoring data.


# AirQualityMonitoring
- Live City wise AQI (Air Quality Index) Table
- AQI highlighted by different colors based on level (Good, Poor, Severe, etc, refer to chart on next page)
- AQI upto 2 decimal places
- Clicking on the city should open a real time (30s interval or more) chart of the AQI of the selected city

DATA
Your app should subscribe for updates via WebSockets
Server url: ws://city-ws.herokuapp.com/

Updates (WebSocket messages) will be provided in the following form:
[ { city:”Mumbai”, aqi:”150.23145”}, { city:”Delhi”, aqi:”250.23145”} , . . . ]
Each update will contain 0 or more cities/AQI pairs. You may assume that update messages will have no more than 12 cities/AQI pairs.
