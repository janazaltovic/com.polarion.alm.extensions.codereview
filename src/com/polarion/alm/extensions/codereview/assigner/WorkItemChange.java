/*
 * Copyright 2016 Polarion AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.polarion.alm.extensions.codereview.assigner;

import java.time.LocalDate;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.polarion.alm.tracker.model.IStatusOpt;
import com.polarion.alm.tracker.model.IWorkItem;

@SuppressWarnings("nls")
final class WorkItemChange {
    private final @NotNull IWorkItem historicalWorkItem;
    private final @NotNull String revisionName;
    private final @Nullable LocalDate changeDate;
    private final @Nullable String changeAuthor;
    private final @Nullable String currentStatus;
    private final @Nullable String previousStatus;

    WorkItemChange(@NotNull IWorkItem historicalWorkItem, @NotNull String revisionName, @Nullable LocalDate changeDate, @Nullable String changeAuthor, @Nullable IWorkItem olderHistoricalWorkItem) {
        this.historicalWorkItem = historicalWorkItem;
        this.revisionName = revisionName;
        this.changeDate = changeDate;
        this.changeAuthor = changeAuthor;
        currentStatus = getStatus(historicalWorkItem);
        previousStatus = getStatus(olderHistoricalWorkItem);
    }

    @NotNull
    IWorkItem getHistoricalWorkItem() {
        return historicalWorkItem;
    }

    @Nullable
    String getChangeAuthor() {
        return changeAuthor;
    }

    boolean wasChangedOn(@NotNull LocalDate date) {
        if (changeDate != null) {
            return changeDate.equals(date);
        }
        return false;
    }

    boolean wasChangedEarlierThan(@NotNull LocalDate date) {
        if (changeDate != null) {
            return changeDate.isBefore(date);
        }
        return false;
    }

    boolean wasStatusChangedFrom(@Nullable String status) {
        return Objects.equals(previousStatus, status) && !Objects.equals(previousStatus, currentStatus);
    }

    private static @Nullable String getStatus(@Nullable IWorkItem workItem) {
        if (workItem == null) {
            return null;
        }
        IStatusOpt status = workItem.getStatus();
        return (status != null) ? status.getId() : null;
    }

    public @NotNull String describe() {
        return revisionName + " (" + changeDate + ") by " + changeAuthor + " with status changed from " + previousStatus + " to " + currentStatus;
    }

    @Override
    public String toString() {
        return String.format("historicalWorkItem.id: %s, revisionName: %s, changeDate: %s, changeAuthor: %s, previousStatus: %s, currentStatus; %s", historicalWorkItem.getId(), revisionName, changeDate, changeAuthor, previousStatus,
                currentStatus);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((changeAuthor != null) ? changeAuthor.hashCode() : 0);
        result = prime * result + ((changeDate != null) ? changeDate.hashCode() : 0);
        result = prime * result + historicalWorkItem.hashCode();
        result = prime * result + revisionName.hashCode();
        result = prime * result + ((currentStatus != null) ? currentStatus.hashCode() : 0);
        result = prime * result + ((previousStatus != null) ? previousStatus.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorkItemChange)) {
            return false;
        }
        WorkItemChange other = (WorkItemChange) obj;
        return Objects.equals(changeAuthor, other.changeAuthor)
                && Objects.equals(changeDate, other.changeDate)
                && Objects.equals(historicalWorkItem, other.historicalWorkItem)
                && Objects.equals(revisionName, other.revisionName)
                && Objects.equals(currentStatus, other.currentStatus)
                && Objects.equals(previousStatus, other.previousStatus);
    }

}