# ProgrammeerProject - Design Document

## Sketch of User Interface
![sketch image](https://github.com/romanlakerveld/ProgProj/blob/master/doc/DesignV2.bmp?raw=true)

## Necessities

### API's
- Global Biotic Interaction API (https://github.com/jhpoelen/eol-globi-data/wiki/API)
  - This API has merged a lot of different databases with information on species interaction (see https://www.globalbioticinteractions.org/status.html for a list of connected databases). The API has a few methods of querying data, but the app will be mainly using the '/interaction' endpoint, since this returns the most information.
  - The '/interaction' endpoint has a few query parameters of which some are of interest to the app: 'targetTaxon', 'sourceTaxon', 'typeInteraction' and 'bbox'.
- Encyclopedia of Life API (http://eol.org/api)
  - This API will be used to search for species without wanting to know about interactions. The '/search' endpoint takes a search argument and will look for pages on the Encyclopedia of Life that contain the argument.
