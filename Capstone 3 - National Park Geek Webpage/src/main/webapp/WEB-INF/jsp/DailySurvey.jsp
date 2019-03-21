<!-- 

includes a survery to vote on their favoirte national park & asks for
	--email
	--stote
	--activity level

 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/CSS/DailySurveyStyle.css" var="StylePage"/>
<c:url value="/img/parks/HomePageBackground.jpg" var="parkPicture"/>
 
<c:import url="Header.jsp"> </c:import>
<link rel="stylesheet" href="${StylePage}"/>

<style>
body {
	background-image: url("${parkPicture}");
}

.error {
	color: red;
}
</style>

<div class="container alert-light">
	<div>
 	<form:form id="survey" action="/m3-java-capstone/EnterSurvey" method="POST" modelAttribute="survey">
			
		<div class="give-me-space">
			<label for="parkCode">Please pick your favorite national park!</label>
			<select class="form-control" id="parkCode" name="parkCode">
				<c:forEach var="name" items="${ parkCodeList }">
					<option value="${ name.key }">${ name.value }</option>	
				</c:forEach>
			</select>	
				
		</div>
		
		<div class="give-me-space">
			<label for="email">Email Address</label>
			<form:input path="email" required="true"/>
			<form:errors path="email" class="error"/>
			
			<c:if test="" >
			</c:if>
		</div>
		
		<div class="give-me-space">	
			<label for="state">State</label>
			<select class="form-control" id="state" name="state">
				<c:forEach var="state" items="${ states }">
					<option value="${ state }">${ state }</option>	
				</c:forEach>
			</select>				
		</div>
		
		<div class="give-me-space">	
			<label for="activityLevel">Activity Level</label>
			<select class="form-control give-me-space" id="activityLevel" name="activityLevel">
				<c:forEach var="level" items="${ activityLevel }">
					<option value="${ level }">${ level }</option>	
				</c:forEach>
			</select>				
		</div>
		
		
		
		<div class="give-me-space">
			<input type="submit" value="Submit Survey"/>
		</div>
		
	</form:form>
	</div>
</div>


<c:import url="Footer.jsp"> </c:import>
