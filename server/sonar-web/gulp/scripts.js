var path = require('path');

var gulp = require('gulp');
var buffer = require('vinyl-buffer');
var concat = require('gulp-concat');
var gulpif = require('gulp-if');
var uglify = require('gulp-uglify');


module.exports.sonar = function (output, production) {
  return gulp.src([
        'src/main/js/libs/translate.js',
        'src/main/js/libs/third-party/jquery.js',
        'src/main/js/libs/third-party/jquery-ui.js',
        'src/main/js/libs/third-party/d3.js',
        'src/main/js/libs/third-party/underscore.js',
        'src/main/js/libs/third-party/select2.js',
        'src/main/js/libs/third-party/keymaster.js',
        'src/main/js/libs/third-party/numeral.js',
        'src/main/js/libs/third-party/numeral-languages.js',
        'src/main/js/libs/third-party/bootstrap/tooltip.js',
        'src/main/js/libs/third-party/bootstrap/dropdown.js',
        'src/main/js/libs/select2-jquery-ui-fix.js',

        'src/main/js/libs/graphics/pie-chart.js',
        'src/main/js/libs/graphics/barchart.js',
        'src/main/js/libs/sortable.js',

        'src/main/js/libs/inputs.js',
        'src/main/js/libs/jquery-isolated-scroll.js',

        'src/main/js/libs/application.js'
      ])
      .pipe(concat('sonar.js'))
      .pipe(gulpif(production, buffer()))
      .pipe(gulpif(production, uglify()))
      .pipe(gulp.dest(path.join(output, 'js')));
};
