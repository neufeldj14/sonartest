/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import _ from 'underscore';
import React from 'react';

import { LanguageDistribution } from './../components/language-distribution';
import { ComplexityDistribution } from './../components/complexity-distribution';
import { NclocDistribution } from '../components/ncloc-distribution';
import { getMeasures } from '../../../api/measures';
import { DetailedMeasure } from '../components/detailed-measure';
import { DomainTimeline } from '../components/domain-timeline';
import { getPeriodLabel, getPeriodDate } from './../helpers/periods';
import { TooltipsMixin } from '../../../components/mixins/tooltips-mixin';
import { filterMetrics, filterMetricsForDomains } from '../helpers/metrics';
import { DomainLeakTitle } from '../main/components';
import { translate } from '../../../helpers/l10n';


export const StructureMain = React.createClass({
  mixins: [TooltipsMixin],

  getInitialState() {
    return {
      ready: false,
      leakPeriodLabel: getPeriodLabel(this.props.component.periods, this.props.leakPeriodIndex),
      leakPeriodDate: getPeriodDate(this.props.component.periods, this.props.leakPeriodIndex)
    };
  },

  componentDidMount() {
    this.requestMeasures().then(r => {
      let measures = this.getMeasuresValues(r);
      let leak = this.getMeasuresValues(r, Number(this.props.leakPeriodIndex));
      this.setState({ ready: true, measures, leak });
    });
  },

  getMeasuresValues (measures, period) {
    let values = {};
    measures.forEach(measure => {
      const container = period ? _.findWhere(measure.periods, { index: period }) : measure;
      if (container) {
        values[measure.metric] = container.value;
      }
    });
    return values;
  },

  getMetricsForDomain() {
    return this.props.metrics
        .filter(metric => ['Size', 'Complexity', 'Documentation'].indexOf(metric.domain) !== -1)
        .map(metric => metric.key);
  },

  getMetricsForTimeline() {
    return filterMetricsForDomains(this.props.metrics, ['Size', 'Complexity', 'Documentation']);
  },

  getAllMetricsForTimeline() {
    return filterMetrics(this.props.metrics);
  },

  requestMeasures () {
    return getMeasures(this.props.component.key, this.getMetricsForDomain());
  },

  renderLoading () {
    return <div className="flex-1 text-center">
      <i className="spinner spinner-margin"/>
    </div>;
  },

  renderLegend () {
    return <DomainLeakTitle inline={true} label={this.state.leakPeriodLabel} date={this.state.leakPeriodDate}/>;
  },

  renderOtherMeasures(domain, hiddenMetrics) {
    let metrics = filterMetricsForDomains(this.props.metrics, [domain])
        .filter(metric => hiddenMetrics.indexOf(metric.key) === -1)
        .map(metric => {
          return <DetailedMeasure key={metric.key}
                                  {...this.props}
                                  {...this.state}
                                  metric={metric.key}
                                  type={metric.type}/>;
        });
    return <div>{metrics}</div>;
  },

  renderOtherSizeMeasures() {
    return this.renderOtherMeasures('Size', ['ncloc']);
  },

  renderOtherComplexityMeasures() {
    return this.renderOtherMeasures('Complexity',
        ['complexity', 'function_complexity', 'file_complexity', 'class_complexity']);
  },

  renderOtherDocumentationMeasures() {
    return this.renderOtherMeasures('Documentation', []);
  },

  renderLanguageDistribution() {
    let distribution = this.state.measures['ncloc_language_distribution'];
    if (distribution == null) {
      return null;
    }
    return <LanguageDistribution lines={Number(this.state.measures['ncloc'])} distribution={distribution}/>;
  },

  renderComplexityDistribution(distribution, props) {
    if (distribution == null) {
      return null;
    }
    return <ComplexityDistribution distribution={distribution} {...props}/>;
  },

  renderComplexityCard() {
    if (this.state.measures['complexity'] == null) {
      return null;
    }

    return <div className="overview-detailed-layout-column">
      <div className="overview-detailed-measures-list">
        <DetailedMeasure {...this.props}
                         {...this.state}
                         metric="complexity"
                         type="INT"/>
        <DetailedMeasure {...this.props}
                         {...this.state}
                         metric="function_complexity"
                         type="FLOAT">
          {this.renderComplexityDistribution(this.state.measures['function_complexity_distribution'],
              { of: 'function' })}
        </DetailedMeasure>
        <DetailedMeasure {...this.props}
                         {...this.state}
                         metric="file_complexity"
                         type="FLOAT">
          {this.renderComplexityDistribution(this.state.measures['file_complexity_distribution'],
              { of: 'file' })}
        </DetailedMeasure>
        <DetailedMeasure {...this.props}
                         {...this.state}
                         metric="class_complexity"
                         type="FLOAT"/>
        {this.renderOtherComplexityMeasures()}
      </div>
    </div>;
  },

  render () {
    if (!this.state.ready) {
      return this.renderLoading();
    }
    return <div className="overview-detailed-page">
      <div className="overview-card">
        <div className="overview-card-header">
          <div className="overview-title">{translate('overview.domain.structure')}</div>
          {this.renderLegend()}
        </div>

        <div className="overview-detailed-layout-size">
          <div className="overview-detailed-layout-column">
            <div className="overview-detailed-measures-list">
              <DetailedMeasure {...this.props}
                               {...this.state}
                               metric="ncloc"
                               type="INT">
                {this.renderLanguageDistribution()}
              </DetailedMeasure>
              {this.renderOtherSizeMeasures()}
            </div>
          </div>

          {this.renderComplexityCard()}

          <div className="overview-detailed-layout-column">
            <div className="overview-detailed-measures-list">
              {this.renderOtherDocumentationMeasures()}
            </div>
          </div>
        </div>
      </div>

      <div className="overview-cards-list">
        <div className="overview-card">
          <DomainTimeline {...this.props} {...this.state}
              initialMetric="ncloc"
              metrics={this.getMetricsForTimeline()}
              allMetrics={this.getAllMetricsForTimeline()}/>
        </div>
        <div className="overview-card">
          <NclocDistribution {...this.props}/>
        </div>
      </div>
    </div>;

  }
});
