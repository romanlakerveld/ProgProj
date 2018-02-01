# Programmingproject - Project proposal

### Problem statement
Studying interactions between species is an important part of Biology. These interactions can range from eating each other, helping each other (for example by pollination) or infecting each other.
Being able to quickly find species interactions would certainly help in research, especially biology students who dont want to go through a lot of papers to discover these interactions.

### Solution
The solution would be a simple app, in which you can search for species or interaction, and show the interaction as well as more information on the species associated with the interaction.

### Visual sketch
![sketch](https://github.com/romanlakerveld/ProgProj/blob/master/doc/reportsketchfinal.bmp)

### Main features
- Getting interactions between 2 species
- Getting interactions of 1 species with other species.
- Getting interactions by location
- Getting more information on the species.

The MVP would contain all of the above mentioned features, but it would be better to have all the information of the species in the result screen and not in an external browser window, although there is not yet an easy way to do that, that i'm aware of.

### Data sources:
- Global Biotic Interactions API https://github.com/jhpoelen/eol-globi-data/wiki/API - This API merges a collection of datasets containing interactions of species (for an example: check https://inaturalist.org/observations/3848742, which shows a sea otter eating a crab as well as the specific location). The API allows search for specific species as well as on a specific type of of interaction. The API also allows rectangular search, which returns only interactions within that rectangle.

### External components:
- Horizontal Picker widget by Blazsolar: https://github.com/blazsolar/HorizontalPicker. HorizontalPicker is licensed under the Apache License 2.0
- Google maps
- SQLiteOpenHelper
- SQLiteAssetHelper: i got most of the code from a tutorial on javahelps.com: http://www.javahelps.com/2015/04/import-and-use-external-database-in.html

### Existing applications:
I have not found any existing apps with the same purpose as this idea. Although the web application https://www.globalbioticinteractions.org/ uses the same API and does what i want to do in my app.

### Hardest part
I expected the hardest part to be setting up the communication with the API, since the returned JSON isn't always as
 straightforward as one would hope. Also implementing Google Maps into the app would seems like a hard obstacle, but not that hard.     
 Eventually these steps were a lot easier to overcome than i had thought and the hardest part was actually getting all the SQL databases working (autocomplete, converting latin to common and getting URLs).
