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
package org.sonar.server.computation.step;

import org.sonar.core.util.logs.Profiler;

/**
 * Context available during execution of a {@link ComputationStep}
 * @see ComputationStep#execute(StepContext)
 */
public class StepContext {
  private final Profiler profiler;

  public StepContext(Profiler profiler) {
    this.profiler = profiler;
  }

  /**
   * Add information to the profiler log. For example {@code addProfilerContext("issues", 100000)}
   * completes the log with the number of processed issues (see last field) :
   * <pre>2015.10.21 13:23:11 INFO  [o.s.s.c.s.ComputationStepExecutor] Persist issues | time=11226ms | issues=100000</pre>
   */
  public StepContext addProfilerContext(String key, Object value) {
    profiler.addContext(key, value);
    return this;
  }
}
