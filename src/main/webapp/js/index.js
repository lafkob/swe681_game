$(function() {
	$('#startBtn').click(function() {
		reqData = {};
		$.ajax({
			type : "POST",
			url : "./api/game/start",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
		return false;
	});
	
	$('#findBtn').click(function() {
		reqData = {};
		$.ajax({
			type : "POST",
			url : "./api/info/open-games",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
		return false;
	});
	
	$('#joinBtn').click(function() {
		reqData = {};
		reqData.gameId = $("#gameId").val();
		$.ajax({
			type : "POST",
			url : "./api/game/join",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
		return false;
	});

	$('#statusBtn').click(function() {
		reqData = {};
		reqData.gameId = $("#gameId").val();
		$.ajax({
			type : "POST",
			url : "./api/game/status",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
		return false;
	});

	$('#moveBtn').click(function() {
		reqData = {};
		reqData.gameId = $("#gameId").val();
		$.ajax({
			type : "POST",
			url : "./api/game/move",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
		return false;
	});
	
	$('#quitBtn').click(function() {
		reqData = {};
		reqData.gameId = $("#gameId").val();
		$.ajax({
			type : "POST",
			url : "./api/game/quit",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			contentType: 'application/json'
		});
		return false;
	});

	$('#pingBtn').click(function() {
		$.ajax({
			type : "GET",
			url : "./api/ping",
			success : function(data, status, jqXHR) {
				$("div#reqResult").html(data);
			},
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
	}
};