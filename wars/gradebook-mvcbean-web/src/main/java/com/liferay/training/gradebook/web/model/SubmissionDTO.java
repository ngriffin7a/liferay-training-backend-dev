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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.ws.rs.FormParam;

/**
 * @author Neil Griffin
 */
public class SubmissionDTO {

	public long getAssignmentId() {
		return assignmentId;
	}

	public int getGrade() {
		return grade;
	}

	public long getStudentId() {
		return studentId;
	}

	public long getSubmissionId() {
		return submissionId;
	}

	public String getSubmissionText() {
		return submissionText;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public void setSubmissionId(long submissionId) {
		this.submissionId = submissionId;
	}

	public void setSubmissionText(String submissionText) {
		this.submissionText = submissionText;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		return "submissionId=" + submissionId + " studentId=" + studentId +
			" grade=" + grade + " submissionText=" + submissionText +
				" submitDate=" + dateFormat.format(submitDate);
	}

	@FormParam("assignmentId")
	@Inject
	@NotNull
	private long assignmentId;

	@FormParam("grade")
	@Inject
	@NotNull
	private int grade;

	@FormParam("studentId")
	@Inject
	@NotNull
	private long studentId;

	@FormParam("submissionId")
	@Inject
	@NotNull
	private long submissionId;

	@FormParam("submissionText")
	@Inject
	@NotBlank
	private String submissionText;

	@FormParam("submitDate")
	@Inject
	@NotNull
	private Date submitDate;

}