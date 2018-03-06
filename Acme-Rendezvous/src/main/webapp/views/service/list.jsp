<%--
 * action-1.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<display:table name="services" id="s" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<security:authorize access="hasRole('ADMIN')">
	<display:column>
	<jstl:if test = "${s.cancelled == false}">
	<a href="service/admin/cancel.do?serviceId=${s.id}"> <spring:message
					code="service.cancel" />
			</a>
	</jstl:if>
	</display:column>
	</security:authorize>
	
	<jstl:if test="${requestURI == 'service/manager/list-created.do' }">
		<display:column>
			<jstl:if test="${s.cancelled == false}">
				<a href="service/manager/edit.do?serviceId=${s.id}"> <spring:message
						code="service.edit" />
				</a>
			</jstl:if>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${requestURI == 'service/manager/list-created.do' }">
		<display:column>
			<jstl:if test="${s.cancelled == false}">
			
				<a href="request/manager/list.do?serviceId=${s.id}"> <spring:message
						code="service.requests" />
				</a>
			</jstl:if>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${requestURI == 'service/user/list.do' }">
		<display:column>
				<a href="request/user/create.do?serviceId=${s.id}"> <spring:message
						code="service.request" />
				</a>
		</display:column>
	</jstl:if>

	<spring:message code="service.name" var="titleHeader" />
	<display:column property="name" title="${titleHeader}" />
	<spring:message code="service.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />



	<spring:message code="service.category" var="categoryHeader" />
	<display:column property="category.displayName" title="${categoryHeader}" />
	
	<spring:message code="service.price" var="priceHeader" />
	<display:column property="price" title="${priceHeader}" />

	<jstl:if test="${requestURI == 'service/manager/list-created.do' }">
	<spring:message code="service.cancelled" var="cancelledHeader" />
	<display:column title="${cancelledHeader}">
		<jstl:if test="${s.cancelled == false }">
			<div class="NOBORRADO">
				<spring:message code="service.cancelled.false" />
			</div>
		</jstl:if>
		<jstl:if test="${s.cancelled == true }">
			<div class="BORRADO">
				<spring:message code="service.cancelled.true" />
			</div>
		</jstl:if>
	</display:column>
	</jstl:if>
	
	<jstl:if test="${requestURI == 'service/admin/list.do' }">
	<spring:message code="service.cancelled" var="cancelledHeader" />
	<display:column title="${cancelledHeader}">
		<jstl:if test="${s.cancelled == false }">
			<div class="NOBORRADO">
				<spring:message code="service.cancelled.false" />
			</div>
		</jstl:if>
		<jstl:if test="${s.cancelled == true }">
			<div class="BORRADO">
				<spring:message code="service.cancelled.true" />
			</div>
		</jstl:if>
	</display:column>
	</jstl:if>
	
	
	<display:column>
	<jstl:if test = "${s.cancelled == false}">
	<a href="service/display.do?serviceId=${s.id}"> <spring:message
					code="service.display" />
			</a>
	</jstl:if>
	</display:column>
	
	
</display:table>



<jstl:if test="${requestURI == 'service/manager/list-created.do' }">
	<div>
		<a href="service/manager/create.do"> <spring:message
				code="service.create" />
		</a>
	</div>
</jstl:if>