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


<jstl:set var="actionURL" value="user/edit.do"/>

<form:form
	action="${actionURL}"
	modelAttribute="userForm">
	
	<%-- <form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="createdRendezvous"/>
	<form:hidden path="reservedRendezvous"/> --%>
	<form:hidden path="userAccount.authorities"/>
	<form:hidden path="userAccount.id"/>
	<form:hidden path="userAccount.version"/>
	
	<acme:textbox code="user.username" path="userAccount.username" /><br />
	<acme:password code="user.password" path="userAccount.password" /><br />
	<acme:password code="user.confirmPassword" path="confirmPass" /><br />
	<acme:textbox code="user.name" path="name" /><br />
	<acme:textbox code="user.surname" path="surname" /><br />
	<spring:message code="user.date.placeholder" var="dateplaceholder"/>
	<acme:textbox code="user.birthdate" path="birthdate" placeholder='${dateplaceholder}' /><br />
	<spring:message code="user.placeholderEmail" var="emailplaceholder"/>
	<acme:textbox code="user.email" path="email" placeholder='${emailplaceholder}' /><br />
	<acme:textbox code="user.phone" path="phone" /><br />
	<acme:textbox code="user.address" path="address" /><br />
	<a href="misc/terms.do" target="_blank"><spring:message code="user.acceptTerms"/></a><acme:checkbox code="user.blank" path="acceptTerms"/><br />
	<acme:submit name="save" code="user.save"  />
	<acme:cancel code="user.cancel" url="welcome/index.do" /><br />	
</form:form>
