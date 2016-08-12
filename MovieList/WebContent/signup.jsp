<html>
<head>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<link rel="stylesheet" href="styles/styles.css" type="text/css" media="screen">
	<script type="text/javascript" src="scripts.js"></script>
	<title>MovieList</title> 
</head>

<body>
<!-- if there are errors, show them -->
<c:forEach var="error" items="${errors}">
	<div class="errorDiv"><c:out value="${error}"/></div>
</c:forEach>
<div class="parentDiv">
	<div class="leftDivLogin">
		<img src="images/movie-genres-documentary-icon.png"/>
	</div>
	<div class="rightDivLogin">
		<form id="signupForm" method="post" action="signup">
			<div class="formDiv">
				<input id="Name" name="name" placeholder="Name" type="text" size="25" autocomplete="on" 
					autofocus="autofocus"/>
			</div>
			<div class="formDiv">
				<input id="email" name="email" placeholder="Email" type="email" size="25" autocomplete="on" 
					autofocus="autofocus"/>
			</div>
			<div class="formDiv">
				<input id="password" name="password" placeholder="Password" type="password" size="25"/>
			</div>
			<div class="formDiv">
				<input id="signup" type="submit" name="signup" value="Sign Up"/>
			</div>
			<div class="formDiv">
				Already a member?&nbsp;&nbsp;<input id="login" type="submit" name="login" value="Login"/>
			</div>
		</form>
	</div>
</div>

</body>
</html>