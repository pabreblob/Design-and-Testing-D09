<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="${requestURI}" modelAttribute="request">
	<jstl:if test="${rendezvousSize == 0 }">
	<spring:message code="request.norendez"/>
	<br/>
	</jstl:if>
	
	<jstl:if test="${rendezvousSize > 0 }">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:hidden path="service"/>
	
	<acme:textarea code="request.comment" path="comment"/>
	<acme:select items="${rendezvous}" itemLabel="name" code="request.rendezvous" path="rendezvous"/>
	<acme:textbox code="request.holderName" path="creditCard.holderName"/>
	<acme:textbox code="request.brandName" path="creditCard.brandName"/>
	<acme:textbox code="request.number" path="creditCard.number"/>
	<acme:textbox code="request.expMonth" path="creditCard.expMonth"/>
	<acme:textbox code="request.expYear" path="creditCard.expYear"/>
	<acme:textbox code="request.cvv" path="creditCard.cvv"/>
	</jstl:if>
	
	<jstl:if test="${rendezvousSize > 0 }">
	<acme:submit name="save" code="request.save"  />
	</jstl:if>
	<acme:cancel code="request.cancel" url="service/user/list.do" />
		
</form:form>