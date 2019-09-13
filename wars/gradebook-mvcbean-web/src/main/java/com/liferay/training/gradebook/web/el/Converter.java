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

package com.liferay.training.gradebook.web.el;

import com.liferay.training.gradebook.web.convert.DateParamConverter;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.inject.Named;

import javax.mvc.Models;

import javax.portlet.PortletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Neil Griffin
 */
@ApplicationScoped
@Named
public class Converter {

	public Object convert(String exp) {
		if (exp == null) {
			return null;
		}

		Expression expression = new Expression(exp);

		Object base = _models.get(expression.getBase());

		if (base == null) {
			return null;
		}

		Class<?> baseClass = base.getClass();

		try {
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(
				expression.getProperty(), baseClass);

			Method readMethod = propertyDescriptor.getReadMethod();

			Object invoke = readMethod.invoke(base);

			if (invoke == null) {
				return null;
			}

			DateParamConverter dateParamConverter = new DateParamConverter(
				new SimpleDateFormat("yyyy-MM-dd"));

			return dateParamConverter.toString((Date)invoke);
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return null;
	}

	private static final Logger _log = LoggerFactory.getLogger(Converter.class);

	@Inject
	private Models _models;

	@Inject
	private PortletRequest _portletRequest;

}