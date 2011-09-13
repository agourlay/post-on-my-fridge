# Post On My Fridge

*This is a project i made to have fun with javascript and learn how to deploy a web application on google Paas (Google App Engine) .*

## Features 

The application allows any user to let a message on a fridge :D.

There is no authentication, everyone can add or delete stuff.

Just fill out the form on the right side to create a post.

You can insert an url in the content of a post:

* a simple url to a website will create a href link.

* a link to a gif/jpeg/png will display the picture in the post.

* a youtube video will embed a video player in the post.

More to come, check the issues of the project. https://github.com/shagaan/PostOnMyFridge/issues?labels=Idea&sort=created&direction=desc&state=open&page=1

Then you can move the post around by drag and drop on the fridge. To delete a post, just drop it in the trashbin.   

## Technical infos

The server side is written in java and hosted on google App Engine. (GoogleAppEngine SDK 1.5.3).

The client side uses a lot of javascript to display and move objects around. (Jquery 1.6.3  and Jquery UI 1.8.16).


## How to use it

Deploy it on your Google App Engine account or test it there www.post-on-my-fridge.appspot.com
