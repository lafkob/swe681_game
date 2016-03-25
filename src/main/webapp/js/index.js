$(function() {
	$('#joinBtn').click(function() {
		$.ajax({
			type : "POST",
			url : "./api/game/join",
			data : { gameId: $("#gameId").val() },
			success : function(data, status, jqXHR) {
				
			},
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
		return false;
	});

	$('#statusBtn').click(function() {
		$.ajax({
			type : "POST",
			url : "./api/game/status",
			data : {},
			success : function(data, status, jqXHR) {
				
			},
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
		return false;
	});

	$('#moveBtn').click(function() {
		$.ajax({
			type : "POST",
			url : "./api/game/move",
			data : {},
			success : function(data, status, jqXHR) {
				
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
		$("div#reqResult").html("Error<br/>" + textStatus + "<br/>" + errorThrown);
	}
};