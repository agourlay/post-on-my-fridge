#post-on-my-fridge  

> Just sticky notes on virtual fridges shared in real time.

[![Build Status](https://travis-ci.org/agourlay/post-on-my-fridge.png?branch=master)](https://travis-ci.org/agourlay/post-on-my-fridge)

You can **[try it](http://fridge.arnaud-gourlay.info)**

![ScreenShot](http://fridge.arnaud-gourlay.info/images/demo.jpeg)

## Architecture

A single page web app built with Ember.js displays the content of a fridge and subscribes to notifications for the current fridge.

The backend is a non blocking rest API built with Spray.io and Slick. When an action such as creating or moving a post is called on a fridge, a notification about the new state is sent via Server Sent Event to all the current fridge´s clients. 

This technique allows near real time collaboration on a fridge.

## Licence

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Copyright &copy; 2013 **[Arnaud Gourlay](http://about.arnaud-gourlay.info/)**.


