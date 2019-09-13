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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.training.gradebook.model.Submission;
import com.liferay.training.gradebook.service.SubmissionService;
import com.liferay.training.gradebook.web.model.SubmissionDTO;

import java.util.Set;

import javax.inject.Inject;

import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.binding.BindingResult;
import javax.mvc.binding.ParamError;
import javax.mvc.security.CsrfProtected;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.annotations.ActionMethod;
import javax.portlet.annotations.PortletRequestScoped;

import javax.validation.Valid;

import javax.ws.rs.BeanParam;

import org.apache.commons.beanutils.BeanUtils;

import org.osgi.service.cdi.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Neil Griffin
 */
@Controller
@PortletRequestScoped
public class SubmissionActionController {

	@ActionMethod(
		actionName = "deleteSubmission", portletName = GradebookPortlet.NAME
	)
	@CsrfProtected
	public String deleteSubmission(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		try {
			_submissionService.deleteSubmission(
				ParamUtil.getLong(actionRequest, "submissionId"));
			ControllerUtil.addDefaultSuccessMessage(actionRequest);
		}
		catch (PortalException pe) {
			_log.error(pe.getMessage(), pe);
			ControllerUtil.addDefaultErrorMessage(actionRequest);
		}

		return "redirect:submissions.jspx";
	}

	@ActionMethod(
		actionName = "submitSubmission", portletName = GradebookPortlet.NAME
	)
	@CsrfProtected
	public String submitSubmission(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		_models.put("submission", _submissionDTO);

		Set<ParamError> bindingErrors = _bindingResult.getAllErrors();

		if (bindingErrors.isEmpty()) {
			if (_log.isDebugEnabled()) {
				_log.debug("[ACTION_PHASE] Submitted " + _submissionDTO);
			}

			try {
				if (_submissionDTO.getSubmissionId() == 0) {
					Submission submission =
						_submissionService.createSubmission();

					BeanUtils.copyProperties(submission, _submissionDTO);

					_submissionService.addSubmission(
						submission,
						ServiceContextFactory.getInstance(actionRequest));
				}
				else {
					Submission submission = _submissionService.getSubmission(
						_submissionDTO.getSubmissionId());

					BeanUtils.copyProperties(submission, _submissionDTO);

					_submissionService.updateSubmission(
						submission,
						ServiceContextFactory.getInstance(actionRequest));

					ControllerUtil.addDefaultSuccessMessage(actionRequest);
				}
			}
			catch (Exception e) {
				_log.error(e.getMessage(), e);
				ControllerUtil.addDefaultErrorMessage(actionRequest);

				return "submission.jspx";
			}

			return "redirect:submissions.jspx";
		}

		return "submission.jspx";
	}

	private static final Logger _log = LoggerFactory.getLogger(
		SubmissionActionController.class);

	@Inject
	private BindingResult _bindingResult;

	@Inject
	private Models _models;

	@BeanParam
	@Inject
	@Valid
	private SubmissionDTO _submissionDTO;

	@Inject
	@Reference
	private SubmissionService _submissionService;

}