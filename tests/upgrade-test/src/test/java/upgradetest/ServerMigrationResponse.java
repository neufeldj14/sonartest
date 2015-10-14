/*
 * Copyright (C) 2009-2014 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package upgradetest;

import java.util.Date;
import javax.annotation.Nullable;

class ServerMigrationResponse {
  private final Status status;
  private final String message;

  @Nullable
  private final Date date;

  ServerMigrationResponse(Status status, String message, Date date) {
    this.status = status;
    this.message = message;
    this.date = date;
  }

  public Status getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  @Nullable
  public Date getDate() {
    return date;
  }

  public enum Status {
    NO_MIGRATION, MIGRATION_NEEDED, MIGRATION_RUNNING, MIGRATION_SUCCEEDED, MIGRATION_FAILED
  }
}
