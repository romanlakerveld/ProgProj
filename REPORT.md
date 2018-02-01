# Report - InterAction
## App description
<div>Studying interactions between species is an important part of Biology.
The goal of this app is to allow data on these interactions to be better accessible
 and not feel as tedious as having to dig through a lot of information.
 Shown below is the way interactions are displayed:</div>
<img height="480" width="270" src="https://github.com/romanlakerveld/ProgProj/blob/master/doc/screenshot.png"/>


## Technical design
![sketch](https://github.com/romanlakerveld/ProgProj/blob/master/doc/reportsketchfinal.bmp)
### Activities
- MainActivity: this activity class handles the main screen. It contains a button to navigate
 to the SearchMashup activity and contains an EditText and a button to search information on
 a single species.
    - Calls getAllTaxa from the DatabaseAccess class to get all taxa from the SQL table to use
    for the autocompleteEditText
- SearchMashup: this activity contains the parameters part of the search functionality.
    - Similarly to MainActivity, this activity calls getAllTaxa to fill the AutocompleteTexts.
    - Also uses a StringRequest to get all supported interaction types from the GloBI API.
    - Uses Google Maps plugin to allow the user to specify an area to search in, the map of which
    can be hidden by clicking the "Allow area search" checkbox.
    - Calls getMapCoords from MapUtils class to get coordinates from the map.
- ActionResultsActivity: this activity displays the found interactions when given certain parameters.
    - Calls HandleParametersFromIntent to get the search parameters from the intent and build an url with them.
    - Secondly, calls GetInteractionsFromURL to call the API with the previously stated URL, then puts those interactions in a ListView with the InteractionAdapter.
- ResultsActivity: this activity takes a single species and requests the SQL database for relevant URLs.
    - First calls getIDsOfSpecies to get IDs of the specified species.
    - Secondly calls convertIDsToURLs, which loops over all the recieved IDs and converts them to usable URLS.
### Helper classes
- InterActionAdapter: this adapter is used by ActionResultsActivity to display interactions in a ListView.
    - Every view contains two buttons, which represent the species in the interaction, it also sets the OnSpeciesClicked listener on both buttons.
    - Uses the ViewHolder class to manage recycling the views.
- ViewHolder: holds the view for the adapter
- OnSpeciesClicked: listener that's used by the InteractionAdapter
    - Has OnClick method which gets the text of the clicked button (the species) and hands it to the ResultsActivity
- MapUtils: class for handling map coordinates
    - getMapCoords takes a google map object and gets the coordinates of the current projection to hand to the API.
    - round method is used by getMapCoords to round the coordinates down to 2 decimal positions.
- DatabaseAccess: handles the retrieval of data from the SQL database.
    - getCommon method takes a latin String and converts it to a common name if possible.
    - getAllTaxa method selects all values from the autocomplete table to be used for Autocomplete feature
    - getAllUrls method takes a latin String and returns all relevant URLs found in the database.
- DatabaseOpenHelper: handles assets needed for the database
- Interaction: class for representing interactions. Contains getters and setters for:
    - source of interaction
    - interaction type
    - target of interaction.

## Development challenges
### Changes in design
My design has certainly changed a lot from the initial sketch. At first i planned on having an activity for every type of search possible:
searching with only source and target, searching with only an area and searching with both. Once most of the hard coding parts were over i wasn't satisfied with the navigation so i decided to
put all search possibilities in a single activity, fittingly named SearchMashup, in which the user can hide or show the map. This influences the search parameters.
Also i put searching for a single species directly in the main activity, instead of having to navigate to it first, this seemed more fitting to me.
I think the design changes were for the better, since the searching is much more compact now. I think that, when given more time, i would have been able to come up with an even more elegant solution
but i am happy with the current result.
### Struggling with the API
Getting URLs and common names from the API was certainly harder than i first envisioned. Getting the interactions was pretty straightforward, but to get the common names
and the URLs i had to download some .tsv files from the API github page which were too large to open with excel. So to transform the data
 of the files i used Unix to get the needed columns and filter some values and i wrote a simple Python script to transform the data to what i needed.
Lastly i used an SQL GUI to put the transformed data in an SQL databasefile and added that to the asset folder of my app.

