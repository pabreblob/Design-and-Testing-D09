<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jstl:if test="${father == true}">
	<a href="category/display.do?categoryId=${fatherId}"><spring:message code="category.father.link"/></a>
</jstl:if>

<jstl:if test="${father == false}">
	<a href="category/list.do"><spring:message code="category.toplist"/></a>
</jstl:if>

<h1><spring:message code="category.current"/>: ${category.displayName}</h1>
<br/>
<p><b><spring:message code="category.description" />:</b> ${category.description}</p>

<h2><spring:message code="category.services"/></h2>

<display:table name="services" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<spring:message code="category.service.name" var="${servNameHeader}"/>
	<display:column property="name" title="${servNameHeader}"/>
	
	<display:column>
		<a href="service/display.do?serviceId=${row.id}"><spring:message code="category.service.link"/></a>
	</display:column>
</display:table>

<h2><spring:message code="category.children"/></h2>

<display:table name="children" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<spring:message code="category.child.name" var="nameHeader"/>
	<display:column property ="displayName" title="${nameHeader}" />
	
	<spring:message code="category.child.link.header" var="${linkHeader}"/>
	<display:column title="${linkHeader}">
		<a href="category/display.do?categoryId=${row.id}"><spring:message code="category.child.link"/></a>
	</display:column>

</display:table>
<br/>
<security:authorize access="hasRole('ADMIN')">
	<a href="category/admin/create.do?categoryId=${category.id}"><spring:message code="category.child.create"/></a>
	<br/>
	<a href="category/admin/move.do?categoryId=${category.id}"><spring:message code="category.move"/></a>
	<br/>
	<a href="category/admin/edit.do?categoryId=${category.id}"><spring:message code="category.edit"/></a>
	<br/>
	<a href="category/admin/delete.do?categoryId=${category.id}"><spring:message code="category.delete"/></a>
</security:authorize>