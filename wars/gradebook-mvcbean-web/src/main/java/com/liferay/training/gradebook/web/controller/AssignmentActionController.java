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
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.model.AssignmentDTO;

import java.util.Locale;
import java.util.Map;
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
public class AssignmentActionController {

	@ActionMethod(
		actionName = "deleteAssignment", portletName = GradebookPortlet.NAME
	)
	@CsrfProtected
	public String deleteAssignment(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		try {
			_assignmentService.deleteAssignment(
				ParamUtil.getLong(actionRequest, "assignmentId"));
			ControllerUtil.addDefaultSuccessMessage(actionRequest);
		}
		catch (PortalException pe) {
			_log.error(pe.getMessage(), pe);
			ControllerUtil.addDefaultErrorMessage(actionRequest);
		}

		return "redirect:assignments.jspx";
	}

	@ActionMethod(
		actionName = "submitAssignment", portletName = GradebookPortlet.NAME
	)
	@CsrfProtected
	public String submitAssignment(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		_models.put("assignment", _assignmentDTO);

		Set<ParamError> bindingErrors = _bindingResult.getAllErrors();

		if (bindingErrors.isEmpty()) {
			if (_log.isDebugEnabled()) {
				_log.debug("[ACTION_PHASE] Submitted " + _assignmentDTO);
			}

			try {
				Map<Locale, String> titleMap =
					LocalizationUtil.getLocalizationMap(actionRequest, "title");

				if (_assignmentDTO.getAssignmentId() == 0) {
					Assignment assignment =
						_assignmentService.createAssignment();

					BeanUtils.copyProperties(assignment, _assignmentDTO);
					assignment.setTitleMap(titleMap);

					_assignmentService.addAssignment(
						assignment,
						ServiceContextFactory.getInstance(actionRequest));
				}
				else {
					Assignment assignment = _assignmentService.getAssignment(
						_assignmentDTO.getAssignmentId());

					BeanUtils.copyProperties(assignment, _assignmentDTO);
					assignment.setTitleMap(titleMap);

					_assignmentService.updateAssignment(
						assignment,
						ServiceContextFactory.getInstance(actionRequest));

					ControllerUtil.addDefaultSuccessMessage(actionRequest);
				}
			}
			catch (Exception e) {
				_log.error(e.getMessage(), e);
				ControllerUtil.addDefaultErrorMessage(actionRequest);

				return "assignment.jspx";
			}

			return "redirect:assignments.jspx";
		}

		return "assignment.jspx";
	}

	private static final Logger _log = LoggerFactory.getLogger(
		AssignmentActionController.class);

	@BeanParam
	@Inject
	@Valid
	private AssignmentDTO _assignmentDTO;

	@Inject
	@Reference
	private AssignmentService _assignmentService;

	@Inject
	private BindingResult _bindingResult;

	@Inject
	private Models _models;

}