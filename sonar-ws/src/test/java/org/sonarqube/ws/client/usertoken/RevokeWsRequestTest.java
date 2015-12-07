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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RevokeWsRequestTest {
  private RevokeWsRequest underTest = new RevokeWsRequest();

  @Test
  public void getLogin_returns_null_on_newly_created_instance() {
    assertThat(underTest.getLogin()).isNull();
  }

  @Test
  public void setLogin_accepts_null_as_an_argument() {
    underTest.setLogin(null);
  }

  @Test
  public void getLogin_returns_argument_of_setLogin() {
    String login = "toto";
    underTest.setLogin(login);

    assertThat(underTest.getLogin()).isEqualTo(login);
  }

  @Test
  public void getName_returns_null_on_newly_created_instance() {
    assertThat(underTest.getName()).isNull();
  }

  @Test
  public void setName_accepts_null_as_an_argument() {
    underTest.setName(null);
  }

  @Test
  public void getName_returns_argument_of_setName() {
    String name = "toto";
    underTest.setName(name);

    assertThat(underTest.getName()).isEqualTo(name);
  }
}
