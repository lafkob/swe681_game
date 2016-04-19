$(function() { 
	$('#registerBtn').click(function() {
		reqData = {};
		reqData.username = $("#username").val(); 
		reqData.password = $("#password").val();
		reqData.passwordConfirm = $("#passwordConfirm").val();
		
		$.ajax({
			type : "POST",
			url : "./api/register",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
		return false;
	});
});

utils = {
	requestErrorHandler: function(jqXHR, textStatus, errorThrown){
		var output = "<b>Request Error</b><br/>";
		output += jqXHR.status + ": " + errorThrown + "<br/>";
		if(jqXHR.responseJSON) output += JSON.stringify(jqXHR.responseJSON);
		// do not show responseText in case html is returned
		
		$("div#reqResult").html(output);
	},
	
	jsonSuccessHandler: function(data, status, jqXHR){
		message = "<b>Status:</b> " + jqXHR.status + " (" + status + ")";
		if(data) message += "<br/>" + JSON.stringify(data);
		$("div#reqResult").html(message);
		
		// window.location.href="./login"
	}
};