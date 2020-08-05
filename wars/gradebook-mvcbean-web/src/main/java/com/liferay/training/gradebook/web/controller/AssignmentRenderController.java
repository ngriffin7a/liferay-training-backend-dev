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

import com.liferay.frontend.taglib.liferay.ui.view.state.SearchContainerViewState;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.el.CurrentUser;
import com.liferay.training.gradebook.web.model.AssignmentDTO;
import com.liferay.training.gradebook.web.view.AssignmentEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.inject.Named;

import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.engine.ViewEngineContext;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.annotations.RenderMethod;

import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;

import org.apache.commons.beanutils.BeanUtils;

import org.osgi.service.cdi.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Neil Griffin
 */
@ApplicationScoped
@Controller
public class AssignmentRenderController extends MasterDetailRenderController {

	// Annotation ideas for Portlet 3.1 that will get rid of the need for
	// checking the "javax.portlet.render" parameter programatically:
	// @RenderMethod(renderName = "assignment", portletNames = GradebookPortlet.NAME)
	// @RenderParameters("javax.portlet.render=assignment,!javax.portlet.render")

	@RenderMethod(portletNames = GradebookPortlet.NAME)
	@ValidateOnExecution(type = ExecutableType.NONE)
	public String prepareView(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		return prepareMasterDetail(renderRequest);
	}

	@Override
	protected String getMasterViewName() {
		return "assignments";
	}

	@Override
	protected String getRenderName() {
		return "assignment";
	}

	@Override
	protected String getRenderNameDefault() {
		return "assignment";
	}

	@Override
	protected ViewEngineContext getViewEngineContext() {
		return _viewEngineContext;
	}

	@Override
	protected String prepareDetailView(RenderRequest renderRequest) {
		try {
			AssignmentDTO assignmentDTO = new AssignmentDTO();

			long assignmentId = ParamUtil.getLong(
				renderRequest, "assignmentId");

			if (assignmentId > 0) {
				Assignment assignment = _assignmentService.getAssignment(
					assignmentId);

				BeanUtils.copyProperties(assignmentDTO, assignment);
			}
			else {
				assignmentDTO.setDueDate(new Date());
			}

			_models.put("assignment", assignmentDTO);

			return "assignment.jspx";
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);

			return null;
		}
	}

	@Override
	protected String prepareMasterView(RenderRequest renderRequest) {
		OrderByComparator<Assignment> orderByComparator =
			OrderByComparatorFactoryUtil.create(
				"Assignment", _searchContainerViewState.getOrderByCol(),
				"asc".equals(_searchContainerViewState.getOrderByType()));

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<Assignment> assignmentsByKeywords =
			_assignmentService.getAssignmentsByKeywords(
				themeDisplay.getScopeGroupId(), _searchContainerViewState.getKeywords(),
				_searchContainerViewState.getStart(),
				_searchContainerViewState.getEnd(),
				_currentUser.getWorkflowStatus(), orderByComparator);

		List<Assignment> assignmentEntries = new ArrayList<>();

		for (Assignment assignment : assignmentsByKeywords) {
			assignmentEntries.add(
				new AssignmentEntry(assignment, renderRequest.getLocale()));
		}

		_models.put("assignments", assignmentEntries);

		_models.put(
			"assignmentCount",
			_assignmentService.getAssignmentsCountByKeywords(
				themeDisplay.getScopeGroupId(), _searchContainerViewState.getKeywords(),
				_currentUser.getWorkflowStatus()));

		return "assignments.jspx";
	}

	private static final Logger _log = LoggerFactory.getLogger(
		AssignmentRenderController.class);

	@Inject
	@Reference
	private AssignmentService _assignmentService;

	@Inject
	private CurrentUser _currentUser;

	@Inject
	private Models _models;

	@Inject
	@Named("assignmentSearchContainerViewState")
	private SearchContainerViewState _searchContainerViewState;

	@Inject
	private ViewEngineContext _viewEngineContext;

}