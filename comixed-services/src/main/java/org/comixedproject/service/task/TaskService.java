/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2020, The ComiXed Project.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package org.comixedproject.service.task;

import java.util.Date;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.comixedproject.model.tasks.TaskAuditLogEntry;
import org.comixedproject.model.tasks.TaskType;
import org.comixedproject.repositories.tasks.TaskAuditLogRepository;
import org.comixedproject.repositories.tasks.TaskRepository;
import org.comixedproject.service.ComiXedServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <code>TaskService</code> provides functions for working with instances of {@link Task}.
 *
 * @author Darryl L. Pierce
 */
@Service
@Log4j2
public class TaskService {
  private static final Object SEMAPHORE = new Object();

  @Autowired private TaskRepository taskRepository;
  @Autowired private TaskAuditLogRepository taskAuditLogRepository;

  /**
   * Returns the current number of records with the given task type.
   *
   * @param taskType the task type
   * @return the record count
   */
  public int getTaskCount(final TaskType taskType) {
    final int result = this.taskRepository.getTaskCount(taskType);
    log.debug("Found {} instance{} of {}", result, result == 1 ? "" : "s", taskType);
    return result;
  }

  /**
   * Return all log entries after the cutoff timestamp.
   *
   * @param cutoff the cutoff
   * @return the log entries
   * @throws ComiXedServiceException if an error occurs
   */
  public List<TaskAuditLogEntry> getAuditLogEntriesAfter(final Date cutoff)
      throws ComiXedServiceException {
    log.debug("Finding task audit log entries aftrr date: {}", cutoff);

    List<TaskAuditLogEntry> result = null;
    boolean done = false;
    long started = System.currentTimeMillis();
    while (!done) {
      result = this.taskAuditLogRepository.findAllByStartTimeGreaterThan(cutoff);

      if (result.isEmpty()) {
        log.debug("Waiting for task audit log entries");
        synchronized (SEMAPHORE) {
          try {
            SEMAPHORE.wait(1000L);
          } catch (InterruptedException error) {
            log.debug("Interrupted while getting task audit log entries", error);
            Thread.currentThread().interrupt();
          }
        }
      }

      done = !result.isEmpty() || (System.currentTimeMillis() - started > 60000);
    }

    return result;
  }
}
