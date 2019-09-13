package com.liferay.training.gradebook.web.url;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.osgi.service.cdi.annotations.BeanPropertyType;

@BeanPropertyType
@Retention(RetentionPolicy.RUNTIME)
@Target(
	{
		ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
		ElementType.TYPE
	}
)
public @interface ServicePortlet {

	public static final String PREFIX_ = "javax.portlet.";

	String name();

}