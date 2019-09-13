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

package com.liferay.training.gradebook.web.asset;

import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseAssetRendererFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentLocalService;
import com.liferay.training.gradebook.service.permission.AssignmentPermissionChecker;
import com.liferay.training.gradebook.web.annotation.ServicePortlet;
import com.liferay.training.gradebook.web.controller.GradebookPortlet;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import org.osgi.service.cdi.annotations.Reference;
import org.osgi.service.cdi.annotations.Service;

/**
 * @author Neil Griffin
 */
@ApplicationScoped
@Service(AssetRendererFactory.class)
@ServicePortlet(name = GradebookPortlet.NAME)
public class AssignmentAssetRendererFactory
	extends BaseAssetRendererFactory<Assignment> {

	public AssignmentAssetRendererFactory() {
		setClassName(Assignment.class.getName());
		setLinkable(true);
		setPortletId(GradebookPortlet.NAMESPACE);
		setSearchable(true);
	}

	@Override
	public AssetRenderer<Assignment> getAssetRenderer(long classPK, int type)
		throws PortalException {

		Assignment assignment = _assignmentLocalService.getAssignment(classPK);

		AssignmentAssetRenderer assignmentAssetRenderer =
			new AssignmentAssetRenderer(
				assignment, _assignmentPermissionChecker, _portal,
				_portletURLFactory);

		assignmentAssetRenderer.setAssetRendererType(type);

		return assignmentAssetRenderer;
	}

	@Override
	public String getType() {
		return "assignment";
	}

	@Inject
	@Reference
	private AssignmentLocalService _assignmentLocalService;

	@Inject
	@Reference
	private AssignmentPermissionChecker _assignmentPermissionChecker;

	@Inject
	@Reference
	private Portal _portal;

	@Inject
	@Reference
	private PortletURLFactory _portletURLFactory;

}