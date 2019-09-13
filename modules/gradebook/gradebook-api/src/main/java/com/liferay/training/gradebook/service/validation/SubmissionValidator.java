
package com.liferay.training.gradebook.service.validation;

import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.training.gradebook.exception.SubmissionValidationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubmissionValidator {

	/**
	 * Returns <code>true</code> if the given fields are valid for an
	 * assignment.
	 *
	 * @param submissionText
	 * @param submitDate
	 * @param errors
	 * @return boolean <code>true</code> if assignment is valid, otherwise
	 *         <code>false</code>
	 */
	public static boolean isSubmissionValid(
		final String submissionText, final Date submitDate, final List<String> errors) {

		boolean result = true;

		result &= isSubmitDateValid(submitDate, errors);
		result &= isDescriptionValid(submissionText, errors);

		return result;
	}

	/**
	 * Returns <code>true</code> if submissionText is valid for an assignment. If
	 * not valid, an error message is added to the errors list.
	 *
	 * @param submissionText
	 * @param errors
	 * @return boolean <code>true</code> if submissionText is valid, otherwise
	 *         <code>false</code>
	 */
	public static boolean isDescriptionValid(
		final String submissionText, final List<String> errors) {

		boolean result = true;

		if (Validator.isBlank(HtmlUtil.stripHtml(submissionText))) {
			errors.add("error.assignment-submission-text-empty");
			result = false;
		}

		return result;
	}

	/**
	 * Returns <code>true</code> if submit date is valid for an assignment. If not
	 * valid, an error message is added to the errors list.
	 *
	 * @param submitDate
	 * @param errors
	 * @return boolean <code>true</code> if submit date is valid, otherwise
	 *         <code>false</code>
	 */
	public static boolean isSubmitDateValid(
		final Date submitDate, final List<String> errors) {

		boolean result = true;

		if (Validator.isNull(submitDate)) {
			errors.add("error.assignment-date-empty");
			result = false;
		}

		return result;
	}

	/**
	 * Validates assignment values and throws
	 * {SubmissionValidationExceptionException} if the assignment values are not
	 * valid.
	 *
	 * @param submissionText
	 * @param submitDate
	 * @throws SubmissionValidationException
	 */
	public static void validate(
			String submissionText, Date submitDate)
		throws SubmissionValidationException {

		List<String> errors = new ArrayList<>();

		if (!isSubmissionValid(submissionText, submitDate, errors)) {
			throw new SubmissionValidationException(errors);
		}
	}

}