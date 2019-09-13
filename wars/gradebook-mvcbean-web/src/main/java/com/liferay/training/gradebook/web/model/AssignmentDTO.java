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

package com.liferay.training.gradebook.web.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.inject.Inject;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.ws.rs.FormParam;

/**
 * @author Neil Griffin
 */
public class AssignmentDTO {

	public long getAssignmentId() {
		return assignmentId;
	}

	public String getDescription() {
		return description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getTitle() {
		return title;
	}

	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		return "assignmentId=" + assignmentId + " title=" + title +
			" description=" + description + " dueDate=" +
				dateFormat.format(dueDate);
	}

	@FormParam("assignmentId")
	@Inject
	@NotNull
	private long assignmentId;

	@FormParam("description")
	@Inject
	@NotBlank
	private String description;

	@FormParam("dueDate")
	@Future
	@Inject
	@NotNull
	private Date dueDate;

	@FormParam("title")
	@Inject
	@NotBlank
	private String title;

}