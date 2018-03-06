<%--
 * action-2.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="service/manager/edit.do" modelAttribute="service">
	<form:hidden path="id" />
	<form:hidden path="version" />


	<form:hidden path="requests" />
	<form:hidden path = "cancelled"/>
	
	<acme:textbox code="service.name" path="name" /><br />
	<acme:textarea code="service.description" path="description" /><br />
	<acme:textbox code="service.pictureUrl" path="pictureUrl" /><br />
	<acme:textbox code="service.price" path="price" /><br />
	<acme:select items="${categories}" itemLabel="displayName" code="service.category" path="category"/>

	<jstl:if test="${service.cancelled == false }">
	<acme:submit name="save" code="service.save"  />
	</jstl:if>
	
	<jstl:if test="${service.id!= 0 }">
	<jstl:if test="${requestSize == 0 }">
	<acme:submit name="cancel" code="service.delete"/>
	</jstl:if>
	</jstl:if>
	<acme:cancel code="service.cancel" url="service/manager/list-created.do" /><br />	
</form:form>



