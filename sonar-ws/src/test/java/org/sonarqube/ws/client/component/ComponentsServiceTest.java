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
package org.sonarqube.ws.client.component;

import com.google.common.collect.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.sonarqube.ws.WsComponents;
import org.sonarqube.ws.client.GetRequest;
import org.sonarqube.ws.client.ServiceTester;
import org.sonarqube.ws.client.WsConnector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ComponentsServiceTest {
  private static final ImmutableList<String> QUALIFIERS = ImmutableList.of("1", "beer", "on", "the", "wall");
  private static final int PAGE_VALUE = 6854;
  private static final int PAGE_SIZE_VALUE = 4525;
  private static final String QUERY_VALUE = "query value";

  @Rule
  public ServiceTester<ComponentsService> serviceTester = new ServiceTester<>(new ComponentsService(mock(WsConnector.class)));

  private ComponentsService underTest = serviceTester.getInstanceUnderTest();

  @Test
  public void search_does_GET_on_Ws_search() {
    underTest.search(new SearchWsRequest()
      .setQualifiers(QUALIFIERS)
      .setPage(PAGE_VALUE)
      .setPageSize(PAGE_SIZE_VALUE)
      .setQuery(QUERY_VALUE)
      );

    assertThat(serviceTester.getGetParser()).isSameAs(WsComponents.SearchWsResponse.parser());
    GetRequest getRequest = serviceTester.getGetRequest();
    serviceTester.assertThat(getRequest)
      .hasPath("search")
      .hasParam("qualifiers", QUALIFIERS.toString())
      .hasParam("p", PAGE_VALUE)
      .hasParam("ps", PAGE_SIZE_VALUE)
      .hasParam("q", QUERY_VALUE)
      .andNoOtherParam();
  }

  @Test
  public void search_does_GET_without_params_on_Ws_search_with_empty_request() {
    underTest.search(new SearchWsRequest());

    assertThat(serviceTester.getGetParser()).isSameAs(WsComponents.SearchWsResponse.parser());
    GetRequest getRequest = serviceTester.getGetRequest();
    serviceTester.assertThat(getRequest)
      .hasPath("search")
      .andNoOtherParam();
  }
}
