$(function() {
	$('#startBtn').click(function() {
		reqData = {};
		$.ajax({
			type : "POST",
			url : "./api/game/start",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : function(data, status, jqXHR) {
				$("div#reqResult").html(JSON.stringify(data));
			},
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
			success : function(data, status, jqXHR) {
				$("div#reqResult").html(data);
			},
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
			success : function(data, status, jqXHR) {
				$("div#reqResult").html(data);
			},
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
			success : function(data, status, jqXHR) {
				$("div#reqResult").html(data);
			},
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
			url : "./api/game/start",
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : function(data, status, jqXHR) {
				$("div#reqResult").html(status);
			},
			error: utils.requestErrorHandler,
			dataType : 'json',
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
		$("div#reqResult").html("<b>Error</b><br/>" + textStatus + "<br/>" + errorThrown);
	}
};