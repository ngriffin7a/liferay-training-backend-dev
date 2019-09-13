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

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.view.state.SearchContainerViewState;
import com.liferay.training.gradebook.model.Submission;
import com.liferay.training.gradebook.service.SubmissionService;
import com.liferay.training.gradebook.web.model.SubmissionDTO;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.inject.Named;

import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.engine.ViewEngineContext;

import javax.portlet.RenderParameters;
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
public class SubmissionRenderController extends MasterDetailRenderController {

	@RenderMethod(portletNames = GradebookPortlet.NAME)
	@ValidateOnExecution(type = ExecutableType.NONE)
	public String prepareView(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		return prepareMasterDetail(renderRequest);
	}

	@Override
	protected String getMasterViewName() {
		return "submissions";
	}

	@Override
	protected String getRenderName() {
		return "submission";
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
			SubmissionDTO submissionDTO = new SubmissionDTO();

			RenderParameters renderParameters =
				renderRequest.getRenderParameters();

			long submissionId = GetterUtil.getLong(
				renderParameters.getValue("submissionId"));

			if (submissionId > 0) {
				Submission submission = _submissionService.getSubmission(
					submissionId);

				BeanUtils.copyProperties(submissionDTO, submission);
			}
			else {
				submissionDTO.setAssignmentId(
					GetterUtil.getLong(
						renderParameters.getValue("assignmentId")));
				submissionDTO.setSubmitDate(new Date());
			}

			_models.put("submission", submissionDTO);

			return "submission.jspx";
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);

			return null;
		}
	}

	@Override
	protected String prepareMasterView(RenderRequest renderRequest) {
		OrderByComparator<Submission> orderByComparator =
			OrderByComparatorFactoryUtil.create(
				"Submission", _searchContainerViewState.getOrderByCol(),
				"asc".equals(_searchContainerViewState.getOrderByType()));

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long scopeGroupId = themeDisplay.getScopeGroupId();

		RenderParameters renderParameters = renderRequest.getRenderParameters();

		long assignmentId = GetterUtil.getLong(
			renderParameters.getValue("assignmentId"));

		List<Submission> submissions =
			_submissionService.getSubmissionsByKeywords(
				scopeGroupId, assignmentId,
				_searchContainerViewState.getKeywords(),
				_searchContainerViewState.getStart(),
				_searchContainerViewState.getEnd(), orderByComparator);

		_models.put("submissions", submissions);

		_models.put(
			"submissionCount",
			_submissionService.getSubmissionsCountByKeywords(
				scopeGroupId, assignmentId,
				_searchContainerViewState.getKeywords()));

		return "submissions.jspx";
	}

	private static final Logger _log = LoggerFactory.getLogger(
		SubmissionRenderController.class);

	@Inject
	private Models _models;

	@Inject
	@Named("submissionSearchContainerViewState")
	private SearchContainerViewState _searchContainerViewState;

	@Inject
	@Reference
	private SubmissionService _submissionService;

	@Inject
	private ViewEngineContext _viewEngineContext;

}