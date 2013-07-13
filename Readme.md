#post-on-my-fridge  

![ScreenShot](https://travis-ci.org/agourlay/post-on-my-fridge.png)

> Just sticky notes on virtual fridges shared in real time.

You can **[try it](http://fridge.arnaud-gourlay.info)**

![ScreenShot](http://fridge.arnaud-gourlay.info/images/demo.jpeg)

### Architecture

A single page web app built with Ember.js displays the content of a fridge and subscribes to notifications for the current fridge.

The backend is a non blocking rest API that reads and modifies fridges. It is built with Spray.io and Slick for persistence. When an action such as creating or moving a post is called on a fridge, a notification about the new state is sent via Server Sent Event to all the current fridge´s clients. 

This technique allows near real time collaboration on a fridge.


