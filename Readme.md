# Post On My Fridge

*This is a small project i made to have fun with javascript and learn how to deploy a web application on google Paas (Google App Engine) .*

## Features 

The application allows any user to let a message on a fridge :D.
There is no authentication, everyone can add or delete stuff.

Just fill out the form on the right side to create a post. If you copy/paste in the content a link to a website, a youtube video or a picture, it will be parsed to adapt the display.

Then you can move the post around by drag and drop on the fridge. To delete a post, just drop it in the trashbin.   

## Technical infos

The server side is written in java and hosted on google App Engine.(googleAppEngine SDK 1.5.2)

The client side uses a lot of javascript to display and move objects around.Jquery 1.6.3  and Jquery UI 1.8.16

The position of a post on the fridge is saved in db using ajax requests everytime it changes. 

The content of the fridge is refreshed automatically every 5 minutes.

## How to use it

Deploy it on your Google App Engine account or test it there www.post-on-my-fridge.appspot.com

## Futur fixes and improvements

* Fix timestamp value (client vs server local).
* Fix regexp to detect url.
* Fix display for different resolution (remove most of fixed sizes). 
* Redesign background (some cool kitchen wall?).
* Mavenize the project (https://code.google.com/p/maven-gae-plugin/).
* Update to App Engine 1.5.3 (http://googleappengine.blogspot.com/2011/08/app-engine-153-sdk-released.html).

## Futur features

* Possibility to choose the color of the post (http://acko.net/dev/farbtastic and save color in db, then color PNG file).
* Add funny magnets to move around (put a magnet on a post to lock it?).
* Add post with due date (use Jquery Datepicker and save it in db, then erase it when date expires).
* Display last article from Rss feed on a post
* Display last message from a Twitter account on a post.
* Display current played song on a LastFm account or other info (http://www.lastfm.fr/api possibility to stream the song?).
* Think about the creation of account, 1 user = 1 fridge (use google account authentication).
