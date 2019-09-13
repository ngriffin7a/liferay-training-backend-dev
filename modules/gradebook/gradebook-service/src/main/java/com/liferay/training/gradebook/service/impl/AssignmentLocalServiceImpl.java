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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.base.AssignmentLocalServiceBaseImpl;
import com.liferay.training.gradebook.service.validation.AssignmentValidator;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the assignment local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.training.gradebook.service.AssignmentLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssignmentLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.training.gradebook.model.Assignment",
	service = AopService.class
)
public class AssignmentLocalServiceImpl extends AssignmentLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	public Assignment addAssignment(
			Assignment assignment, ServiceContext serviceContext)
		throws PortalException {

		// Validate assignment parameters.

		AssignmentValidator.validate(
			assignment.getTitleMap(), assignment.getDescription(),
			assignment.getDueDate());

		// Get group and same time validate that it exists.

		Group group = serviceContext.getScopeGroup();

		long userId = serviceContext.getUserId();

		User user = userLocalService.getUser(userId);

		// Generate primary key for the assignment.

		assignment.setAssignmentId(
			counterLocalService.increment(Assignment.class.getName()));

		// Fill the assignment

		assignment.setCompanyId(group.getCompanyId());
		assignment.setGroupId(group.getGroupId());
		assignment.setUserId(userId);

		assignment.setStatus(WorkflowConstants.STATUS_DRAFT);
		assignment.setStatusByUserId(userId);
		assignment.setStatusByUserName(user.getFullName());
		assignment.setStatusDate(serviceContext.getModifiedDate(null));
		assignment.setUserName(user.getScreenName());

		assignment.setCreateDate(serviceContext.getCreateDate(new Date()));
		assignment.setModifiedDate(serviceContext.getModifiedDate(new Date()));

		// Persist assignment.

		assignment = super.addAssignment(assignment);

		// Add permission resources.

		boolean portletActions = false;
		boolean addGroupPermissions = true;
		boolean addGuestPermissions = true;

		resourceLocalService.addResources(
			assignment.getCompanyId(), assignment.getGroupId(),
			assignment.getUserId(), Assignment.class.getName(),
			assignment.getAssignmentId(), portletActions, addGroupPermissions,
			addGuestPermissions);

		// Update asset.

		updateAsset(assignment, serviceContext);

		// Log assignment addition

		if (_log.isInfoEnabled()) {
			_log.info(
				"User " + serviceContext.getUserId() + " added an assignment.");
		}

		// Start workflow instance and return the assignment.

		return startWorkflowInstance(
			serviceContext.getUserId(), assignment, serviceContext);
	}

	/**
	 * Deletes assignment.
	 *
	 * @param assignment
	 * @return
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	public Assignment deleteAssignment(Assignment assignment)
		throws PortalException {

		// Delete permission resources.

		resourceLocalService.deleteResource(
			assignment, ResourceConstants.SCOPE_INDIVIDUAL);

		// Delete asset data.

		assetEntryLocalService.deleteEntry(
			Assignment.class.getName(), assignment.getAssignmentId());

		// Delete workflow instance.

		workflowInstanceLinkLocalService.deleteWorkflowInstanceLinks(
			assignment.getCompanyId(), assignment.getGroupId(),
			Assignment.class.getName(), assignment.getAssignmentId());

		// Delete the Assignment

		return super.deleteAssignment(assignment);
	}

	@Indexable(type = IndexableType.DELETE)
	public Assignment deleteAssignment(long assignmentId)
		throws PortalException {

		Assignment assignment = getAssignment(assignmentId);

		return deleteAssignment(assignment);
	}

	/**
	 * Gets assignments by keywords and status.
	 *
	 * This example uses dynamic queries.
	 *
	 * @param groupId
	 * @param keywords
	 * @param start
	 * @param end
	 * @param status
	 * @param orderByComparator
	 * @return
	 */
	public List<Assignment> getAssignmentsByKeywords(
		long groupId, String keywords, int start, int end, int status,
		OrderByComparator<Assignment> orderByComparator) {

		DynamicQuery assignmentQuery = dynamicQuery().add(
			RestrictionsFactoryUtil.eq("groupId", groupId));

		if (status != WorkflowConstants.STATUS_ANY) {
			assignmentQuery.add(RestrictionsFactoryUtil.eq("status", status));
		}

		if (Validator.isNotNull(keywords)) {
			Disjunction disjunctionQuery =
				RestrictionsFactoryUtil.disjunction();

			disjunctionQuery.add(
				RestrictionsFactoryUtil.like("title", "%" + keywords + "%"));
			disjunctionQuery.add(
				RestrictionsFactoryUtil.like(
					"description", "%" + keywords + "%"));
			assignmentQuery.add(disjunctionQuery);
		}

		return assignmentLocalService.dynamicQuery(
			assignmentQuery, start, end, orderByComparator);
	}

	/**
	 * Get assignment count by keywords and status.
	 *
	 * This example uses dynamic queries.
	 *
	 * @param groupId
	 * @param keywords
	 * @param status
	 * @return
	 */
	public long getAssignmentsCountByKeywords(
		long groupId, String keywords, int status) {

		DynamicQuery assignmentQuery = dynamicQuery().add(
			RestrictionsFactoryUtil.eq("groupId", groupId));

		if (status != WorkflowConstants.STATUS_ANY) {
			assignmentQuery.add(RestrictionsFactoryUtil.eq("status", status));
		}

		if (Validator.isNotNull(keywords)) {
			Disjunction disjunctionQuery =
				RestrictionsFactoryUtil.disjunction();

			disjunctionQuery.add(
				RestrictionsFactoryUtil.like("title", "%" + keywords + "%"));
			disjunctionQuery.add(
				RestrictionsFactoryUtil.like(
					"description", "%" + keywords + "%"));
			assignmentQuery.add(disjunctionQuery);
		}

		return assignmentLocalService.dynamicQueryCount(assignmentQuery);
	}

	public Assignment updateAssignment(
			Assignment assignment, ServiceContext serviceContext)
		throws PortalException {

		AssignmentValidator.validate(
			assignment.getTitleMap(), assignment.getDescription(),
			assignment.getDueDate());

		updateAsset(assignment, serviceContext);

		return super.updateAssignment(assignment);
	}

	/**
	 * Updates model's workflow status.
	 *
	 * @param userId
	 * @param assignmentId
	 * @param status
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public Assignment updateStatus(
			long userId, long assignmentId, int status,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);
		Assignment assignment = getAssignment(assignmentId);

		assignment.setStatus(status);
		assignment.setStatusByUserId(userId);
		assignment.setStatusByUserName(user.getFullName());
		assignment.setStatusDate(new Date());

		assignmentPersistence.update(assignment);

		if (status == WorkflowConstants.STATUS_APPROVED) {
			assetEntryLocalService.updateVisible(
				Assignment.class.getName(), assignmentId, true);
		}
		else {
			assetEntryLocalService.updateVisible(
				Assignment.class.getName(), assignmentId, false);
		}

		return assignment;
	}

	/**
	 * Starts model workflow instance.
	 *
	 * @param userId
	 * @param assignment
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 */
	protected Assignment startWorkflowInstance(
			long userId, Assignment assignment, ServiceContext serviceContext)
		throws PortalException {

		Map<String, Serializable> workflowContext = new HashMap();

		String userPortraitURL = StringPool.BLANK;
		String userURL = StringPool.BLANK;

		if (serviceContext.getThemeDisplay() != null) {
			User user = userLocalService.getUser(userId);

			userPortraitURL = user.getPortraitURL(
				serviceContext.getThemeDisplay());
			userURL = user.getDisplayURL(serviceContext.getThemeDisplay());
		}

		workflowContext.put(
			WorkflowConstants.CONTEXT_USER_PORTRAIT_URL, userPortraitURL);
		workflowContext.put(WorkflowConstants.CONTEXT_USER_URL, userURL);

		return WorkflowHandlerRegistryUtil.startWorkflowInstance(
			assignment.getCompanyId(), assignment.getGroupId(), userId,
			Assignment.class.getName(), assignment.getAssignmentId(),
			assignment, serviceContext, workflowContext);
	}

	/**
	 * Updates asset model related asset.
	 *
	 * @param assignment
	 * @param serviceContext
	 * @throws PortalException
	 */
	private void updateAsset(
			Assignment assignment, ServiceContext serviceContext)
		throws PortalException {

		assetEntryLocalService.updateEntry(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			assignment.getCreateDate(), assignment.getModifiedDate(),
			Assignment.class.getName(), assignment.getAssignmentId(),
			assignment.getUuid(), 0, serviceContext.getAssetCategoryIds(),
			serviceContext.getAssetTagNames(), true, true,
			assignment.getCreateDate(), null, null, null,
			ContentTypes.TEXT_HTML,
			assignment.getTitle(serviceContext.getLocale()),
			assignment.getDescription(), null, null, null, 0, 0,
			serviceContext.getAssetPriority());
	}

	private static final Logger _log = LoggerFactory.getLogger(
		AssignmentLocalServiceImpl.class);

}