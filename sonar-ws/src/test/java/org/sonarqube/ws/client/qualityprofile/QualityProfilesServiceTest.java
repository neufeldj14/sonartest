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
package org.sonarqube.ws.client.qualityprofile;

import org.junit.Rule;
import org.junit.Test;
import org.sonarqube.ws.QualityProfiles;
import org.sonarqube.ws.client.GetRequest;
import org.sonarqube.ws.client.ServiceTester;
import org.sonarqube.ws.client.WsConnector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class QualityProfilesServiceTest {

  public static final String LANGUAGE_VALUE = "language value";
  public static final String PROFILE_NAME_VALUE = "profile name value";
  public static final String PROFILE_KEY_VALUE = "profile key value";
  @Rule
  public ServiceTester<QualityProfilesService> serviceTester = new ServiceTester<>(new QualityProfilesService(mock(WsConnector.class)));

  private QualityProfilesService underTest = serviceTester.getInstanceUnderTest();

  @Test
  public void search_does_GET_with_single_param_on_Ws_search_when_request_is_empty() {
    underTest.search(new SearchWsRequest());

    assertThat(serviceTester.getGetParser()).isSameAs(QualityProfiles.SearchWsResponse.parser());
    GetRequest getRequest = serviceTester.getGetRequest();
    serviceTester.assertThat(getRequest)
        .hasPath("search")
        .hasParam("defaults", "false")
        .andNoOtherParam();
  }

  @Test
  public void search_does_GET_on_Ws_search() {
    underTest.search(new SearchWsRequest()
            .setDefaults(true)
            .setLanguage(LANGUAGE_VALUE)
            .setProfileName(PROFILE_NAME_VALUE)
            .setProjectKey(PROFILE_KEY_VALUE)
    );

    assertThat(serviceTester.getGetParser()).isSameAs(QualityProfiles.SearchWsResponse.parser());
    GetRequest getRequest = serviceTester.getGetRequest();
    serviceTester.assertThat(getRequest)
        .hasPath("search")
        .hasParam("defaults", "true")
        .hasParam("language", LANGUAGE_VALUE)
        .hasParam("profileName", PROFILE_NAME_VALUE)
        .hasParam("projectKey", PROFILE_KEY_VALUE)
        .andNoOtherParam();
  }
}
