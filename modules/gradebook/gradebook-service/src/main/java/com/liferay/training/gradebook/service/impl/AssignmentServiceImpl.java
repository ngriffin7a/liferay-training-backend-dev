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
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.base.AssignmentServiceBaseImpl;

import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the assignment remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.training.gradebook.service.AssignmentService</code> interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssignmentServiceBaseImpl
 */
@Component(
	property = {
		"json.web.service.context.name=gradebook",
		"json.web.service.context.path=Assignment"
	},
	service = AopService.class
)
public class AssignmentServiceImpl extends AssignmentServiceBaseImpl {

	public Assignment addAssignment(
			Assignment assignment, ServiceContext serviceContext)
		throws PortalException {

		// TODO: ADD SECURITY CHECK!

		return assignmentLocalService.addAssignment(assignment, serviceContext);
	}

	public Assignment createAssignment() {

		// TODO: ADD SECURITY CHECK!

		return assignmentLocalService.createAssignment(0);
	}

	public Assignment deleteAssignment(long assignmentId)
		throws PortalException {

		// TODO: ADD SECURITY CHECK!

		return assignmentLocalService.deleteAssignment(assignmentId);
	}

	public Assignment getAssignment(long assignmentId) throws PortalException {

		// TODO: ADD SECURITY CHECK!

		return assignmentLocalService.getAssignment(assignmentId);
	}

	public List<Assignment> getAssignmentsByKeywords(
		long groupId, String keywords, int start, int end, int status,
		OrderByComparator<Assignment> orderByComparator) {

		return assignmentLocalService.getAssignmentsByKeywords(
			groupId, keywords, start, end, status, orderByComparator);
	}

	public long getAssignmentsCountByKeywords(
		long groupId, String keywords, int status) {

		return assignmentLocalService.getAssignmentsCountByKeywords(
			groupId, keywords, status);
	}

	public Assignment updateAssignment(
			Assignment assignment, ServiceContext serviceContext)
		throws PortalException {

		// TODO: ADD SECURITY CHECK!

		return assignmentLocalService.updateAssignment(
			assignment, serviceContext);
	}

}