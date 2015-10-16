import _ from 'underscore';
import Route from 'route-parser';

export default class Router {
  constructor () {
    this.routes = [];
  }

  on (route, listener) {
    let routeMatcher = new Route(route);
    this.routes.push({ matcher: routeMatcher, listener: listener });
    return this;
  }

  start () {
    let currentRoute = window.location.pathname + window.location.search;
    let matchedRoute = this.routes.find(route => route.matcher.match(currentRoute));
    if (matchedRoute) {
      let parameters = matchedRoute.matcher.match(currentRoute);
      matchedRoute.listener(parameters);
    }
    return this;
  }
}
