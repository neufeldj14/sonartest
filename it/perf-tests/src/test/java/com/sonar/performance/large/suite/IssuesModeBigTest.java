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
package com.sonar.performance.large.suite;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.SonarRunner;
import com.sonar.performance.PerfTestCase;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

// Can't afford multiple executions of this long test
public class IssuesModeBigTest extends PerfTestCase {

  @ClassRule
  public static Orchestrator orchestrator = LargeTestSuite.ORCHESTRATOR;

  @ClassRule
  public static TemporaryFolder temp = new TemporaryFolder();

  private static File baseProjectDir;

  @BeforeClass
  public static void beforeClass() throws IOException {
    orchestrator.resetData();
    prepareAndPublish();
  }

  @Test
  public void issues_mode_scan_big_project() throws IOException {
    File userHome = temp.newFolder();
    SonarRunner runner = createRunner();
    runner.setProperty("sonar.analysis.mode", "issues");
    runner.setProperty("sonar.scanAllFiles", "true");
    // Use a new home to start with empty cache
    runner.setProperty("sonar.userHome", userHome.getAbsolutePath());
    long start = System.currentTimeMillis();
    orchestrator.executeBuild(runner);
    long firstIssuesDuration = System.currentTimeMillis() - start;
    System.out.println("First issues analysis skipping unchanged files: " + firstIssuesDuration + "ms");

    // caches are warmed
    start = System.currentTimeMillis();
    orchestrator.executeBuild(runner);
    long secondIssuesDuration = System.currentTimeMillis() - start;
    System.out.println("Second issues analysis skipping unchanged files: " + secondIssuesDuration + "ms");

    assertDurationAround(secondIssuesDuration, 109_000L);
  }

  @Test
  public void issues_mode_scan_big_project_only_changed() throws IOException {
    File userHome = temp.newFolder();
    SonarRunner runner = createRunner();
    runner.setProperty("sonar.analysis.mode", "issues");
    // Use a new home to start with empty cache
    runner.setProperty("sonar.userHome", userHome.getAbsolutePath());
    long start = System.currentTimeMillis();
    orchestrator.executeBuild(runner);
    long firstIssuesDuration = System.currentTimeMillis() - start;
    System.out.println("First issues analysis: " + firstIssuesDuration + "ms");

    // caches are warmed
    start = System.currentTimeMillis();
    orchestrator.executeBuild(runner);
    long secondIssuesDuration = System.currentTimeMillis() - start;
    System.out.println("Second issues analysis: " + secondIssuesDuration + "ms");

    assertDurationAround(secondIssuesDuration, 69_000L);
  }

  private static void prepareAndPublish() throws IOException {
    orchestrator.getServer().provisionProject("big", "xoo-big");
    orchestrator.getServer().associateProjectToQualityProfile("big", "xoo", "one-xoo-issue-per-line");

    baseProjectDir = prepareProject();
    SonarRunner runner = createRunner();

    long start = System.currentTimeMillis();
    orchestrator.executeBuild(runner);
    long firstDuration = System.currentTimeMillis() - start;
    System.out.println("Publishing: " + firstDuration + "ms");
  }

  private static SonarRunner createRunner() {
    return SonarRunner.create()
      .setProperties(
        "sonar.projectKey", "big",
        "sonar.projectName", "xoo-big",
        "sonar.projectVersion", "1.0",
        "sonar.sources", "src",
        "sonar.showProfiling", "true")
      .setEnvironmentVariable("SONAR_RUNNER_OPTS", "-Xmx128m -server -XX:MaxPermSize=64m")
      .setProjectDir(baseProjectDir);
  }

  private static File prepareProject() throws IOException {
    File baseDir = temp.newFolder();
    File srcDir = new File(baseDir, "src");
    srcDir.mkdir();

    int nbFiles = 100;
    int lines = 10000;
    for (int nb = 1; nb <= nbFiles; nb++) {
      File xooFile = new File(srcDir, "sample" + nb + ".xoo");
      FileUtils.write(xooFile, StringUtils.repeat(StringUtils.repeat("a", 100) + "\n", lines));
    }
    return baseDir;
  }

}
