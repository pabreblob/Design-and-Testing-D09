
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

	<div>
	<p>
	<spring:message code="admin.name"/>: <jstl:out value="${administrator.name}"/> 
	</p>
	</div>
	
	<div>
	<p>
	<spring:message code="admin.surname"/>: <jstl:out value="${administrator.surname}"/> 
	</p>
	</div>
	
	<div>
	<spring:message code="admin.dateFormat2" var="dateFormat2" />
	<p>
	<spring:message code="admin.birthdate"/>:<fmt:formatDate value="${administrator.birthdate}" pattern="${dateFormat2}" />
	</p>
	</div>
	
	<div>
	<p>
	<spring:message code="admin.email"/>: <jstl:out value="${administrator.email}"/> 
	</p>
	</div>
	<jstl:if test="${user.phone!=null}">
	<div>
	<p>
	<spring:message code="admin.phone"/>: <jstl:out value="${administrator.phone}"/>
	</p>
	</div>
	</jstl:if>
	<jstl:if test="${administrator.address!=null}">
	<div>
	<p>
	<spring:message code="admin.address"/>: <jstl:out value="${administrator.address}"/>
	</p>
	</div>
	</jstl:if>
