$(function() {
	$('#startBtn').click(function() {
		utils.ajaxPostNoParams("./api/game/start");
		return false;
	});
	
	$('#openGamesBtn').click(function() {
		utils.ajaxPostNoParams("./api/info/open-games");
		return false;
	});
	
	$('#finishedGamesBtn').click(function() {
		utils.ajaxPostNoParams("./api/info/finished-games");
		return false;
	});
	
	$('#userInfoBtn').click(function() {
		utils.ajaxPostNoParams("./api/info/users");
		return false;
	});
	
	$('#auditBtn').click(function() {
		utils.ajaxPostGameIdOnly("./api/info/game", $("#auditGameId").val());
		return false;
	});
	
	$('#joinBtn').click(function() {
		utils.ajaxPostGameIdOnly("./api/game/join", $("#gameId").val());
		return false;
	});

	$('#statusBtn').click(function() {
		utils.ajaxPostGameIdOnly("./api/game/status", $("#gameId").val());
		return false;
	});

	$('#moveBtn').click(function() {
		reqData = {};
		reqData.gameId = $("#moveGameId").val();
		reqData.pieceId = $("#pieceId").val();
		reqData.moves = utils.parseMoves();
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
		utils.ajaxPostGameIdOnly("./api/game/quit", $("#gameId").val());
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
	ajaxPostNoParams: function(url) {
		reqData = {};
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			dataType : 'json',
			contentType: 'application/json'
		});
	},
	
	ajaxPostGameIdOnly: function(url, gameId) {
		reqData = {};
		reqData.gameId = gameId;
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(reqData),
			headers: {'X-CSRF-TOKEN': $("input#_csrf_token").val()},
			success : utils.jsonSuccessHandler,
			error: utils.requestErrorHandler,
			contentType: 'application/json'
		});
	},
		
	requestErrorHandler: function(jqXHR, textStatus, errorThrown){
		var output = "<b>Status:</b> ";
		output += jqXHR.status + " (" + errorThrown + ")<br/>";
		if(jqXHR.responseJSON) output += "<b>Response:</b> " + JSON.stringify(jqXHR.responseJSON);
		// do not show responseText in case html is returned
		
		$("div#reqResult").html(output);
	},
	
	jsonSuccessHandler: function(data, status, jqXHR){
		var message = "<b>Status:</b> " + jqXHR.status + " (" + status + ")";
		
		if(data) message += "<br/><b>Data:</b> " + JSON.stringify(data);
		
		if(data.board) message += "<br/><br/>" + utils.createBoardDisplay(data.board);
		
		$("div#reqResult").html(message);
	},
	
	createBoardDisplay: function(board2dArray) {
		var boardDisplay = "<b>Board Display</b><br/>";
		boardDisplay += "(0,0 is in the bottom left)<br/>";
		boardDisplay += "<div>"; // wrap the two panels
		boardDisplay += "<div class=\"leftPanel\">"; // board panel
		boardDisplay += "<table class=\"board\">";

		for (var x = board2dArray.length - 1; x >= 0; x--) {
			boardDisplay += "<tr>";
			for (var y = 0; y < board2dArray[x].length; y++) {
				boardDisplay += "<td class=\"cell\"><div class=\"cell\">";
				if(board2dArray[x][y] >= 0) {
					boardDisplay += board2dArray[x][y];
				}
				boardDisplay += "</div></td>";
			}
		    boardDisplay += "</tr>";
		}
		
		boardDisplay += "</table>";
		boardDisplay += "</div>";
		boardDisplay += utils.createBoardKeyDisplay();
		boardDisplay += "</div>";
		return boardDisplay;
	},
	
	createBoardKeyDisplay: function() {
		var key = "<div class=\"rightPanel\">";
		key += "<b>Player 1 Piece Mappings</b><br/>";
		key += "0, 1 -> Circle Piece<br/>";
		key += "2, 3 -> Diamond Piece<br/>";
		key += "4, 5 -> Square Piece<br/>";
		key += "6, 7 -> Triangle Piece<br/><br/>";
		key += "<b>Player 2 Piece Mappings</b><br/>";
		key += "8, 9 -> Circle Piece<br/>";
		key += "10, 11 -> Diamond Piece<br/>";
		key += "12, 13 -> Square Piece<br/>";
		key += "14, 15 -> Triangle Piece<br/>";
		key += "</div>"
		return key;
	},
	
	parseMoves: function() {
		var pairs = $("#move").val().split("|");
		var coords;
		var moves = [];
		for (var idx = 0; idx < pairs.length; idx++) {
			coords = pairs[idx].split(",");
			moves.push({y:coords[0], x:coords[1]}); // flip-flop for now with current board draw orientation
		}
		return moves;
	}
};