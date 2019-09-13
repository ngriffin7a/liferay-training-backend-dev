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

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.training.gradebook.service.permission.AssignmentPermissionChecker;
import com.liferay.training.gradebook.service.permission.SubmissionPermissionChecker;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import javax.inject.Inject;
import javax.inject.Named;

import javax.portlet.RenderRequest;
import javax.portlet.annotations.PortletRequestScoped;

import org.osgi.service.cdi.annotations.Reference;

/**
 * @author Neil Griffin
 */
@ApplicationScoped
public class CurrentUserProducer {

	@Named
	@PortletRequestScoped
	@Produces
	public CurrentUser getCurrentUser(RenderRequest renderRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return new CurrentUserImpl(
			_assignmentPermissionChecker, _submissionPermissionChecker,
			themeDisplay.getScopeGroupId(), themeDisplay.getPermissionChecker(),
			themeDisplay.isSignedIn());
	}

	@Inject
	@Reference
	private AssignmentPermissionChecker _assignmentPermissionChecker;

	@Inject
	@Reference
	private SubmissionPermissionChecker _submissionPermissionChecker;

	private static class CurrentUserImpl implements CurrentUser {

		public CurrentUserImpl(
			AssignmentPermissionChecker assignmentPermissionChecker,
			SubmissionPermissionChecker submissionPermissionChecker,
			long groupId, PermissionChecker permissionChecker,
			boolean signedIn) {

			_assignmentPermissionChecker = assignmentPermissionChecker;
			_submissionPermissionChecker = submissionPermissionChecker;
			_groupId = groupId;
			_permissionChecker = permissionChecker;
			_signedIn = signedIn;
		}

		@Override
		public int getWorkflowStatus() {
			if (_workflowStatus == null) {
				if (_permissionChecker.isCompanyAdmin()) {
					_workflowStatus = WorkflowConstants.STATUS_ANY;
				}
				else {
					_workflowStatus = WorkflowConstants.STATUS_APPROVED;
				}
			}

			return _workflowStatus;
		}

		@Override
		public boolean isMayAddAssignment() {
			if (_mayAddAssignment == null) {
				_mayAddAssignment = _signedIn;

				// TODO: Not sure why, but the following line throws a
				//  NoSuchResourcePermissionException
				//  _assignmentPermissionChecker.containsTopLevel(
				//      permissionChecker, themeDisplay.getScopeGroupId(),
				//          "ADD_ASSIGNMENT");

			}

			return _mayAddAssignment;
		}

		@Override
		public boolean isMayAddSubmission() {
			if (_mayAddSubmission == null) {
				_mayAddSubmission = _signedIn;

				// TODO: Not sure why, but the following line throws a
				//  NoSuchResourcePermissionException
				//  _submissionPermissionChecker.containsTopLevel(
				//      permissionChecker, themeDisplay.getScopeGroupId(),
				//          "ADD_SUBMISSION");

			}

			return _mayAddSubmission;
		}

		@Override
		public boolean isSignedIn() {
			return _signedIn;
		}

		@Override
		public boolean mayDeleteAssignment(long assignmentId) {
			return _hasAssignnmentModelPermission("DELETE", assignmentId);
		}

		@Override
		public boolean mayDeleteSubmission(long submissionId) {
			return _hasSubmissionModelPermission("DELETE", submissionId);
		}

		@Override
		public boolean mayEditAssignment(long assignmentId) {
			return _hasAssignnmentModelPermission("UPDATE", assignmentId);
		}

		@Override
		public boolean mayEditSubmission(long submissionId) {
			return _hasSubmissionModelPermission("UPDATE", submissionId);
		}

		@Override
		public boolean mayGradeSubmission(long submissionId) {
			return _hasSubmissionModelPermission("GRADE", submissionId);
		}

		@Override
		public boolean mayPermissionAssignment(long assignmentId) {
			return _hasAssignnmentModelPermission("PERMISSIONS", assignmentId);
		}

		@Override
		public boolean mayPermissionSubmission(long submissionId) {
			return _hasSubmissionModelPermission("PERMISSIONS", submissionId);
		}

		@Override
		public boolean mayViewAssignment(long assignmentId) {
			return _hasAssignnmentModelPermission("VIEW", assignmentId);
		}

		@Override
		public boolean mayViewSubmission(long submissionId) {
			return _hasSubmissionModelPermission("VIEW", submissionId);
		}

		@Override
		public boolean mayViewSubmissions(long assignmentId) {
			return _hasAssignnmentModelPermission(
				"VIEW_SUBMISSIONS", assignmentId);
		}

		private boolean _hasAssignnmentModelPermission(
			String actionKey, long assignmentId) {

			if ((assignmentId > 0) &&
				_assignmentPermissionChecker.contains(
					_permissionChecker, _groupId, assignmentId, actionKey)) {

				return true;
			}

			return false;
		}

		private boolean _hasSubmissionModelPermission(
			String actionKey, long submissionId) {

			if ((submissionId > 0) &&
				_submissionPermissionChecker.contains(
					_permissionChecker, _groupId, submissionId, actionKey)) {

				return true;
			}

			return false;
		}

		private AssignmentPermissionChecker _assignmentPermissionChecker;
		private long _groupId;
		private Boolean _mayAddAssignment;
		private Boolean _mayAddSubmission;
		private PermissionChecker _permissionChecker;
		private boolean _signedIn;
		private SubmissionPermissionChecker _submissionPermissionChecker;
		private Integer _workflowStatus;

	}

}