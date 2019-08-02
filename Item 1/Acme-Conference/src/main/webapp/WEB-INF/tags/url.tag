<%@ tag language="java" body-content="empty"%>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%-- Attributes --%>

<%@ attribute name="code" required="true"%>
<%@ attribute name="url" required="true"%>

<%@ attribute name="value" required="false"%>

<b><spring:message code="${code}" /></b>
<jstl:if test="${value == null}">
	<a href="${url}"><jstl:out value="${url}"/></a>
</jstl:if>
<jstl:if test="${value != null}">
	<a href="${url}"><jstl:out value="${value}"/></a>
</jstl:if>
<br />