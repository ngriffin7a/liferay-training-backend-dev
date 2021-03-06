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

package com.liferay.training.gradebook.web.convert;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Date;

import javax.ws.rs.ext.ParamConverter;

/**
 * @author Neil Griffin
 */
public class DateParamConverter implements ParamConverter<Date> {

	public DateParamConverter(DateFormat dateFormat) {
		_dateFormat = dateFormat;
	}

	@Override
	public Date fromString(String value) {
		try {
			return _dateFormat.parse(value);
		}
		catch (ParseException pe) {
			throw new IllegalArgumentException(pe);
		}
	}

	@Override
	public String toString(Date date) {
		return _dateFormat.format(date);
	}

	private DateFormat _dateFormat;

}