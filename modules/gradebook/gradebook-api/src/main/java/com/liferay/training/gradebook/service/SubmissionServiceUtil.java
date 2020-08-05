/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.training.gradebook.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for Submission. This utility wraps
 * <code>com.liferay.training.gradebook.service.impl.SubmissionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see SubmissionService
 * @generated
 */
public class SubmissionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.training.gradebook.service.impl.SubmissionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use <code>SubmissionServiceUtil</code> to access the submission remote service.
	 */
	public static com.liferay.training.gradebook.model.Submission addSubmission(
			com.liferay.training.gradebook.model.Submission submission,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addSubmission(submission, serviceContext);
	}

	public static com.liferay.training.gradebook.model.Submission
		createSubmission() {

		return getService().createSubmission();
	}

	public static com.liferay.training.gradebook.model.Submission
			deleteSubmission(long submissionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteSubmission(submissionId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.training.gradebook.model.Submission getSubmission(
			long submissionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSubmission(submissionId);
	}

	public static java.util.List
		<com.liferay.training.gradebook.model.Submission>
			getSubmissionsByKeywords(
				long groupId, long assignmentId, String keywords, int start,
				int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.training.gradebook.model.Submission>
						orderByComparator) {

		return getService().getSubmissionsByKeywords(
			groupId, assignmentId, keywords, start, end, orderByComparator);
	}

	public static long getSubmissionsCountByKeywords(
		long groupId, long assignmentId, String keywords) {

		return getService().getSubmissionsCountByKeywords(
			groupId, assignmentId, keywords);
	}

	public static com.liferay.training.gradebook.model.Submission
			updateSubmission(
				com.liferay.training.gradebook.model.Submission submission,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateSubmission(submission, serviceContext);
	}

	public static SubmissionService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<SubmissionService, SubmissionService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(SubmissionService.class);

		ServiceTracker<SubmissionService, SubmissionService> serviceTracker =
			new ServiceTracker<SubmissionService, SubmissionService>(
				bundle.getBundleContext(), SubmissionService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}