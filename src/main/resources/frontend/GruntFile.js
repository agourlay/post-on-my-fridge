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
    }
  });

  grunt.loadNpmTasks('grunt-ember-templates');

  // Default task(s).
  grunt.registerTask('default', ['emberTemplates']);

};