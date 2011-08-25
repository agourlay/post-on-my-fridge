/* author : http://jquery-howto.blogspot.com/2009/05/jyoutube-jquery-youtube-thumbnail.html*/

$.extend({
  jYoutube: function( url, size ){
    if(url === null){ return ""; }

    size = (size === null) ? "big" : size;
    var vid;
    var results;

    results = url.match("[\\?&]v=([^&#]*)");

    vid = ( results === null ) ? url : results[1];

    if(size == "small"){
      return "http://img.youtube.com/vi/"+vid+"/2.jpg";
    }else {
      return "http://img.youtube.com/vi/"+vid+"/0.jpg";
    }
  }
});