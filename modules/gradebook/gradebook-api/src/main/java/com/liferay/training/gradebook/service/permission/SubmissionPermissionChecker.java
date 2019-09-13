
package com.liferay.training.gradebook.service.permission;

import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.permission.BaseModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.training.gradebook.model.Submission;

/**
 * Gradebook submissions permission checker interface.
 *
 * @author liferay
 *
 */
public interface SubmissionPermissionChecker
	extends BaseModelPermissionChecker {

	public static final String ADD_SUBMISSION = "ADD_SUBMISSION";

	public static final String RESOURCE_NAME = Submission.class.getName();

	public static final String TOP_LEVEL_RESOURCE = Submission.class.getName(
	).substring(
		0,
		Submission.class.getName(
		).lastIndexOf(
			"."
		)
	);

	public void check(PermissionChecker permissionChecker, long groupId, long submissionId, String actionId)
		throws AuthException;

	public void checkTopLevel(PermissionChecker permissionChecker, long groupId, String actionId)
		throws AuthException;

	public boolean contains(PermissionChecker permissionChecker, long groupId, long submissionId, String actionId);

	public boolean containsTopLevel(PermissionChecker permissionChecker, long groupId, String actionId);

}