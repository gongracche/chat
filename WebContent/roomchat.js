'use strict'

var dom = [];

var p = function(d) {
	dom.push(d);
}

var printMessage = ( function(message, position) {
	dom = [];
	p('<div class="chat-message chat-message-' + position +'">');
	p('<div class="chat-message-box">');
	p('<div class="chat-message-content">');
	p('<div class="chat-message-text">' + message + '</div>');
	p('</div>');
	p('</div>');
	p('</div>');
	p('<div class="chat-messge-clear"></div>');
	$('#chat-messages').append(dom.join(''));
} );

var printUser = ( function () {
	dom = [];
	p('<div id="chat-user-status">');
	p('<div id="chat-status-icon">●</div>');
	p('<div id="chat-user-name">ユーザ</div>');
	p('</div>');
	$('#chat-user').append(dom.join(''));
});

var printSender = ( function () {
	dom = [];
	p('<textarea id="chat-send-message"></textarea>');
	p('<div id="chat-send-btn">送信</div>');
	$('#chat-sender').append(dom.join(''));
});

var printRoomCreator = ( function() {
	dom = [];
	p('<div id="room-create-button">Room作成</div>');
	$('#room-creator').append(dom.join(''));
	$('#room-create-button').click(createRoom);
});

$(document).ready(function(){
	printUser();
	printSender();
	printRooms();
	printRoomCreator();
});

var ws = null;

var onOpen = ( function() {
	$('#chat-send-btn').click(function(){
		var message = $('#chat-send-message').val();
		ws.send(JSON.stringify({"message": message}));
		$('#chat-send-message').val("");
	})
});

var onMessage = ( function(e) {
	var data =JSON.parse(e.data);
	var message = data.message;
	printMessage(message, "left");
});

var openRoom = ( function(descriptor) {
	if (ws != null) {
		ws.close();
	}
	ws = new WebSocket('ws://localhost:' + location.port + '/chat/ws/room/' + descriptor);
	ws.onopen = onOpen;
	ws.onmessage = onMessage;


});

var closeRoom = ( function(descriptor) {
	if (ws != null) {
		ws.close();
	}
});

var getRooms = ( function() {
	var $form = $('<form />', { method: 'get', action: '/chat/api/room' });
	$form.appendTo(document.body);

	var data = $.ajax({
    	url: $form.attr('action'),
     	type: $form.attr('method'),
     	data: $form.serialize(),
    	async: false
    }).responseText;
	$form.remove();
	var rooms = JSON.parse(data);
	return rooms;
});

var getRoom = ( function(descriptor) {
	var $form = $('<form />', { method: 'get', action: '/chat/api/room/' + decriptor });
	$form.appendTo(document.body);

	var data = $.ajax({
    	url: $form.attr('action'),
     	type: $form.attr('method'),
     	data: $form.serialize(),
    	async: false
    }).responseText;
	$form.remove();
	var room = JSON.parse(data);
	return room;
});

var createRoom =  ( function() {
	var $dialog_button_execute = $('<button />', { id: 'room-create-button-exeute', class: 'dialog-button'});
	$dialog_button_execute.text("作成");
	$dialog_button_execute.click(function(){

		var $dialog_text_name = $('#room-name');
		var room_name = $dialog_text_name.val();
		var $form = $('<form />', { method: 'post', action: '/chat/api/room/create' });
		$form.append($('<input />', { type: 'hidden', name: 'name', value: room_name }));
		$form.appendTo(document.body);

		var data = $.ajax({
	    	url: $form.attr('action'),
	     	type: $form.attr('method'),
	     	data: $form.serialize(),
	    	async: false
	    }).responseText;
		$form.remove();
		var room = JSON.parse(data);

		printRoom(room);
		$dialog.remove();
	});
	var $dialog_button_cancel = $('<button />', { id: 'room-create-buttonn-cancel', class: 'dialog-button' });
	$dialog_button_cancel.text("キャンセル");
	$dialog_button_cancel.click(function(){
		$dialog.remove();
	});

	dom = [];
	p('<div id="room-dialog" class="dialog">');
	p('<div id="room-dialog-title"  class="dialog-title">' + '</div>');
	p('<div id="room-dialog-body"  class="dialog-body">');
	p('<div id="room-dialog-body-left"  class="dialog-body-left">' + '</div>');
	p('<div id="room-dialog-body-right"  class="dialog-body-right">' + '</div>');
	p('</div>');
	p('<div class="dialog-body-clear"></div>');
	p('<div id="room-dialog-buttons" class="dialog-buttons">' + '</div>');
	p('</div>');
	$('body').append(dom.join(''));

	var $dialog = $('#room-dialog');

	var $dialog_title=$('#room-dialog-title');
	$dialog_title.text($('#room-create-button').text());

	var $dialog_body_left=$('#room-dialog-body-left');
	$dialog_body_left.append('<div class="dialog-caption dialog-item">Room名</div>');

	var $dialog_body_right=$('#room-dialog-body-right');
	$dialog_body_right.append('<div class="dialog-item"><input type="text" id="room-name" class="dialog-text"></div>');

	var $dialog_buttons = $('#room-dialog-buttons');
	$dialog_buttons.append($dialog_button_cancel);
	$dialog_buttons.append($dialog_button_execute);
});

var printRoom = ( function(room) {
	var $room = $('<div />', { id: room.descriptor, class: "chat-room" });
	$room.text(room.name);
	$room.click(function(e) { openRoom(this.id); });
	$room.appendTo($('#rooms'));
});

var printRooms = ( function() {
	var rooms = getRooms();
	for(var i = 0; i < rooms.length; i++) {
		var room = rooms[i];
		printRoom(room);
	}
});