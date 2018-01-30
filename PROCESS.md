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
    - Wont be a lot harder (just merging existing activities.)
- Create SQLite support for Latin and Common names
    - Add Common names to interaction results where possible
    - Getting the Data in SQLite will probably be hard.
- Create SQLite support for external ids.
- Make layout prettier
- Make a display on top of the results to show what the search queries were.
    - Also not a very hard task

# Week 3 - Day 1
Over the weekend i added SQLite support for getting the common and latin names. Today i have transformed a database with python, to be used as an autocomplete feature.

# Week 3 - Day 2
I have thought about how to implement an autocomplete feature that also contains the common name where possible. So ive decided to put the name where possible in parentheses behind the latin name.
Also, i have discovered by re-downloading the database that it has shrunk in size: from 800MB to just 80MB, but this is probably just done to filter out double values. But just to be sure i will use this new database for latin-to-common conversion and autocompleting search queries.

# Week 3 - Day 3
Today i have used python to transform the databases into usable data. For thursday and friday i hope to complete the usage of these databases.

# Week 3 - Day 4
I have implemented the autocomplete function, which now works for all the EditTexts. Only one problem because the common name is inside of parentheses so the autocomplete does not pick it up.

# Week 3 - Day 5
Added sites showing info on species in a new activity.