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
package org.sonarqube.ws.client.usertoken;

import org.junit.Rule;
import org.junit.Test;
import org.sonarqube.ws.WsUserTokens;
import org.sonarqube.ws.client.PostRequest;
import org.sonarqube.ws.client.ServiceTester;
import org.sonarqube.ws.client.WsConnector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.sonarqube.ws.client.usertoken.UserTokensWsParameters.PARAM_LOGIN;
import static org.sonarqube.ws.client.usertoken.UserTokensWsParameters.PARAM_NAME;

public class UserTokensServiceTest {

  private static final String LOGIN_VALUE = "login value";
  private static final String NAME_VALUE = "name value";

  @Rule
  public ServiceTester<UserTokensService> serviceTester = new ServiceTester<>(new UserTokensService(mock(WsConnector.class)));

  private UserTokensService underTest = serviceTester.getInstanceUnderTest();

  @Test
  public void generate_does_POST_without_params_on_Ws_generate_with_empty_request() {
    underTest.generate(new GenerateWsRequest());

    assertThat(serviceTester.getPostParser()).isSameAs(WsUserTokens.GenerateWsResponse.parser());
    PostRequest postRequest = serviceTester.getPostRequest();
    serviceTester.assertThat(postRequest)
      .hasPath("generate")
      .andNoOtherParam();
  }

  @Test
  public void generate_does_POST_on_Ws_generate() {
    underTest.generate(new GenerateWsRequest()
      .setLogin(LOGIN_VALUE)
      .setName(NAME_VALUE)
      );

    assertThat(serviceTester.getPostParser()).isSameAs(WsUserTokens.GenerateWsResponse.parser());
    PostRequest postRequest = serviceTester.getPostRequest();
    serviceTester.assertThat(postRequest)
      .hasPath("generate")
      .hasParam(PARAM_LOGIN, LOGIN_VALUE)
      .hasParam(PARAM_NAME, NAME_VALUE)
      .andNoOtherParam();
  }
}
