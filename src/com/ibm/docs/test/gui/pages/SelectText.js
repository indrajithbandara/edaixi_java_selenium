var functions = {
	selectText : function (el, start, end) {
		var textNodes=[], walker = document.createTreeWalker(el, NodeFilter.SHOW_TEXT, null, false);
		while(node = walker.nextNode())
		    textNodes.push(node);
		var k = 0, startNode, startOffset, endNode, endOffset;
		for (var i in textNodes) {
			var textNode = textNodes[i];
			var textLen = textNode.nodeValue.length;
			if (start >= k && start < k + textLen) {
				startNode = textNode;
				startOffset = start - k;
			}
			
			if (end >= k && end < k + textLen) {
				endNode = textNode;
				endOffset = end - k + 1;
			}
			k += textLen;
		}
		
		var range=document.createRange(), selection=window.getSelection();
		range.setStart(startNode, startOffset);
		range.setEnd(endNode, endOffset);
		selection.removeAllRanges();
		selection.addRange(range);
	}
};

var args = Array.prototype.slice.call(arguments);
return functions['selectText'].apply(null, args);