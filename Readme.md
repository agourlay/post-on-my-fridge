# Post On My Fridge

*This is a project i made to have fun with javascript and google Paas (Google App Engine).*

## Features 

The application allows any user to leave a message on a fridge :D.

There is no authentication, everyone can add or delete stuff.

You can insert an url in the content of a post:

* a simple url to a website will create a href link.

* a link to a gif/jpeg/png will display the picture in the post.

* a youtube video will embed a video player in the post.

* a link to a twitter account will display the last tweet. (ex : http://twitter.com/#!/BarackObama ).

* a link to a rss feed will display the last feed. (ex: http://blablabla.xml or http://blabla/feed(s)/ or http://blabla.....rss ).

You can move posts around by drag and drop. To delete a post, just drop it in the trashbin.   

More features to come...

## Technical infos

The server side is written in java and hosted on Google App Engine.

The client side uses a lot of javascript to build all the  interactions. (Jquery / Jquery UI).

All the content is built dynamically on the client from a JSON object requested every 2 minutes from the server.


## Donation

If you like the application [![Flattr this git repo](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=shagaan&url=https://github.com/shagaan&title=PostOnMyFridge&language=en_GB&tags=github&category=software)
