module.exports = function(grunt) {
  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    emberTemplates: {
       compile: {
        options: {
          templateBasePath: /web\/templates\//,
          templateFileExtensions: /\.hbs/
        },
        files: {
          "web/js/templates.js": ["web/templates/**/*.hbs"]
        }
      }
    },
    uglify: {
      js: {
        files: {
          'web/js/pomf.min.js': [
            "web/js/templates.js",
            "web/js/tools.js",
            "web/js/app.js",
            "web/js/jquery-ui-ember.js",
            "web/js/dao.js",
            "web/js/router.js",
            "web/js/view/customViews.js",
            "web/js/controller/mainHeaderController.js",
            "web/js/view/mainHeaderView.js",
            "web/js/model/indexModel.js",
            "web/js/controller/indexController.js",
            "web/js/view/indexView.js",
            "web/js/model/fridgeModel.js",
            "web/js/controller/fridgeController.js",
            "web/js/view/fridgeView.js",
            "web/js/model/postModel.js",
            "web/js/view/postView.js",
            "web/js/controller/postsController.js",
            "web/js/view/postsView.js",
            "web/js/model/messageModel.js",
            "web/js/view/messageView.js",
            "web/js/view/messagesView.js",
            "web/js/controller/messagesController.js",
            "web/js/view/chatInputView.js",
            "web/js/controller/chatInputController.js",
            "web/js/view/panelView.js",
            "web/js/controller/panelController.js",
            ]
        }
      }
    },
    cssmin : {
      combine: {
        files: {
          "web/css/pomf.min.css" : [
            "web/css/typeahead-custom.css",
            "web/css/layout.css",
            "web/css/post.css"
            ]
        } 
      }   
    }
  });

  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-ember-templates');

  // Default task(s).
  grunt.registerTask('default', ['emberTemplates','uglify','cssmin' ]);

};