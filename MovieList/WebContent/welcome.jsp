<html>
<head>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<link rel="stylesheet" href="styles/styles.css" type="text/css" media="screen">
	<script type="text/javascript" src="scripts.js"></script>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js" type="text/javascript"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js" type="text/javascript"></script>
    <title>MovieList</title> 
</head>

<body>
<!--  protect the page by checking to make sure the correct cookie is set -->
<script>
	var cookies = document.cookie.split(';');
	var userId;
	for (var i=0; i<cookies.length; i++){
		var cookie = cookies[i];
		var cookieKeyValue = cookie.split("=");
		if (cookieKeyValue[0] == "userId"){
			userId = cookieKeyValue[1];
		}
	}
	if (userId == null){
		window.location = "login";
	}
</script>

<!-- if there are errors, show them -->
<c:forEach var="error" items="${errors}">
	<div class="errorDiv"><c:out value="${error}"/></div>
</c:forEach>
<div class="container">
	<div class="headerDiv">
		<div class="leftDiv">
			<h1>Welcome to your Movielist<c:if test="${not empty userName}">, <c:out value="${userName}"/></c:if>!</h1>
		</div>
		<div class="rightDiv">
			<form id="logoutForm" method="post" action="logout">
				<input id="logout" type="submit" name="logout" value="Logout"/> 
			</form>
		</div>
	</div>
	<div class="bodyDiv">
		<form id="myAjaxRequestForm">
		<div id=searchTermText><b>Search IMDB:</b></div>
		<input type="text" name="searchTerm" id="searchTermId" size="10"/>
		<input id="myButton" type="submit" value="Sumbit" onClick="searchIMDB(document.getElementById('searchTermId').value)"/>
		</form>
		<div id="resultsId"></div>
	</div>
</div>
</body>
</html>