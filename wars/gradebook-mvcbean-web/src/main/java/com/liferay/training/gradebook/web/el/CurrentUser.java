/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.training.gradebook.web.el;

/**
 * @author Neil Griffin
 */
public interface CurrentUser {

	public int getWorkflowStatus();

	public boolean isMayAddAssignment();

	public boolean isMayAddSubmission();

	public boolean isSignedIn();

	public boolean mayDeleteAssignment(long assignmentId);

	public boolean mayDeleteSubmission(long submissionId);

	public boolean mayEditAssignment(long assignmentId);

	public boolean mayEditSubmission(long submissionId);

	public boolean mayGradeSubmission(long submissionId);

	public boolean mayPermissionAssignment(long assignmentId);

	public boolean mayPermissionSubmission(long submissionId);

	public boolean mayViewAssignment(long assignmentId);

	public boolean mayViewSubmission(long submissionId);

	public boolean mayViewSubmissions(long assignmentId);

}