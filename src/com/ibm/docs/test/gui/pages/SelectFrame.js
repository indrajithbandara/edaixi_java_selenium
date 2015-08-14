var id = arguments[0].id;
function sendEvent(name) {
	e = document.createEvent('MouseEvents');
	e.initMouseEvent(name, true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
	document.getElementById(id).dispatchEvent(e);
}
sendEvent("mousedown");
sendEvent("mouseup");
sendEvent("click");