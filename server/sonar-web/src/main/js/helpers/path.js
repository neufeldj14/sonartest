import _ from 'underscore';


/**
 * Return a collapsed path without a file name
 * @example
 * // returns 'src/.../js/components/navigator/app/models/'
 * collapsedDirFromPath('src/main/js/components/navigator/app/models/state.js')
 * @param {string} path
 * @returns {string|null}
 */
export function collapsedDirFromPath (path) {
  var limit = 30;
  if (typeof path === 'string') {
    var tokens = _.initial(path.split('/'));
    if (tokens.length > 2) {
      var head = _.first(tokens),
          tail = _.last(tokens),
          middle = _.initial(_.rest(tokens)),
          cut = false;
      while (middle.join().length > limit && middle.length > 0) {
        middle.shift();
        cut = true;
      }
      var body = [].concat(head, cut ? ['...'] : [], middle, tail);
      return body.join('/') + '/';
    } else {
      return tokens.join('/') + '/';
    }
  } else {
    return null;
  }
}


/**
 * Return a file name for a given file path
 * * @example
 * // returns 'state.js'
 * collapsedDirFromPath('src/main/js/components/navigator/app/models/state.js')
 * @param {string} path
 * @returns {string|null}
 */
export function fileFromPath (path) {
  if (typeof path === 'string') {
    var tokens = path.split('/');
    return _.last(tokens);
  } else {
    return null;
  }
}