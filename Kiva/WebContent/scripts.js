function getNewestLenders(){
	$.getJSON("http://api.kivaws.org/v1/lenders/newest.json", {
		format: "json"
	})
	.done(function(data){
		//	TODO: handle the case where there are no results
		if (data != null){
			var resultsElement = document.getElementById("resultsDiv");
			if (resultsElement != null){
				//	get the lenders and iterate through them to display something
				if (data.lenders != null){
					var results = "<table border='1'>";
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
	})
	.error(function(data){
		var errorElement = document.getElementById("errorDiv");
		if (errorElement != null){
			if (data.status == "404" ){
				errorElement.innerHTML = "Service is down.  Try again later.";
			} else {
				errorElement.innerHTML = "Something went wrong.  Try again later.";
			}
		}
	});
}