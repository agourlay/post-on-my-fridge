# Post On My Fridge

*This is a small project i made to learn how to deploy a web application on google Paas (Google App Engine) .*

## Features 

The application allows any user to let a message on a fridge :D.
There is no authentication, everyone can add or delete stuff.

Just fill out the form on the right side to create a post. If you copy/paste in the content a link to a website, a youtube video or a picture, it will be parsed to adapt the display.

Then you can move the post around by drag and drop on the fridge. To delete a post, just drop it in the trashbin.   

## Technical infos

The server side is written in java and hosted on google App Engine.

The client side uses a lot of javascript to display and move objects around. It uses Jquery and Jquery UI.

The position of a post on the fridge is saved in db using ajax requests everytime it changes. 

## How to use it

Deploy it on your Google App Engine account or test it there www.post-on-my-fridge.appspot.com

## Futur improvements

* Fix timestamp value (client vs server local).
* Fix regexp to detect url.
* Fix display for different resolution (remove most of fixed sizes).
* Possibility to choose the color of the post (http://acko.net/dev/farbtastic and save color in db, then color PNG file).
* Read youtube video on the post (extract video Id then http://apiblog.youtube.com/2010/07/new-way-to-embed-youtube-videos.html).
* Display a Rss feed on a post
* Display a Twitter feed on a post. 
* Add funny magnets to move around (put a magnet on a post to lock it?).
* Add post with due date (use Jquery Datepicker and save it in db, then erase it when date expires).
* Refresh fridge with Ajax (use Jquery .load() to refresh only the content of the fridge). 
* Redesign background (some cool kitchen wall?).
* Mavenize the project (https://code.google.com/p/maven-gae-plugin/).
* Update to App Engine 1.5.3 (http://googleappengine.blogspot.com/2011/08/app-engine-153-sdk-released.html).
* Think about the creation of account, 1 user = 1 fridge.(use google account authentication).
