Just sticky notes on virtual fridges that can be shared in real time.

This project is a sandbox that demonstrates modern technologies for modern webapps.

Post-on-my-fridge is composed from several parts.

* pomf-client : single page client with Ember.js
* pomf-api    : rest api with Spray & Slick, sends notification to the pusher via akka remoting
* pomf-pusher : Server Sent Event pusher with Play2
* pomf-infra  : nginx reverse proxy conf, Gatling simulations

Test instance available http://fridge.arnaud-gourlay.info
