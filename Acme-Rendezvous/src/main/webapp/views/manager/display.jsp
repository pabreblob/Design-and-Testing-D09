
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
	<spring:message code="manager.username"/>: <jstl:out value="${manager.userAccount.username}"/> 
	</p>
	</div>
	<div>
	<p>
	<spring:message code="manager.name"/>: <jstl:out value="${manager.name}"/> 
	</p>
	</div>
	
	<div>
	<p>
	<spring:message code="manager.surname"/>: <jstl:out value="${manager.surname}"/> 
	</p>
	</div>
	<div>
	<p>
	<spring:message code="manager.vat"/>: <jstl:out value="${manager.vat}"/> 
	</p>
	</div>
	
	<div>
	<spring:message code="manager.dateFormat2" var="dateFormat2" />
	<p>
	<spring:message code="manager.birthdate"/>:<fmt:formatDate value="${manager.birthdate}" pattern="${dateFormat2}" />
	</p>
	</div>
	
	<div>
	<p>
	<spring:message code="manager.email"/>: <jstl:out value="${manager.email}"/> 
	</p>
	</div>
	<jstl:if test="${manager.phone!=null}">
	<div>
	<p>
	<spring:message code="manager.phone"/>: <jstl:out value="${manager.phone}"/>
	</p>
	</div>
	</jstl:if>
	<jstl:if test="${manager.address!=null}">
	<div>
	<p>
	<spring:message code="manager.address"/>: <jstl:out value="${manager.address}"/>
	</p>
	</div>
	</jstl:if>
	