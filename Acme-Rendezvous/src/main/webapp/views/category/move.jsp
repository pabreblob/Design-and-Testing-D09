<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="category/admin/move.do" modelAttribute="category">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="name"/>
	<form:hidden path="description"/>
	<form:hidden path="children"/>
	<form:hidden path="services"/>	
	
	<acme:select items="${categories}" itemLabel="displayName" code="category.parent" path="parent"/>
	
	<acme:submit name="submit" code="category.submit"/>
	<acme:cancel url="category/display.do?categoryId=${childId}" code="category.cancel"/>

</form:form>