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
package org.sonar.db.ce;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.sonar.api.utils.internal.TestSystem2;
import org.sonar.db.DbTester;
import org.sonar.test.DbTests;

import static com.google.common.collect.FluentIterable.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.sonar.db.ce.CeQueueDto.Status.IN_PROGRESS;
import static org.sonar.db.ce.CeQueueDto.Status.PENDING;

@Category(DbTests.class)
public class CeQueueDaoTest {

  private static final long INIT_TIME = 1_450_000_000_000L;

  private TestSystem2 system2 = new TestSystem2().setNow(INIT_TIME);

  @Rule
  public DbTester db = DbTester.create(system2);

  CeQueueDao underTest = new CeQueueDao(system2);

  @Test
  public void test_insert() {
    insert("TASK_1", "PROJECT_1", PENDING);

    Optional<CeQueueDto> saved = underTest.selectByUuid(db.getSession(), "TASK_1");
    assertThat(saved.isPresent()).isTrue();
  }

  @Test
  public void test_selectByUuid() {
    insert("TASK_1", "PROJECT_1", PENDING);

    assertThat(underTest.selectByUuid(db.getSession(), "TASK_UNKNOWN").isPresent()).isFalse();
    CeQueueDto saved = underTest.selectByUuid(db.getSession(), "TASK_1").get();
    assertThat(saved.getUuid()).isEqualTo("TASK_1");
    assertThat(saved.getTaskType()).isEqualTo(CeTaskTypes.REPORT);
    assertThat(saved.getComponentUuid()).isEqualTo("PROJECT_1");
    assertThat(saved.getStatus()).isEqualTo(PENDING);
    assertThat(saved.getSubmitterLogin()).isEqualTo("henri");
    assertThat(saved.getCreatedAt()).isEqualTo(1_450_000_000_000L);
    assertThat(saved.getUpdatedAt()).isEqualTo(1_450_000_000_000L);
    assertThat(saved.getStartedAt()).isNull();
  }

  @Test
  public void test_selectByComponentUuid() {
    insert("TASK_1", "PROJECT_1", PENDING);
    insert("TASK_2", "PROJECT_1", PENDING);
    insert("TASK_3", "PROJECT_2", PENDING);

    assertThat(underTest.selectByComponentUuid(db.getSession(), "UNKNOWN")).isEmpty();
    assertThat(underTest.selectByComponentUuid(db.getSession(), "PROJECT_1")).extracting("uuid").containsOnly("TASK_1", "TASK_2");
    assertThat(underTest.selectByComponentUuid(db.getSession(), "PROJECT_2")).extracting("uuid").containsOnly("TASK_3");
  }

  @Test
  public void test_selectAllInAscOrder() {
    insert("TASK_1", "PROJECT_1", PENDING);
    insert("TASK_2", "PROJECT_1", PENDING);
    insert("TASK_3", "PROJECT_2", PENDING);

    assertThat(underTest.selectAllInAscOrder(db.getSession())).extracting("uuid").containsOnly("TASK_1", "TASK_2", "TASK_3");
  }

  @Test
  public void test_delete() {
    insert("TASK_1", "PROJECT_1", PENDING);

    underTest.deleteByUuid(db.getSession(), "UNKNOWN");
    assertThat(underTest.selectByUuid(db.getSession(), "TASK_1").isPresent()).isTrue();

    underTest.deleteByUuid(db.getSession(), "TASK_1");
    assertThat(underTest.selectByUuid(db.getSession(), "TASK_1").isPresent()).isFalse();
  }

  @Test
  public void test_resetAllToPendingStatus() throws Exception {
    insert("TASK_1", "PROJECT_1", PENDING);
    insert("TASK_2", "PROJECT_1", IN_PROGRESS);
    insert("TASK_3", "PROJECT_1", IN_PROGRESS);
    assertThat(underTest.countByStatus(db.getSession(), PENDING)).isEqualTo(1);
    assertThat(underTest.countByStatus(db.getSession(), IN_PROGRESS)).isEqualTo(2);

    underTest.resetAllToPendingStatus(db.getSession());

    assertThat(underTest.countByStatus(db.getSession(), PENDING)).isEqualTo(3);
    assertThat(underTest.countByStatus(db.getSession(), IN_PROGRESS)).isEqualTo(0);
  }

