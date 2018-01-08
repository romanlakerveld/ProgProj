# ProgProj
Programmeerproject

# Problem statement
Studying interactions between species is an important part of Biology. These interactions can range from eating each other, helping each other (for example by pollination) or infecting each other.
Being able to quickly find species interactions would certainly help in research, especially biology students who dont want to go through a lot of papers to discover these interactions.

# Solution
The solution would be a simple app, in which you can search for species or interaction, and show the interaction as well as more information on the species associated with the interaction.

Visual sketch

### Main features
- Getting interactions between 2 species
- Getting interactions of 1 species with other species.
- Getting interactions by location
- Getting more information on the species.

The MVP would contain all of the above mentioned features, but it would be better to have all the information of the species in the result screen and not in an external browser window, although there is not yet an easy way to do that, that i'm aware of.

### Data sources:
- EOL API http://eol.org/api
- Global Biotic Interactions API https://github.com/jhpoelen/eol-globi-data/wiki/API

### External components:
- Google maps

I have not found any existing apps with the same purpose as this idea.

The hardest part will probably be setting up the communication with the API, since the returned JSON isnt always as straightforward as one would hope. Also implementing Google Maps into the app would seems like a hard obstacle, but not that hard.
