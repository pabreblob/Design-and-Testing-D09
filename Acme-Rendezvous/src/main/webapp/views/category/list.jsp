<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<display:table name="categories" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<spring:message code="category.name" var="${nameHeader}"/>
	<display:column title="${nameHeader}" property="name"/>
	
	<spring:message code="category.link.header" var="${linkHeader}"/>
	<display:column title="${linkHeader}">
		<a href="category/display.do?categoryId=${row.id}"><spring:message code="category.link"/></a>
	</display:column>

</display:table>

<security:authorize access="hasRole('ADMIN')">
	<a href="category/admin/create-top.do"><spring:message code="category.create"/></a>

</security:authorize>