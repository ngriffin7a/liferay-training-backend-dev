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

package com.liferay.training.gradebook.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.training.gradebook.model.Submission;
import com.liferay.training.gradebook.service.base.SubmissionLocalServiceBaseImpl;

import com.liferay.training.gradebook.service.validation.SubmissionValidator;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * The implementation of the submission local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.training.gradebook.service.SubmissionLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SubmissionLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.training.gradebook.model.Submission",
	service = AopService.class
)
public class SubmissionLocalServiceImpl extends SubmissionLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>com.liferay.training.gradebook.service.SubmissionLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.training.gradebook.service.SubmissionLocalServiceUtil</code>.
	 */

	@Indexable(type = IndexableType.REINDEX)
	public Submission addSubmission(
		Submission submission, ServiceContext serviceContext)
		throws PortalException {

		// Validate submission parameters.

		SubmissionValidator.validate(
			submission.getSubmissionText(), submission.getSubmitDate());

		// Get group and same time validate that it exists.

		Group group = serviceContext.getScopeGroup();

		long userId = serviceContext.getUserId();

		User user = userLocalService.getUser(userId);

		// Generate primary key for the submission.

		submission.setSubmissionId(
			counterLocalService.increment(Submission.class.getName()));

		// Fill the submission

		submission.setCompanyId(group.getCompanyId());
		submission.setGroupId(group.getGroupId());
		submission.setUserId(userId);
		submission.setUserName(user.getScreenName());
		submission.setCreateDate(serviceContext.getCreateDate(new Date()));
		submission.setModifiedDate(serviceContext.getModifiedDate(new Date()));

		// A grade of -1 indicates that it hasn't been graded yet.
		submission.setGrade(-1);
		submission.setSubmitDate(submission.getCreateDate());
		submission.setStudentId(userId);

		// Persist submission.

		submission = super.addSubmission(submission);

		// Add permission resources.

		boolean portletActions = false;
		boolean addGroupPermissions = true;
		boolean addGuestPermissions = true;

		resourceLocalService.addResources(
			submission.getCompanyId(), submission.getGroupId(),
			submission.getUserId(), Submission.class.getName(),
			submission.getSubmissionId(), portletActions, addGroupPermissions,
			addGuestPermissions);

		// Update asset.

		updateAsset(submission, serviceContext);

		// Log submission addition

		if (_log.isInfoEnabled()) {
			_log.info(
				"User " + serviceContext.getUserId() + " added an submission.");
		}

		return submission;
	}

	public Submission updateSubmission(
		Submission submission, ServiceContext serviceContext)
		throws PortalException {

		SubmissionValidator.validate(
			submission.getSubmissionText(), submission.getSubmitDate());

		return super.updateSubmission(submission);
	}

	public List<Submission> getSubmissionsByKeywords(
		long groupId, long assignmentId, String keywords, int start, int end,
		OrderByComparator<Submission> orderByComparator) {

		DynamicQuery submissionsQuery = dynamicQuery(
		).add(
			RestrictionsFactoryUtil.eq("groupId", groupId)
		).add(
			RestrictionsFactoryUtil.eq("assignmentId", assignmentId)
		).add(
			RestrictionsFactoryUtil.like("submissionText", "%" + keywords + "%")
		);

		return dynamicQuery(submissionsQuery, start, end, orderByComparator);
	}

	public long getSubmissionsCountByKeywords(
		long groupId, long assignmentId, String keywords) {

		DynamicQuery submissionsQuery = dynamicQuery(
		).add(
			RestrictionsFactoryUtil.eq("groupId", groupId)
		).add(
			RestrictionsFactoryUtil.eq("assignmentId", assignmentId)
		).add(
			RestrictionsFactoryUtil.like("submissionText", "%" + keywords + "%")
		);

		return dynamicQueryCount(submissionsQuery);
	}


	/**
	 * Updates asset model related asset.
	 *
	 * @param submission
	 * @param serviceContext
	 * @throws PortalException
	 */
	private void updateAsset(
		Submission submission, ServiceContext serviceContext)
		throws PortalException {

		assetEntryLocalService.updateEntry(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			submission.getCreateDate(), submission.getModifiedDate(),
			Submission.class.getName(), submission.getSubmissionId(),
			submission.getUuid(), 0, serviceContext.getAssetCategoryIds(),
			serviceContext.getAssetTagNames(), true, true,
			submission.getCreateDate(), null, null, null,
			ContentTypes.TEXT_HTML,
			submission.getSubmissionText(),
			submission.getSubmissionText(), null, null, null, 0, 0,
			serviceContext.getAssetPriority());
	}

	private static final Logger _log = LoggerFactory.getLogger(
		SubmissionLocalServiceImpl.class);
}