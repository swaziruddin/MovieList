<html>
<head>
<title>IMDB POC</title>   
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js" type="text/javascript"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="apps.js"></script>
</head>


<body>
<form id="myAjaxRequestForm">
	<b>Search Term:</b>
	<input type="text" name="searchTerm" id="searchTermId" size="10"/>
	<input id="myButton" type="submit" value="Sumbit" onClick="searchIMDB(document.getElementById('searchTermId').value)"/>
</form>

<div id="resultsId"></div>

</body>
</html>