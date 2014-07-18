module.exports = function(grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    emberTemplates: {
       compile: {
        options: {
          templateBasePath: /web\/templates\//,
          templateFileExtensions: /\.hbs/
        },
        files: {
          "web/dist/js/templates.js": ["web/templates/**/*.hbs"]
        }
      }
    },
    concat : {
      libjs : {
        src : [
          "web/bower_components/jquery/dist/jquery.min.js",
          "web/bower_components/bootstrap/dist/js/bootstrap.min.js",
          "web/bower_components/handlebars/handlebars.runtime.min.js",
          "web/bower_components/ember/ember.min.js",
          "web/bower_components/momentjs/min/moment.min.js",
          "web/bower_components/store.js/store.min.js",
          "web/bower_components/js-url/url.min.js",
          "web/bower_components/typeahead.js/dist/typeahead.bundle.min.js",
          "web/bower_components/bacon/dist/Bacon.min.js",
          "web/bower_components/jquery-ui/ui/minified/jquery.ui.core.min.js",
          "web/bower_components/jquery-ui/ui/minified/jquery.ui.widget.min.js",
          "web/bower_components/jquery-ui/ui/minified/jquery.ui.mouse.min.js",
          "web/bower_components/jquery-ui/ui/minified/jquery.ui.draggable.min.js",
          "web/bower_components/jquery-ui/ui/minified/jquery.ui.droppable.min.js",
          "web/bower_components/jquery-ui/ui/minified/jquery.ui.effect.min.js",
          "web/bower_components/jquery-ui/ui/minified/jquery.ui.effect-clip.min.js",
          "web/bower_components/noty/js/noty/packaged/jquery.noty.packaged.min.js",
          "web/bower_components/jquery-ui-touch-punch-improved/jquery.ui.touch-punch-improved.js",
          "web/bower_components/hammerjs/hammer.min.js"
        ],
        dest: 'web/dist/js/libs.min.js'
      },
      libcss : {
        src : [
          "web/bower_components/bootstrap/dist/css/bootstrap.min.css",
          "web/bower_components/nprogress/nprogress.css",
          "web/bower_components/font-awesome/css/font-awesome.min.css",
          "web/bower_components/alertify.js/dist/themes/alertify.bootstrap.css"
        ],
        dest : 'web/dist/css/libs.min.css'
      }
    },
    uglify: {
      js: {
        files: {
          'web/dist/js/pomf.min.js': [
            "web/dist/js/templates.js",
            "web/js/tools.js",
            "web/js/app.js",
            "web/js/dao.js",
            "web/js/router.js",
            "web/js/view/customViews.js",
            "web/js/view/mainHeaderView.js",
            "web/js/model/fridgeModel.js",
            "web/js/view/fridgeView.js",
            "web/js/model/metricsModel.js",
            "web/js/view/metricsView.js",
            "web/js/controller/fridgesController.js",
            "web/js/view/fridgesView.js",
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
          "web/dist/css/pomf.min.css" : [
            "web/css/layout.css",
            "web/css/post.css",
            "web/css/typeahead.css"
            ]
        } 
      }   
    },
    copy: {
      fonts: {
        src: 'web/bower_components/font-awesome/fonts/*',
        dest: "web/dist/fonts/",
        filter: 'isFile',
        flatten: true,
        expand: true
      },
      maps: {
        cwd : "web/bower_components/jquery/dist/",
        src: "jquery.min.map",
        dest: "web/dist/js/",
        expand: true
      }
    },
    watch: {
      files: ["web/css/**","web/js/**","web/templates/**"],
      tasks: ['default']
    }
  });

  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-ember-templates');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-copy');

  // Default task(s).
  grunt.registerTask('default', ['emberTemplates','concat','uglify','cssmin','copy' ]);
};