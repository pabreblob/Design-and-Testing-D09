
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
	<spring:message code="user.username"/>: <jstl:out value="${user.userAccount.username}"/> 
	</p>
	</div>
	<div>
	<p>
	<spring:message code="user.name"/>: <jstl:out value="${user.name}"/> 
	</p>
	</div>
	
	<div>
	<p>
	<spring:message code="user.surname"/>: <jstl:out value="${user.surname}"/> 
	</p>
	</div>
	
	<div>
	<spring:message code="user.dateFormat2" var="dateFormat2" />
	<p>
	<spring:message code="user.birthdate"/>:<fmt:formatDate value="${user.birthdate}" pattern="${dateFormat2}" />
	</p>
	</div>
	
	<div>
	<p>
	<spring:message code="user.email"/>: <jstl:out value="${user.email}"/> 
	</p>
	</div>
	<jstl:if test="${user.phone!=null}">
	<div>
	<p>
	<spring:message code="user.phone"/>: <jstl:out value="${user.phone}"/>
	</p>
	</div>
	</jstl:if>
	<jstl:if test="${user.address!=null}">
	<div>
	<p>
	<spring:message code="user.address"/>: <jstl:out value="${user.address}"/>
	</p>
	</div>
	</jstl:if>
	<spring:message code="user.dateFormat" var="dateFormat" />
	<jstl:if test="${rendezEmpty==false}">
	<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="${rendezvous}"  id="row" requestURI="${requestURI}">

	<spring:message code="user.rendezvous.name" var="nameHeader" />
		<display:column property="name" title="${nameHeader}" />
	
		
	<spring:message code="user.rendezvous.moment" var="momentHeader" />
	<display:column title="${momentHeader}" >	
	<jstl:if test ="${row.moment.before(timestamp) }">
		
		<div class = "EXPIRADO">
			<fmt:formatDate value="${row.moment}" pattern="${dateFormat}" />
		</div>
		</jstl:if>
		<jstl:if test ="${row.moment.after(timestamp)}">
		<div class = "NOEXPIRADO">
			<fmt:formatDate value="${row.moment}" pattern="${dateFormat}" />
		</div>
		</jstl:if>
	</display:column>
	<spring:message code="user.rendezvous.adult" var="adultHeader" />
		<display:column property="adultContent" title="${adultHeader}" />
	<display:column>
			<a href="rendezvous/display.do?rendezvousId=${row.id}">
				<spring:message	code="user.rendezvous.view" />
			</a>
		</display:column>
		
	</display:table>
	</jstl:if>