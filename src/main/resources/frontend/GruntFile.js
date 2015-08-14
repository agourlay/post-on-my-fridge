module.exports = function(grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    emberTemplates: {
       compile: {
        options: {
          templateCompilerPath: 'web/bower_components/ember/ember-template-compiler.js',
          handlebarsPath: 'node_modules/handlebars/dist/handlebars.js',
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
          "web/bower_components/ember/ember.js",
          "web/bower_components/momentjs/min/moment.min.js",
          "web/bower_components/store.js/store.min.js",
          "web/bower_components/js-url/url.min.js",
          "web/bower_components/typeahead.js/dist/typeahead.bundle.min.js",
          "web/bower_components/bacon/dist/Bacon.min.js",
          "web/bower_components/jquery-ui/jquery-ui.min.js",
          "web/bower_components/noty/js/noty/packaged/jquery.noty.packaged.min.js",
          "web/bower_components/jquery-ui-touch-punch-improved/jquery.ui.touch-punch-improved.js"
        ],
        dest: 'web/dist/js/libs.min.js'
      },
      libcss : {
        src : [
          "web/bower_components/bootstrap/dist/css/bootstrap.min.css",
          "web/bower_components/nprogress/nprogress.css",
          "web/bower_components/font-awesome/css/font-awesome.min.css"
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
            "web/js/view/*",
            "web/js/model/*",
            "web/js/controller/*"
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
      mapsJq: {
        cwd : "web/bower_components/jquery/dist/",
        src: "jquery.min.map",
        dest: "web/dist/js/",
        expand: true
      },
      mapsHam: {
        cwd : "web/bower_components/hammerjs/",
        src: "hammer.min.map",
        dest: "web/dist/js/",
        expand: true
      },
      images: {
        src: "web/images/*",
        flatten: true,
        dest: "web/dist/images/",
        expand: true
      },
      favicon: {
        src: "web/favicon.ico",
        flatten: true,
        dest: "web/dist/",
        expand: true
      }
    },
    watch: {
      files: ["web/css/**","web/js/**","web/templates/**"],
      tasks: ['default']
    },
    htmlmin: {                                     
      dist: {                                      
        options: {                                 
          removeComments: true,
          collapseWhitespace: true
        },
        files: {                                   
          'web/dist/index.html': 'web/index.html'
        }
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-ember-templates');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-htmlmin');

  // Default task(s).
  grunt.registerTask('default', ['emberTemplates','concat','uglify','cssmin','copy','htmlmin' ]);
};
