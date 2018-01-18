# Week 2 - Day 2
Added most, if not all, of the functionality needed to work with the GloBI API, requesting information and displaying information in a list view. Also, i have added basic functionality for working with the Google Maps Plugin.

# Week 2 - Day 3
Worked on the implementation of the Google Maps Plugin. Right now i can hand coordinates as an extra, but i still have to make the ActionResultsActivity handle the extra correctly and also allow it to handle extras from the other 'path'. The API request with coordinates works a bit wonky: sometimes it responds quickly, sometimes fast and sometimes not at all.  
I found some databases which could help with allowing Latin to Common name conversion as well as implement an autocomplete function. 
I recieved some help from Martijn with transforming the database into usable data and tomorrow i will attempt to implement a SQLiteHelper.

# Week 2 - Day 4
Today i will try to make sure the basic functionality the app provides works well, which will mainly be the handling of extras. Also if i have time i would like to start on the implementation of the databases.

### Update 15:29
Basic functionality is complete. I also added some comments to make the code more readable.
Further TODO items are:
- Add another activity to search for area as well as specific species and interaction.
- Create SQLite support for Latin and Common names
    - Add Common names to interaction results where possible
- Create SQLite support for external ids.
- Make layout prettier
- Make a display on top of the results to show what the search queries were.

