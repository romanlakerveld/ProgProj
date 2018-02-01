# Report - InterAction
## App description
Studying interactions between species is an important part of Biology.
 The goal of this app is to allow data on these interactions to be better accessible
 and not feel as tedious as having to dig through a lot of information.
 Shown below is the way interactions are displayed:
<img height="480" width="270" src="https://github.com/romanlakerveld/ProgProj/blob/master/doc/screenshot.png"/>


## Technical design
![sketch](https://github.com/romanlakerveld/ProgProj/blob/master/doc/reportsketchfinal.bmp)
### Activities
- MainActivity: this activity class handles the main screen. It contains a button to navigate
 to the SearchMashup activity and contains an EditText and a button to search information on
 a single species.
    - Calls getAllTaxa from the DatabaseAccess class to get all taxa from the SQL table to use
    for the autocompleteEditText
- SearchMashup: this activity contains the parameters part of the search fucntionality.
    - Similarly to MainActivity, this activity calls getAllTaxa to fill the AutocompleteTexts.
    - Also uses a StringRequest to get all supported interaction types from the GloBI API.
    - Uses Google Maps plugin to allow the user to specify an area to search in, the map of which
    can be hidden by clicking the "Allow area search" button.
- ActionResultsActivity: this activity displays the found interactions when given certain parameters.
    - Calls HandleParametersFromIntent to get the search parameters from the intent and build an url with them.
    - Secondly, calls GetInteractionsFromURL to call the API with the previously stated URL, then puts those interactions in a ListView with the InteractionAdapter.
- ResultsActivity: this activity takes a single species and requests the SQL database for relevant URLs.
    - First calls getIDsOfSpecies to get IDs of the specified species (which looks like this: EOL:2847123)
    - Secondly calls convertIDsToURLs, which loops over all the recieved IDs and converts them to usable URLS.
### Helper classes
- InterActionAdapter: this adapter is used by ActionResultsActivity to display interactions in a ListView.
    - Every view contains two buttons, which represent the species in the interaction, it also sets the OnSpeciesClicked listener on both buttons.
    - Uses the ViewHolder class to manage recycling the views.
- ViewHolder: holds the view for the adapter
- OnSpeciesClicked: listener that's used by the InteractionAdapter
    - Has OnClick method which get the text of the clicked button (the species) and hands it to the ResultsActivity

