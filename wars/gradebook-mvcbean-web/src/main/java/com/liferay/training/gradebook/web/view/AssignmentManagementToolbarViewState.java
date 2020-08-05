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
import com.liferay.frontend.taglib.clay.view.state.ManagementToolbarViewState;
import com.liferay.frontend.taglib.clay.view.state.ManagementToolbarViewStateWrapper;

import java.util.List;
import java.util.Objects;

import javax.portlet.MutableRenderParameters;
import javax.portlet.RenderURL;

/**
 * @author Neil Griffin
 */
public class AssignmentManagementToolbarViewState
	extends ManagementToolbarViewStateWrapper {

	public AssignmentManagementToolbarViewState(
		ManagementToolbarViewState clayToolbarViewState, String orderByMessage,
		String sizeMessage, String titleMessage) {

		super(clayToolbarViewState);

		_addEntryURL = clayToolbarViewState.getAddEntryURL();

		MutableRenderParameters renderParameters =
			_addEntryURL.getRenderParameters();

		renderParameters.setValue("javax.portlet.render", "assignment");
		renderParameters.setValue("viewId", "add");

		_orderByMessage = orderByMessage;
		_sizeMessage = sizeMessage;
		_titleMessage = titleMessage;
	}

	@Override
	public CreationMenu getCreationMenu() {
		if (_creationMenu == null) {
			_creationMenu = new CreationMenu();

			_creationMenu.addDropdownItem(
				dropdownItem -> {
					dropdownItem.setHref(_addEntryURL);

					ManagementToolbarViewState clayToolbarViewState = getWrapped();

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
					dropdownGroupItem.setLabel(_orderByMessage);
				});
		}

		return _filterDropdownItems;
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		DropdownItemList orderByDropDownItems = new DropdownItemList();

		orderByDropDownItems.add(
			dropdownItem -> {
				dropdownItem.setActive(
					Objects.equals(getSortingOrder(), "title"));
				dropdownItem.setHref(
					getSortingURLCurrent(), "orderByCol", "title");
				dropdownItem.setLabel(_titleMessage);
			});

		orderByDropDownItems.add(
			dropdownItem -> {
				dropdownItem.setActive(
					Objects.equals(getSortingOrder(), "size"));
				dropdownItem.setHref(
					getSortingURLCurrent(), "orderByCol", "size");
				dropdownItem.setLabel(_sizeMessage);
			});

		return orderByDropDownItems;
	}

	private RenderURL _addEntryURL;
	private CreationMenu _creationMenu;
	private DropdownItemList _filterDropdownItems;
	private String _orderByMessage;
	private String _sizeMessage;
	private String _titleMessage;

}