# Report - InterAction
## App description
Studying interactions between species is an important part of Biology.
 The goal of this app is to allow data on these interactions to be better accessible
 and not feel as tedious as having to dig through a lot of information.
 Shown below is the way interactions are displayed:
<img height="480" width="270" src="https://github.com/romanlakerveld/ProgProj/blob/master/doc/screenshot.png"/>

## Technical design
<img src="https://github.com/romanlakerveld/ProgProj/blob/master/doc/reportsketchfinal.png"/>
### Code functionality
- MainActivity: this activity class handles the main screen. It contains a button to navigate
 to the SearchMashup activity and contains an EditText and a button to search information on
 a single species.
    - Calls getAllTaxa from the DatabaseAccess class to get all taxa from the SQL table to use
    for the autocompleteEditText
- SearchMashup: this activity contains the parameters part of the search fucntionality.
    - Similarly to MainActivity, this activity calls getAllTaxa to fill the AutocompleteTexts.
    - Also uses a StringRequest to get all supported interaction types from the GloBI API.