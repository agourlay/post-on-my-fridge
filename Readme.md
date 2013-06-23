#post-on-my-fridge  

Just sticky notes on virtual fridges shared in real time. 

You can **[try it](http://fridge.arnaud-gourlay.info)**

This project is a sandbox that demonstrates modern technologies for modern webapps.

![ScreenShot](http://fridge.arnaud-gourlay.info/images/demo.jpeg)

Post-on-my-fridge architecture is composed from several parts.

### pomf-client

A single page web app built with Ember.js that displays the content of a fridge and allows collaborative edition in realtime.
The client calls the rest API on pomf-api and subscribes to notifications for the current fridge with pomf-pusher.

### pomf-api  

This is a non blocking rest API that reads and modify fridges. It is built with Spray.io and Slick on a Postgresql database for persistence.
When an action on a fridge such as creating or moving a post is called, a notification is sent to pomf-pusher via akka-remote to notify all the current fridge´s clients about the new state. 
This technique allows near real time collaboration on a fridge.

### pomf-pusher 

This module allows a client to subscribe to all notifications as Server Sent Event on a fridge. 
This is built using Play 2.1 and Iteratess.

### pomf-infra

This folder contains : 

* the Nginx reverse proxy configuration used to route clients between the API and the SSE pusher.

* a Gatling simulation to stress the application.

