<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!--  Listing grid -->

<display:table pagesize="5" class="displaytag" name="conferences"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	
	<spring:message code="conference.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}"
		sortable="false" />
		
	<security:authorize access="hasRole('ADMIN')">
	<spring:message code="conference.isFinal" var="isFinalHeader" />
	<display:column property="isFinal" title="${isFinalHeader}"
		sortable="false" />
	</security:authorize>
	
	<spring:message code="conference.startDate" var="startDateHeader" />
	<display:column property="startDate" title="${startDateHeader}"
		sortable="false" />

	<spring:message code="conference.endDate" var="endDateHeader" />
	<display:column property="endDate" title="${endDateHeader}"
		sortable="false" />
		
	<!-- Actions -->
	
	<security:authorize access="hasRole('AUTHOR')">
	<display:column>
	<jstl:if test="${row.submissionDeadline > today }">
		<a href="submission/author/create.do?conferenceId=${row.id}"> <spring:message
				code="conference.submission" />
		</a>
	</jstl:if>
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
	<display:column>
	<jstl:if test="${row.submissionDeadline < today && row.isFinal == true}">
		<a href="administrator/decide.do?conferenceId=${row.id}"> <spring:message
				code="conference.decide" />
		</a>
	</jstl:if>
	</display:column>
	
	<display:column>
		<a href="activity/administrator/list.do?conferenceId=${row.id}"> <spring:message
				code="conference.activity.list" />
		</a>
	</display:column>
	</security:authorize>
	
</display:table>
<br/>
