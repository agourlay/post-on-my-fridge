# Post On My Fridge

*This is a project i made to have fun with javascript and learn how to deploy a web application on google Paas (Google App Engine) .*

## Features 

The application allows any user to leave a message on a fridge :D.

There is no authentication, everyone can add or delete stuff.

To create a post :

* fill out the form on the right side.

* change the color of the post as well. (optionnal)

* choose a due date using the calendar. (optionnal)

* solve the captcha.

* drag & drop your post where you want on the fridge.

You can insert an url in the content of a post:

* a simple url to a website will create a href link.

* a link to a gif/jpeg/png will display the picture in the post.

* a youtube video will embed a video player in the post.

* a link to a twitter account will display the last tweet. (ex : http://twitter.com/#!/BarackObama ).

* a link to a rss feed will display the last feed. (ex: http://blablabla.xml or http://blabla/feed(s)/ or http://blabla.....rss ).

You can move posts around by drag and drop. To delete a post, just drop it in the trashbin.   

More features to come (check the issues of the project)

## Technical infos

The server side is written in java and hosted on Google App Engine.

The client side uses a lot of javascript to build all the  interactions. (Jquery / Jquery UI).

All the content is built dynamically on the client from a JSON object requested every 5 minutes from the server.

## How to use it

Deploy it on your Google App Engine account or test it there www.post-on-my-fridge.appspot.com
