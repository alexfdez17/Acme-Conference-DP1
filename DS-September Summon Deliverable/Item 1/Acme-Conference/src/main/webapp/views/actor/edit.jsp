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

<script>
function checkPhoneNumber() {
	var phone = document.getElementById("phone").value;
   	pattern = /^((\+(\d{1}|\d{2}|\d{3}))* *(\((\d{1}|\d{2}|\d{3})\))* )*(\d{4,})$/igm;
   	var result = pattern.test(phone);
   	if(result==false){
   		confirm("<spring:message code='actor.checkPhone'/>");
   	}
}
</script>


<form:form action="${role}/edit.do"
	modelAttribute="actor" onsubmit="checkPhoneNumber();">
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="actor.name" path="name"/>
	<acme:textbox code="actor.middleName" path="middleName"/>
	<acme:textbox code="actor.surname" path="surname"/>
	<acme:textbox code="actor.photo" path="photo"/>
	<acme:textbox code="actor.email" path="email"/>
	<acme:textbox code="actor.phone" path="phone"/>
	<acme:textbox code="actor.address" path="address"/>
	
	<jstl:if test="${role == 'reviewer'}">
		<acme:textbox code="actor.reviewer.keywords" path="keywords"/>
	</jstl:if>
	
	<acme:submit name="save" code="actor.save"/>
		
	<acme:cancel url="" code="actor.cancel"/>
	

</form:form>
<spring:message code="actor.phoneCheck" var="phoneCheck" />
<script>
	$('#registerProviderForm').submit(function() {
		var regex = /^((\+[1-9][0-9]{0,2}\s[0-9]{4,})|(\+[1-9][0-9]{0,2}\s\([1-9][0-9]{0,2}\)\s[0-9]{4,})|([0-9]{4,})|(^$))$/;
		var phone = document.getElementById("phone").value;
		var submit = false;
		if (!regex.exec(phone)) {
			submit = confirm('<jstl:out value="${phoneCheck}" />');
		} else {
			submit = true;
		}
		return submit;
	});
</script>