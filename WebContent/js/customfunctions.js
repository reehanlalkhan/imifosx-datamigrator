var forgotPassword = function() {
	alert("Please contact ideoholic team for the password");
};

var loadLoginPage = function() {
	if (this.readyState == 4 && this.status == 200) {
		// Typical action to be performed when the document is ready:
		let locaiton = window.location.href;
		let lastindex = locaiton.lastIndexOf('/');
		let redirectLocation = locaiton.substring(0, lastindex);
		window.location.replace(redirectLocation);
	}
};

var logout = function() {
	try {
		var request = new XMLHttpRequest();
		request.onreadystatechange = loadLoginPage;
		request.open("POST", "doLogout", true);
		request.send();
		return true;
	} catch (err) {
		alert(err.message);
	}
};
