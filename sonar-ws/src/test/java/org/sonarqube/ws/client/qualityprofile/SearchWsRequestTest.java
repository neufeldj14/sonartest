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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchWsRequestTest {
  private SearchWsRequest underTest = new SearchWsRequest();

  @Test
  public void getDefaults_returns_false_on_newly_created_instance() {
    assertThat(underTest.getDefaults()).isFalse();
  }

  @Test
  public void getDefaults_returns_argument_of_setDefaults() {
    underTest.setDefaults(true);
    assertThat(underTest.getDefaults()).isTrue();
    underTest.setDefaults(false);
    assertThat(underTest.getDefaults()).isFalse();
  }

  @Test
  public void getLanguage_returns_null_on_newly_created_instance() {
    assertThat(underTest.getLanguage()).isNull();
  }

  @Test
  public void setLanguage_accepts_null_as_an_argument() {
    underTest.setLanguage(null);
  }

  @Test
  public void getLanguage_returns_argument_of_setLanguage() {
    String language = "toto";
    underTest.setLanguage(language);

    assertThat(underTest.getLanguage()).isEqualTo(language);
  }

  @Test
  public void getProfileName_returns_null_on_newly_created_instance() {
    assertThat(underTest.getProfileName()).isNull();
  }

  @Test
  public void setProfileName_accepts_null_as_an_argument() {
    underTest.setProfileName(null);
  }

  @Test
  public void getProfileName_returns_argument_of_setProfileName() {
    String profileName = "tutu";
    underTest.setProfileName(profileName);

    assertThat(underTest.getProfileName()).isEqualTo(profileName);
  }

  @Test
  public void getProjectKey_returns_null_on_newly_created_instance() {
    assertThat(underTest.getProjectKey()).isNull();
  }

  @Test
  public void setProjectKey_accepts_null_as_an_argument() {
    underTest.setProjectKey(null);
  }

  @Test
  public void getProjectKey_returns_argument_of_setProjectKey() {
    String profileKey = "titi";
    underTest.setProjectKey(profileKey);

    assertThat(underTest.getProjectKey()).isEqualTo(profileKey);
  }

}
