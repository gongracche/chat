'use strict'

var chatDom = [];

var p = function(dom) {
	chatDom.push(dom);
}

var printMessage = ( function(message, position) {
	chatDom = [];
	p('<div class="chat-message chat-message-' + position +'">');
	p('<div class="chat-message-box">');
	p('<div class="chat-message-content">');
	p('<div class="chat-message-text">' + message + '</div>');
	p('</div>');
	p('</div>');
	p('</div>');
	p('<div class="chat-messge-clear"></div>');

	$('#chat-messges').append(chatDom.join(''));
} );

$(document).ready(function(){
	chatDom = [];
	p('<div id="chat-header">');
	p('<div id="chat-user-status">');
	p('<div id="chat-status-icon">●</div>');
	p('<div id="chat-user-name">ユーザー</div>');
	p('</div>');
	p('</div>');
	p('<div id="chat-messges">');
	p('</div>');
	p('<div id="chat-send">');
	p('<textarea id="chat-send-message"></textarea>');
	p('<div id="chat-send-btn">送信</div>');
	p('</div>');
	$('#chat-container').append(chatDom.join(''));

	connectServer();
});

var ws = null;

var onOpen = ( function() {
	$('#chat-send-btn').click(function(){
		var message = $('#chat-send-message').val();
		ws.send(JSON.stringify({"message": message}));
		$('#chat-send-message').val("");
	})
} );

var onMessage = ( function(e) {
	var data =JSON.parse(e.data);
	var message = data.message;
	printMessage(message, "left");
} );

var connectServer = function() {
	ws = new WebSocket('ws://localhost:' + location.port + '/chat/ws');
	ws.onopen = onOpen;
	ws.onmessage = onMessage;
}
