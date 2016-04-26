function searchIMDB (searchTerm) {
	event.preventDefault();
	//WORKS using yahoo server
	$.getJSON("http://query.yahooapis.com/v1/public/yql", {
		q: "select * from json where url=\"http://www.imdb.com/xml/find?json=1&nr=1&nm=on&q="+encodeURI(searchTerm)+
    		    		"?fmt=JSON\"",
        format: "json"
	})
	.done(function(data) {
		if (data.query.results) {
			var results = "<table border='1'>";
			for (var i=0; i<data.query.results.json.name_approx.length; i++){
				results +=   "<tr>"
						+ "<td> Name: " + data.query.results.json.name_approx[i].name + "</td>"
						+ "<td> Description: " + data.query.results.json.name_approx[i].description + "</td>"
						+ "<td> Id: "  + data.query.results.json.name_approx[i].id + "</td>"
						+ "<td> Title: "  + data.query.results.json.name_approx[i].title + "</td>"
						+ "</tr>";
				}
				results += "</table>";
				$('#resultsId').html(results);
		}
		else {
			alert("fail");
		    //	TODO: handle no results elegantly
		}
		console.log( "second success" );
	})
	.fail(function() {
		//	TODO: handle errors gracefully
		console.log( "error" );
	})
	.always(function() {
		console.log( "complete" );
	});
}