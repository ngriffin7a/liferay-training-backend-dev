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

import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.asset.util.AssetHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.permission.AssignmentPermissionChecker;
import com.liferay.training.gradebook.web.controller.GradebookPortlet;

import java.lang.reflect.InvocationTargetException;

import java.util.Locale;

import javax.portlet.MutableRenderParameters;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Neil Griffin
 */
public class AssignmentAssetRenderer extends BaseJSPAssetRenderer<Assignment> {

	public AssignmentAssetRenderer(
		Assignment assignment,
		AssignmentPermissionChecker assignmentPermissionChecker, Portal portal,
		PortletURLFactory portletURLFactory) {

		_assignment = assignment;
		_assignmentPermissionChecker = assignmentPermissionChecker;
		_portal = portal;
		_portletURLFactory = portletURLFactory;
	}

	@Override
	public Assignment getAssetObject() {
		return _assignment;
	}

	@Override
	public String getClassName() {
		return Assignment.class.getName();
	}

	@Override
	public long getClassPK() {
		return _assignment.getAssignmentId();
	}

	@Override
	public long getGroupId() {
		return _assignment.getGroupId();
	}

	@Override
	public String getJspPath(
		HttpServletRequest httpServletRequest, String template) {

		return "/WEB-INF/views/asset/" + template + ".jspx";
	}

	@Override
	public int getStatus() {
		return _assignment.getStatus();
	}

	@Override
	public String getSummary(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		int abstractLength = AssetHelper.ASSET_ENTRY_ABSTRACT_LENGTH;

		if (portletRequest != null) {
			abstractLength = GetterUtil.getInteger(
				portletRequest.getAttribute(
					WebKeys.ASSET_ENTRY_ABSTRACT_LENGTH),
				AssetHelper.ASSET_ENTRY_ABSTRACT_LENGTH);
		}

		return HtmlUtil.stripHtml(
			StringUtil.shorten(_assignment.getDescription(), abstractLength));
	}

	@Override
	public String getTitle(Locale locale) {
		return _assignment.getTitle(locale);
	}

	@Override
	public PortletURL getURLEdit(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		return _getRenderURL(liferayPortletRequest, "edit");
	}

	@Override
	public String getURLViewInContext(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			String noSuchEntryRedirect)
		throws Exception {

		RenderURL renderURL = _getRenderURL(liferayPortletRequest, "view");

		return renderURL.toString();
	}

	@Override
	public long getUserId() {
		return _assignment.getUserId();
	}

	@Override
	public String getUserName() {
		return _assignment.getUserName();
	}

	@Override
	public String getUuid() {
		return _assignment.getUuid();
	}

	@Override
	public boolean hasEditPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return _assignmentPermissionChecker.contains(
			permissionChecker, _assignment.getGroupId(),
			_assignment.getAssignmentId(), ActionKeys.UPDATE);
	}

	@Override
	public boolean hasViewPermission(PermissionChecker permissionChecker)
		throws PortalException {

		return _assignmentPermissionChecker.contains(
			permissionChecker, _assignment.getGroupId(),
			_assignment.getAssignmentId(), ActionKeys.VIEW);
	}

	@Override
	public boolean include(
			HttpServletRequest request, HttpServletResponse response,
			String template)
		throws Exception {

		_prepareModel(request);

		return super.include(request, response, template);
	}

	private RenderURL _getRenderURL(
		LiferayPortletRequest liferayPortletRequest, String viewId) {

		Group group = (Group)liferayPortletRequest.getAttribute(
			WebKeys.ASSET_RENDERER_FACTORY_GROUP);

		RenderURL renderURL = (RenderURL)_portal.getControlPanelPortletURL(
			liferayPortletRequest, group, GradebookPortlet.NAMESPACE, 0,
			LayoutConstants.DEFAULT_PLID, PortletRequest.RENDER_PHASE);

		MutableRenderParameters urlRenderParameters =
			renderURL.getRenderParameters();

		urlRenderParameters.setValue(
			"assignmentId", String.valueOf(_assignment.getAssignmentId()));
		urlRenderParameters.setValue("javax.portlet.render", "assignment");
		urlRenderParameters.setValue("viewId", viewId);

		urlRenderParameters.setValue(
			"backURL", _portal.getCurrentURL(liferayPortletRequest));

		return renderURL;
	}

	private void _prepareModel(HttpServletRequest request)
		throws IllegalAccessException, InvocationTargetException {

		int abstractLength = GetterUtil.getInteger(
			request.getAttribute(WebKeys.ASSET_ENTRY_ABSTRACT_LENGTH),
			AssetHelper.ASSET_ENTRY_ABSTRACT_LENGTH);

		request.setAttribute("assignmentDescription",
			HtmlUtil.stripHtml(
				StringUtil.shorten(
					_assignment.getDescription(), abstractLength)));

		request.setAttribute("assignmentTitle",
			HtmlUtil.stripHtml(_assignment.getTitle(request.getLocale())));
	}

	private Assignment _assignment;
	private AssignmentPermissionChecker _assignmentPermissionChecker;
	private Portal _portal;
	private PortletURLFactory _portletURLFactory;

}