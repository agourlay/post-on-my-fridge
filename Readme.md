# Post On My Fridge

## Features 

The application allows any user to leave a message on a fridge :D.

There is no authentication, everyone can add or delete stuff.

To access or create a fridge use the search box or type directly the fridge name in the url.

The post can contain dynamique media content. Just insert an url in the content of a post:

* a simple url to a website will create a href link.

* a link to a gif/jpeg/png will display the picture in the post.

* a youtube video will embed a video player in the post.

* a link to a twitter account will display the last tweet. (ex : http://twitter.com/#!/BarackObama ).

* a link to a rss feed will display the last feed. (ex: http://blablabla.xml or http://blabla/feed(s)/ or http://blabla.....rss ).

You can move posts around by drag and drop. To delete a post, just drop it in the trashbin. 

It's also possible to follow the content of your fridge by RSS.

More features to come...

## Technical infos

* server side in java & hosted on Google App Engine.

* tons of JS on client side to build content dynamically from Jersey REST api.

* web application built with Gradle.

## Donation

If you like the application [![Flattr this git repo](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=shagaan&url=https://github.com/shagaan&title=PostOnMyFridge&language=en_GB&tags=github&category=software)
