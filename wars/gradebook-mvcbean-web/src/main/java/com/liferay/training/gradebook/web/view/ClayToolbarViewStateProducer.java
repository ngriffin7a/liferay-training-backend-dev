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

import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.view.state.ClayToolbarViewState;
import com.liferay.portlet.view.state.ClayToolbarViewStateFactory;
import com.liferay.portlet.view.state.ClayToolbarViewStateWrapper;
import com.liferay.portlet.view.state.SearchContainerURLFactory;
import com.liferay.training.gradebook.web.el.CurrentUser;

import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import javax.inject.Inject;
import javax.inject.Named;

import javax.portlet.MutableRenderParameters;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderParameters;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.RenderURL;
import javax.portlet.annotations.PortletRequestScoped;

import org.osgi.service.cdi.annotations.Reference;

/**
 * @author Neil Griffin
 */
@ApplicationScoped
public class ClayToolbarViewStateProducer {

	@Named("assignmentClayToolbarViewState")
	@PortletRequestScoped
	@Produces
	public ClayToolbarViewState getAssignmentClayToolbarViewState(
		RenderRequest renderRequest, RenderResponse renderResponse,
		CurrentUser currentUser) {

		return _getClayToolbarViewState(
			renderRequest, renderResponse, currentUser, "assignment");
	}

	@Named("submissionClayToolbarViewState")
	@PortletRequestScoped
	@Produces
	public ClayToolbarViewState getSubmissionClayToolbarViewState(
		RenderRequest renderRequest, RenderResponse renderResponse,
		CurrentUser currentUser) {

		return _getClayToolbarViewState(
			renderRequest, renderResponse, currentUser, "submission");
	}

	private ClayToolbarViewState _getClayToolbarViewState(
		RenderRequest renderRequest, RenderResponse renderResponse,
		CurrentUser currentUser, String renderName) {

		ResourceBundle resourceBundle = _portletConfig.getResourceBundle(
			_portletRequest.getLocale());

		boolean assignment = renderName.equals("assignment");

		ClayToolbarViewState clayToolbarViewState =
			_clayToolbarViewStateFactory.create(
				LanguageUtil.get(
					resourceBundle, "action.ADD_" + renderName.toUpperCase()),
				"list", renderName.equals("assignment") ? "title" : "createDate", "asc",
				renderRequest, renderResponse,
				currentUser.isMayAddAssignment(), assignment, assignment, true);

		return new CustomClayToolbarViewState(
			clayToolbarViewState, renderName, resourceBundle);
	}

	@Inject
	@Reference
	private ClayToolbarViewStateFactory _clayToolbarViewStateFactory;

	@Inject
	private PortletConfig _portletConfig;

	@Inject
	private PortletRequest _portletRequest;

	@Inject
	@Reference
	private SearchContainerURLFactory _searchContainerURLFactory;

	private class CustomClayToolbarViewState
		extends ClayToolbarViewStateWrapper {

		public CustomClayToolbarViewState(
			ClayToolbarViewState clayToolbarViewState, String renderName,
			ResourceBundle resourceBundle) {

			super(clayToolbarViewState);

			RenderURL addEntryURL = clayToolbarViewState.getAddEntryURL();

			MutableRenderParameters urlRenderParameters =
				addEntryURL.getRenderParameters();

			urlRenderParameters.setValue("javax.portlet.render", renderName);
			urlRenderParameters.setValue("viewId", "add");

			RenderParameters portletRequestRenderParameters =
				_portletRequest.getRenderParameters();

			String assignmentId = GetterUtil.getString(
				portletRequestRenderParameters.getValue("assignmentId"));

			if (renderName.equals("assignment")) {
				urlRenderParameters.setValue("assignmentId", "0");
			}
			else {
				urlRenderParameters.setValue("assignmentId", assignmentId);
				urlRenderParameters.setValue("submissionId", "0");
			}

			_renderName = renderName;
			_resourceBundle = resourceBundle;

			_addURLRenderParameters(
				clayToolbarViewState.getDisplayStyleURL(), assignmentId,
				renderName);
			_addURLRenderParameters(
				clayToolbarViewState.getSortingURLCurrent(), assignmentId,
				renderName);
			_addURLRenderParameters(
				clayToolbarViewState.getSortingURLReverse(), assignmentId,
				renderName);
			_addURLRenderParameters(
				clayToolbarViewState.getSearchURL(), assignmentId, renderName);
		}

		@Override
		public CreationMenu getCreationMenu() {
			if (_creationMenu == null) {
				_creationMenu = new CreationMenu();

				_creationMenu.addDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref(getAddEntryURL());

						ClayToolbarViewState clayToolbarViewState =
							getWrapped();

						dropdownItem.setLabel(
							clayToolbarViewState.getAddEntryMessage());
					});
			}

			return _creationMenu;
		}

		@Override
		public List<DropdownItem> getFilterDropdownItems() {
			if (_filterDropdownItems == null) {
				_filterDropdownItems = new DropdownItemList();

				_filterDropdownItems.addGroup(
					dropdownGroupItem -> {
						dropdownGroupItem.setDropdownItems(
							_getOrderByDropdownItems());
						dropdownGroupItem.setLabel(
							LanguageUtil.get(_resourceBundle, "order-by"));
					});
			}

			return _filterDropdownItems;
		}

		@Override
		public RenderURL getSortingURLReverse() {
			return super.getSortingURLReverse();
		}

		private void _addURLRenderParameters(
			RenderURL renderURL, String assignmentId, String renderName) {

			MutableRenderParameters urlRenderParameters =
				renderURL.getRenderParameters();

			if (Validator.isNotNull(assignmentId)) {
				urlRenderParameters.setValue("assignmentId", assignmentId);
			}

			urlRenderParameters.setValue("javax.portlet.render", renderName);
			urlRenderParameters.setValue("viewId", "master");
		}

		private List<DropdownItem> _getOrderByDropdownItems() {
			DropdownItemList orderByDropDownItems = new DropdownItemList();

			orderByDropDownItems.add(
				dropdownItem -> {
					dropdownItem.setActive(
						Objects.equals(getSortingOrder(), "createDate"));
					dropdownItem.setHref(
						getSortingURLCurrent(), "orderByCol", "createDate");
					dropdownItem.setLabel(
						LanguageUtil.get(_resourceBundle, "create-date"));
				});

			if (_renderName.equals("assignment")) {
				orderByDropDownItems.add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getSortingOrder(), "title"));
						dropdownItem.setHref(
							getSortingURLCurrent(), "orderByCol", "title");
						dropdownItem.setLabel(
							LanguageUtil.get(_resourceBundle, "title"));
					});
			}
			else {
				orderByDropDownItems.add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getSortingOrder(), "submitDate"));
						dropdownItem.setHref(
							getSortingURLCurrent(), "orderByCol", "submitDate");
						dropdownItem.setLabel(
							LanguageUtil.get(_resourceBundle, "submit-date"));
					});
			}

			return orderByDropDownItems;
		}

		private CreationMenu _creationMenu;
		private DropdownItemList _filterDropdownItems;
		private String _renderName;
		private ResourceBundle _resourceBundle;

	}

}