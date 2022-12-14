module.exports = function(grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    meta: {
      css_dev_path: 'css',
      less_dev_path: 'less',

      less_shared_path: '../../less'
    },

    less: {
      dev: {
        options: {
          compress: true,
          dumpLineNumbers: true,
          sourceMap: true,
          sourceMapFilename: '<%= meta.css_dev_path %>/mvpready-launch.min.css.map',
          sourceMapRootpath: '/'
        },
        files: { '<%= meta.css_dev_path %>/mvpready-launch.css': '<%= meta.less_dev_path %>/mvpready-launch.less' }
      }
    },

    cssmin: {
      dev: {
        files: {
          '<%= meta.css_dev_path %>/mvpready-launch.min.css': [ '<%= meta.css_dev_path %>/mvpready-launch.css' ]
        }
      }
    },

    watch: {
      dev: {
        files: ['<%= meta.less_shared_path %>/**/*.less', '<%= meta.less_dev_path %>/mvpready-launch.less'],
        tasks: ['less:dev', 'cssmin:dev']      
      }
    }

  });

  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-cssmin');

  grunt.registerTask ('default', ['watch:dev']);
}