# Bill Tracker Android App

Android app that tracks and allows user queries for current bills being discussed in congress.

* Built using Android Studio, Java, and Google Firestore. 
* Bulk data aggregated through open-source [Congress API](https://github.com/unitedstates/congress).
* Data scraped, parsed, and encoded using Python backend.  

## Version 1.0 

1. Users can query for sorted bill data based on subject, sponsor, status, bill id, or action dates.
2. Queries are paginated within Firebase and loaded on "infinite" scroll down.
3. Custom text-search index developed in Python for title searches.  

Showing search results for "Criminal justice and law enforcement."

![Bill Tracker Demo](https://media.giphy.com/media/fv40UKht6y0jAd8zf5/giphy.gif)
