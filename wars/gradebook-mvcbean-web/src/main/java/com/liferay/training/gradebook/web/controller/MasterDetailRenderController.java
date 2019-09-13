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

package com.liferay.training.gradebook.web.controller;

import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.mvc.engine.ViewEngineContext;

import javax.portlet.RenderParameters;
import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Neil Griffin
 */
public abstract class MasterDetailRenderController {

	protected abstract String getMasterViewName();

	protected abstract String getRenderName();

	protected abstract String getRenderNameDefault();

	protected abstract ViewEngineContext getViewEngineContext();

	protected abstract String prepareDetailView(RenderRequest renderRequest);

	protected String prepareMasterDetail(RenderRequest renderRequest) {
		RenderParameters renderParameters = renderRequest.getRenderParameters();

		String backURL = GetterUtil.getString(
			renderParameters.getValue("backURL"));

		if (Validator.isNotNull(backURL)) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

			portletDisplay.setShowBackIcon(true);
			portletDisplay.setURLBack(backURL);
		}

		String renderName = GetterUtil.getString(
			renderParameters.getValue("javax.portlet.render"),
			getRenderNameDefault());

		if (renderName.equals(getRenderName())) {
			ViewEngineContext viewEngineContext = getViewEngineContext();

			// Get the view path that may have been by an action controller in
			// the ACTION_PHASE

			String viewPath = viewEngineContext.getView();

			// If the action controller specified a view path, then

			if (viewPath != null) {

				// If the view path contains the master view name, then prepare
				// the master view.

				if (viewPath.contains(getMasterViewName())) {
					prepareMasterView(renderRequest);
				}
			}

			// Otherwise, prepare the master or detail view according to the
			// viewId.

			else {
				String viewId = GetterUtil.getString(
					renderParameters.getValue("viewId"));

				if (viewId.equals("master")) {
					viewPath = prepareMasterView(renderRequest);
				}
				else if (viewId.equals("add") || viewId.equals("edit") ||
						 viewId.equals("view")) {

					viewPath = prepareDetailView(renderRequest);
				}
			}

			// If the view path has not been been determined, then prepare the
			// master view and determine its view path.

			if (viewPath == null) {
				viewPath = prepareMasterView(renderRequest);
			}

			_log.debug(
				"[RENDER_PHASE] prepared model for viewPath: {}", viewPath);

			return viewPath;
		}

		return null;
	}

	protected abstract String prepareMasterView(RenderRequest renderRequest);

	private static final Logger _log = LoggerFactory.getLogger(
		MasterDetailRenderController.class);

}