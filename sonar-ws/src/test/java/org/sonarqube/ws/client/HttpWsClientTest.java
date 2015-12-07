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
package org.sonarqube.ws.client;

import org.junit.Test;
import org.sonarqube.ws.client.component.ComponentsService;
import org.sonarqube.ws.client.issue.IssuesService;
import org.sonarqube.ws.client.permission.PermissionsService;
import org.sonarqube.ws.client.qualitygate.QualityGatesService;
import org.sonarqube.ws.client.qualityprofile.QualityProfilesService;
import org.sonarqube.ws.client.usertoken.UserTokensService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class HttpWsClientTest {
  private WsConnector wsConnector = mock(WsConnector.class);

  private HttpWsClient underTest = new HttpWsClient(wsConnector);

  @Test
  public void wsConnector_returns_constructor_argument_on_newly_created_instance() {
    assertThat(underTest.wsConnector()).isSameAs(wsConnector);
  }

  @Test
  public void permissions_returns_always_returns_the_same_instance() {
    PermissionsService permissionsService = underTest.permissions();
    assertThat(permissionsService).isNotNull();
    assertThat(underTest.permissions()).isSameAs(permissionsService);
  }

  @Test
  public void components_returns_always_returns_the_same_instance() {
    ComponentsService componentsService = underTest.components();
    assertThat(componentsService).isNotNull();
    assertThat(underTest.components()).isSameAs(componentsService);
  }

  @Test
  public void qualityProfiles_returns_always_returns_the_same_instance() {
    QualityProfilesService qualityProfilesService = underTest.qualityProfiles();
    assertThat(qualityProfilesService).isNotNull();
    assertThat(underTest.qualityProfiles()).isSameAs(qualityProfilesService);
  }

  @Test
  public void issues_returns_always_returns_the_same_instance() {
    IssuesService issuesService = underTest.issues();
    assertThat(issuesService).isNotNull();
    assertThat(underTest.issues()).isSameAs(issuesService);
  }

  @Test
  public void userTokens_returns_always_returns_the_same_instance() {
    UserTokensService userTokensService = underTest.userTokens();
    assertThat(userTokensService).isNotNull();
    assertThat(underTest.userTokens()).isSameAs(userTokensService);
  }

  @Test
  public void qualityGates_returns_always_returns_the_same_instance() {
    QualityGatesService qualityGatesService = underTest.qualityGates();
    assertThat(qualityGatesService).isNotNull();
    assertThat(underTest.qualityGates()).isSameAs(qualityGatesService);
  }
}
