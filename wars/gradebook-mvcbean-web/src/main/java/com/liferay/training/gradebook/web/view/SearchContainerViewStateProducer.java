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

package com.liferay.training.gradebook.web.view;

import com.liferay.portlet.view.state.SearchContainerViewState;
import com.liferay.portlet.view.state.SearchContainerViewStateFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import javax.inject.Inject;
import javax.inject.Named;

import javax.portlet.RenderRequest;
import javax.portlet.annotations.PortletRequestScoped;

import org.osgi.service.cdi.annotations.Reference;

/**
 * @author Neil Griffin
 */
@ApplicationScoped
public class SearchContainerViewStateProducer {

	@Named("assignmentSearchContainerViewState")
	@PortletRequestScoped
	@Produces
	public SearchContainerViewState getAssignmentSearchContainerViewState(
		RenderRequest renderRequest) {

		return _searchContainerViewStateFactory.create(
			"list", "title", "asc", renderRequest,
			new String[] {"createDate", "title"});
	}

	@Named("submissionSearchContainerViewState")
	@PortletRequestScoped
	@Produces
	public SearchContainerViewState getSubmissionSearchContainerViewState(
		RenderRequest renderRequest) {

		return _searchContainerViewStateFactory.create(
			"list", "createDate", "asc", renderRequest,
			new String[] {"createDate", "submitDate"});
	}

	@Inject
	@Reference
	private SearchContainerViewStateFactory _searchContainerViewStateFactory;

}