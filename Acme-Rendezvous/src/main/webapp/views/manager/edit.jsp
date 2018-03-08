<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<jstl:set var="actionURL" value="manager/edit.do"/>

<form:form
	action="${actionURL}"
	modelAttribute="managerForm">
	
	<%-- <form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="createdRendezvous"/>
	<form:hidden path="reservedRendezvous"/> --%>
	<form:hidden path="userAccount.authorities"/>
	<form:hidden path="userAccount.id"/>
	<form:hidden path="userAccount.version"/>
	
	<acme:textbox code="manager.username" path="userAccount.username" />
	<acme:password code="manager.password" path="userAccount.password" />
	<acme:password code="manager.confirmPassword" path="confirmPass" />
	<acme:textbox code="manager.name" path="name" />
	<acme:textbox code="manager.surname" path="surname" />
	<acme:textbox code="manager.vat" path="vat" />
	<spring:message code="manager.date.placeholder" var="dateplaceholder"/>
	<acme:textbox code="manager.birthdate" path="birthdate" placeholder='${dateplaceholder}' />
	<spring:message code="manager.placeholderEmail" var="emailplaceholder"/>
	<acme:textbox code="manager.email" path="email" placeholder='${emailplaceholder}' />
	<acme:textbox code="manager.phone" path="phone" />
	<acme:textbox code="manager.address" path="address" />
	<a href="misc/terms.do" target="_blank"><spring:message code="manager.acceptTerms"/></a><acme:checkbox code="manager.blank" path="acceptTerms"/>
	<acme:submit name="save" code="manager.save"  />
	<acme:cancel code="manager.cancel" url="welcome/index.do" />	
</form:form>
