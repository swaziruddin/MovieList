function getNewestLenders(){
	$.getJSON("http://api.kivaws.org/v1/lenders/newest.json", {
		format: "json"
	})
	.done(function(data){
		displayResults(data);
	})
	.error(function(data){
		displayError(data);
	});
}

function resetPaging(){
	//	first figure out which radio button has been clicked
	//	this will allow to figure out what parameter to send to the api for paging
	var pageNumber = $('input[name=page]:checked').val();
	if (pageNumber != null){
		//	make the ajax call through jquery to display page number
		$.getJSON("http://api.kivaws.org/v1/lenders/newest.json", {
			format: "json",
			page: pageNumber 
		})
		.done(function(data){
			displayResults(data);
		})
		.error(function(data){
			displayError(data);
		});
	}
}
	
function displayResults(data){
	if (data != null){
		var resultsElement = document.getElementById("resultsDiv");
		if (resultsElement != null){
			//	get the lenders and iterate through them to display something
			if (data.lenders != null){
				var results = "<table border='1'>";
				//	create a table.  For faster rendering use a div
				results += "<tr>"
					+ "<td> Lender Id </td>"
					+ "<td> Name </td>"
					+ "<td> Whereabouts </td>"
					+ "<td> Country Code </td>"
					+ "<td> UID </td>"
					+ "</tr>";
				for (i=0; i<data.lenders.length; i++){
					//	for each lender start a new row
					//	ignore image data for now
					results += "<tr>"
						+ "<td>" + data.lenders[i].lender_id + "</td>"
						+ "<td>" + data.lenders[i].name + "</td>"
						+ "<td>" + data.lenders[i].whereabouts + "</td>"
						+ "<td>" + data.lenders[i].country_code + "</td>"
						+ "<td>" + data.lenders[i].uid + "</td>"
						+ "</tr>";
				}
				results += "</table>";
			}
			resultsElement.innerHTML = results;
		}
	}
}
	
function displayError(data){
	var errorElement = document.getElementById("errorDiv");
	if (errorElement != null){
		if (data.status == "404" ){
			errorElement.innerHTML = "Service is down.  Try again later.";
		} else {
			errorElement.innerHTML = "Something went wrong.  Try again later.";			}
		}
}