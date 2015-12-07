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
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchWsRequestTest {
  private SearchWsRequest underTest = new SearchWsRequest();

  @Test
  public void getQualifiers_returns_null_on_newly_created_instance() {
    assertThat(underTest.getQualifiers()).isNull();
  }

  @Test(expected = NullPointerException.class)
  public void setQualifiers_throws_NPE_if_List_argument_is_null() {
    underTest.setQualifiers(null);
  }

  @Test
  public void getQualifiers_returns_argument_of_setQualifiers() {
    List<String> qualifiers = ImmutableList.of("1", "2");

    underTest.setQualifiers(qualifiers);

    assertThat(underTest.getQualifiers()).isSameAs(qualifiers);
  }

  @Test
  public void getPage_returns_null_on_newly_create_instance() {
    assertThat(underTest.getPage()).isNull();
  }

  @Test
  public void getPage_returns_argument_of_setPage() {
    int page = 1232;
    underTest.setPage(page);

    assertThat(underTest.getPage()).isEqualTo(page);
  }

  @Test
  public void getPageSize_returns_null_on_newly_create_instance() {
    assertThat(underTest.getPageSize()).isNull();
  }

  @Test
  public void getPageSize_returns_argument_of_setPageSize() {
    int oageSiez = 1232;
    underTest.setPageSize(oageSiez);

    assertThat(underTest.getPageSize()).isEqualTo(oageSiez);
  }

  @Test
  public void getQuery_returns_null_on_newly_created_instance() {
    assertThat(underTest.getQuery()).isNull();
  }

  @Test
  public void setQuery_accepts_null_as_an_argument() {
    underTest.setQuery(null);
  }

  @Test
  public void getQuery_returns_argument_of_setQuery() {
    String login = "toto";
    underTest.setQuery(login);

    assertThat(underTest.getQuery()).isEqualTo(login);
  }
}
