/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarqube.ws.client.qualitygate;

import org.junit.Rule;
import org.junit.Test;
import org.sonarqube.ws.WsQualityGates;
import org.sonarqube.ws.client.GetRequest;
import org.sonarqube.ws.client.ServiceTester;
import org.sonarqube.ws.client.WsConnector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class QualityGatesServiceTest {

  private static final String ANALYSIS_ID_VALUE = "analysis id value";

  @Rule
  public ServiceTester<QualityGatesService> serviceTester = new ServiceTester<>(new QualityGatesService(mock(WsConnector.class)));

  private QualityGatesService underTest = serviceTester.getInstanceUnderTest();

  @Test
  public void projectStatus_does_GET_without_params_on_Ws_project_status_when_request_is_empty() {
    underTest.projectStatus(new ProjectStatusWsRequest());

    assertThat(serviceTester.getGetParser()).isSameAs(WsQualityGates.ProjectStatusWsResponse.parser());
    GetRequest getRequest = serviceTester.getGetRequest();
    serviceTester.assertThat(getRequest)
      .hasPath("project_status")
      .andNoOtherParam();
  }

  @Test
  public void projectStatus_does_GET_on_Ws_project_status() {
    underTest.projectStatus(new ProjectStatusWsRequest()
      .setAnalysisId(ANALYSIS_ID_VALUE)
      );

    assertThat(serviceTester.getGetParser()).isSameAs(WsQualityGates.ProjectStatusWsResponse.parser());
    GetRequest getRequest = serviceTester.getGetRequest();
    serviceTester.assertThat(getRequest)
      .hasPath("project_status")
      .hasParam("analysisId", ANALYSIS_ID_VALUE)
      .andNoOtherParam();
  }
}
