<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="configuration/admin/save.do" modelAttribute="configuration">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="configuration.businessName" path="businessName" /><br />
	<acme:textbox code="configuration.banner" path="bannerUrl" /><br />
	<acme:textbox code="configuration.welcomeEng" path="welcomeEng"/><br />
	<acme:textbox code="configuration.welcomeEsp" path="welcomeEsp"/><br />
	
	<acme:submit name="save" code="configuration.submit"  />
	<acme:cancel code="configuration.cancel" url="configuration/admin/list.do" /><br />	
</form:form>