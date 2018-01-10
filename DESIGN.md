# ProgrammeerProject - Design Document

## Sketch of User Interface
![sketch image](https://github.com/romanlakerveld/ProgProj/blob/master/doc/DesignV3.bmp?raw=true)

## Utility of activities
- OverviewActivity
  - OnClick listeners for navigation
- SpeciesSearchActivity
  - Onclick navigation and add text in search bar to Bundle.
- ActionSearchActivity
  - Two EditTexts as well as a drop-down menu which is populated by calling the Global Biotic Interactions API for all possible interaction types. 
  - On clicking search should navigate to ActionResultsActivity and provide search information in Bundle.
-ResultsActivity
  - Calls the Encyclopedia of Life to get all results for the search argument, which will be provided in the Bundle.
  - Should also handle onClick listeners which should navigate to DetailsActivity

## Necessities

### API's
- Global Biotic Interaction API (https://github.com/jhpoelen/eol-globi-data/wiki/API)
  - This API has merged a lot of different databases with information on species interaction (see https://www.globalbioticinteractions.org/status.html for a list of connected databases). The API has a few methods of querying data, but the app will be mainly using the '/interaction' endpoint, since this returns the most information.
  - The '/interaction' endpoint has a few query parameters of which some are of interest to the app: 'targetTaxon', 'sourceTaxon', 'typeInteraction' and 'bbox'.
- Encyclopedia of Life API (http://eol.org/api)
  - This API will be used to search for species without wanting to know about interactions. The '/search' endpoint takes a search argument and will look for pages on the Encyclopedia of Life that contain the argument.
### Plugin's
- Google Maps (https://www.google.nl/maps)
  - Google Maps will be used to allows users to select an area of interest and then search it for interactions.
