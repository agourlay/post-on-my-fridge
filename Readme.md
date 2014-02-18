#post-on-my-fridge  

Just sticky notes shared in real time on virtual fridges.

[![Build Status](https://travis-ci.org/agourlay/post-on-my-fridge.png?branch=master)](https://travis-ci.org/agourlay/post-on-my-fridge)

This application focuses on realtime interactions and simplicity. 

The backend is written in Scala using [Akka](http://akka.io/) actor's model and [Spray](http://spray.io/) for the rest/http interface. 

The frontend is a single page web app written in javascript with [Ember.js](http://emberjs.com/).

![alt text](./misc/screenshot.png "Overview")

## Architecture

A single page web app displays the content of a fridge and subscribes to all the notifications occuring on it.

When an action, such as creating or moving a post is called on a fridge, a notification enclosing the new state is pushed via Server Sent Event to all the current fridge´s clients. 

This architecture allows near real time collaboration on a fridge.

## Setup

This is full-stack application, you need SBT to build it and a postgreSQL instance to run it.

The frontend is also embedded into the spray application in the [frontend](https://github.com/agourlay/post-on-my-fridge/blob/master/src/main/resources/frontend) folder. It uses [Bower](http://bower.io/) and [Grunt](http://gruntjs.com/) for javascript build management.

The easiest way to deploy the project on a server is to build and run a fatjar.

```sh
sbt assembly
java -jar pomf-api.jar &
```
I have provided my nginx configuration in the [misc](https://github.com/agourlay/post-on-my-fridge/blob/master/misc/nginx.conf) folder, it assumes that the application runs on 127.0.0.1:8080 

## Licence

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Copyright &copy; 2013 **[Arnaud Gourlay](http://about.arnaud-gourlay.info/)**.


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/agourlay/post-on-my-fridge/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
