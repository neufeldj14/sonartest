var path = require('path');

var del = require('del');

var gulp = require('gulp');
var gutil = require('gulp-util');
var env = require('gulp-env');

var argv = require('yargs').argv;
var production = !argv.dev && !argv.fast;
var dev = !!argv.dev && !argv.fast;
var output = argv.output || './src/main/webapp';

var styles = require('./gulp/styles').styles;
var scripts = require('./gulp/scripts');

gulp.task('scripts-sonar', function () {
  return scripts.sonar(output, production);
});

gulp.task('styles', function () {
  return styles(output, production, dev);
});

gulp.task('clean', function (done) {
  del([
    path.join(output, 'js'),
    path.join(output, 'css')
  ], done);
});

gulp.task('scripts', ['scripts-sonar']);

gulp.task('build', ['clean', 'scripts', 'styles']);

gulp.task('watch', [], function () {
  gulp.watch('src/main/less/**/*.less', ['styles']);
  gutil.log(gutil.colors.bgGreen('Watching for changes...'));
});

gulp.task('default', ['build']);
