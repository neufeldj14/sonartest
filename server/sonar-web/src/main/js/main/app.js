import 'babelify/polyfill';
import $ from 'jquery';
import _ from 'underscore';
import Backbone from 'backbone';
import 'whatwg-fetch';
import moment from 'moment';
import '../libs/third-party/backbone-super.js';
import './processes';
import Navigation from './nav/app';
import Router from './router';
import QualityProfilesApp from '../apps/quality-profiles/app';
import QualityGatesApp from '../apps/quality-gates/app';
import CodingRulesApp from '../apps/coding-rules/app';
import MeasuresApp from '../apps/measures/app';
import IssuesApp from '../apps/issues/app';
import ComponentIssuesApp from '../apps/component-issues/app';
import DrilldownApp from '../apps/drilldown/app';

// set the Backbone's $
Backbone.$ = $;


function requestLocalizationBundle () {
  if (!window.sonarqube.bannedNavigation) {
    return new Promise(resolve => window.requestMessages().done(resolve));
  } else {
    return Promise.resolve();
  }
}

function startNavigation () {
  if (!window.sonarqube.bannedNavigation) {
    return new Navigation().start();
  } else {
    return Promise.resolve();
  }
}

let appOptions = { el: '#content' };

function prepareAppOptions (navResponse) {
  if (navResponse) {
    appOptions.rootQualifiers = navResponse.global.qualifiers;
    if (navResponse.component) {
      appOptions.component = {
        id: navResponse.component.uuid,
        key: navResponse.component.key,
        name: navResponse.component.name,
        qualifier: _.last(navResponse.component.breadcrumbs).qualifier
      };
    }
  }
  return appOptions;
}

function getPreferredLanguage () {
  return window.navigator.languages ? window.navigator.languages[0] : window.navigator.language;
}

moment.locale(getPreferredLanguage());

window.sonarqube.appStarted = Promise.resolve()
    .then(requestLocalizationBundle)
    .then(startNavigation)
    .then(prepareAppOptions);


const URLS = {
  QUALITY_PROFILES: window.baseUrl + '/profiles',
  QUALITY_GATES: window.baseUrl + '/quality_gates',
  CODING_RULES: window.baseUrl + '/coding_rules',
  MEASURES: window.baseUrl + '/measures',
  ISSUES: window.baseUrl + '/issues',
  COMPONENT_ISSUES: window.baseUrl + '/component_issues',
  DRILLDOWN: window.baseUrl + '/drilldown/measures'
};


function startApp (app, parameters, urlRoot) {
  app.start(_.extend({ urlRoot }, appOptions));
}

let router = new Router();
router
    .on(URLS.QUALITY_PROFILES + '*inAppPath',
        parameters => startApp(QualityProfilesApp, parameters, URLS.QUALITY_PROFILES))
    .on(URLS.QUALITY_GATES + '*inAppPath',
        parameters => startApp(QualityGatesApp, parameters, URLS.QUALITY_GATES))
    .on(URLS.CODING_RULES + '*inAppPath',
        parameters => startApp(CodingRulesApp, parameters, URLS.CODING_RULES))
    .on(URLS.MEASURES + '*inAppPath',
        parameters => startApp(MeasuresApp, parameters, URLS.MEASURES))
    .on(URLS.ISSUES + '*inAppPath',
        parameters => startApp(IssuesApp, parameters, URLS.ISSUES))
    .on(URLS.COMPONENT_ISSUES + '*inAppPath',
        parameters => startApp(ComponentIssuesApp, parameters, URLS.COMPONENT_ISSUES))
    .on(URLS.DRILLDOWN + '*inAppPath',
        parameters => startApp(DrilldownApp, parameters, URLS.DRILLDOWN));

window.sonarqube.appStarted.then(() => router.start());