  @Test
  public void peek_none_if_no_pendings() throws Exception {
    assertThat(underTest.peek(db.getSession()).isPresent()).isFalse();

    // not pending, but in progress
    insert("TASK_1", "PROJECT_1", IN_PROGRESS);
    assertThat(underTest.peek(db.getSession()).isPresent()).isFalse();
  }

  @Test
  public void peek_oldest_pending() throws Exception {
    String uuidTask1 = "TASK_1";
    String uuidTask2 = "TASK_2";
    String selectQuery = "select uuid,status from ce_queue";

    insert(uuidTask1, "PROJECT_1", PENDING);
    system2.setNow(INIT_TIME + 3_000_000);
    insert(uuidTask2, "PROJECT_2", PENDING);

    assertThat(underTest.countAll(db.getSession())).isEqualTo(2);
    assertThat(upperizeKeys(db.select(selectQuery))).containsOnly(rowMap(uuidTask1, PENDING), rowMap(uuidTask2, PENDING));

    // peek first one
    Optional<CeQueueDto> peek = underTest.peek(db.getSession());
    assertThat(peek.isPresent()).isTrue();
    assertThat(peek.get().getUuid()).isEqualTo(uuidTask1);
    assertThat(peek.get().getStatus()).isEqualTo(IN_PROGRESS);
    assertThat(upperizeKeys(db.select(selectQuery))).containsOnly(rowMap(uuidTask1, IN_PROGRESS), rowMap(uuidTask2, PENDING));

    // peek second one
    peek = underTest.peek(db.getSession());
    assertThat(peek.isPresent()).isTrue();
    assertThat(peek.get().getUuid()).isEqualTo(uuidTask2);
    assertThat(peek.get().getStatus()).isEqualTo(IN_PROGRESS);
    assertThat(upperizeKeys(db.select(selectQuery))).containsOnly(rowMap(uuidTask1, IN_PROGRESS), rowMap(uuidTask2, IN_PROGRESS));

    // no more pendings
    assertThat(underTest.peek(db.getSession()).isPresent()).isFalse();
  }

  @Test
  public void do_not_peek_multiple_tasks_on_same_project_at_the_same_time() throws Exception {
    // two pending tasks on the same project
    insert("TASK_1", "PROJECT_1", PENDING);
    insert("TASK_2", "PROJECT_1", PENDING);

    Optional<CeQueueDto> peek = underTest.peek(db.getSession());
    assertThat(peek.isPresent()).isTrue();
    assertThat(peek.get().getUuid()).isEqualTo("TASK_1");
    assertThat(underTest.countAll(db.getSession())).isEqualTo(2);
    assertThat(underTest.countByStatus(db.getSession(), PENDING)).isEqualTo(1);
    assertThat(underTest.countByStatus(db.getSession(), IN_PROGRESS)).isEqualTo(1);

    // do not peek second task as long as the first one is in progress
    peek = underTest.peek(db.getSession());
    assertThat(peek.isPresent()).isFalse();

    // first one is finished
    underTest.deleteByUuid(db.getSession(), "TASK_1");
    peek = underTest.peek(db.getSession());
    assertThat(peek.get().getUuid()).isEqualTo("TASK_2");
  }

  private void insert(String uuid, String componentUuid, CeQueueDto.Status status) {
    CeQueueDto dto = new CeQueueDto();
    dto.setUuid(uuid);
    dto.setTaskType(CeTaskTypes.REPORT);
    dto.setComponentUuid(componentUuid);
    dto.setStatus(status);
    dto.setSubmitterLogin("henri");
    underTest.insert(db.getSession(), dto);
    db.getSession().commit();
  }

  private static Iterable<Map<String, Object>> upperizeKeys(List<Map<String, Object>> select) {
    return from(select).transform(new Function<Map<String, Object>, Map<String, Object>>() {
      @Nullable
      @Override
      public Map<String, Object> apply(Map<String, Object> input) {
        Map<String, Object> res = new HashMap<>(input.size());
        for (Map.Entry<String, Object> entry : input.entrySet()) {
          res.put(entry.getKey().toUpperCase(), entry.getValue());
        }
        return res;
      }
    });
  }

  private static Map<String, Object> rowMap(String uuid, CeQueueDto.Status status) {
    return ImmutableMap.<String, Object>of("UUID", uuid, "STATUS", status.name());
  }
}
