var obj = document.createElement("div"); 
obj.innerHTML = arguments[0];
var msg = "";
var path = obj.firstChild.nodeName;
traverseNodes(obj.firstChild,arguments[1]);  
return msg;
 
function traverseNodes(node,actualNode){
	if(node.nodeType != actualNode.nodeType){
		msg += "nodeType differ in path: " + path + "\n";
		return;		
	}
	
	if(node.nodeName != actualNode.nodeName){
		msg += "nodeType differ in path: " + path + "\n";
		return;		
	}	
	
	if(node.childNodes.length != getChildLength(actualNode)){
		msg += "ChildNodes number differ in path: " + path + ". Expected:" + node.childNodes.length + ", Actual:" + getChildLength(actualNode) +".\n";
		return;	
	}
	
	if(node.nodeType == 1){		
		var css=node.getAttribute("css");
		if(css!=null){
			var cssArray = css.split(";");
			for(var index=0;index< cssArray.length;index++){
				var attrName = cssArray[index].split("=")[0];
				var attrValue = cssArray[index].split("=")[1];
				var actualAttrValue = getComputedCssValue(actualNode,attrName);
				if(actualAttrValue.indexOf(attrValue) < 0 ){
					msg += "Attribute diff in path:" + path + ". Attribute:" + attrName + ", Expected:" + attrValue + ", Actual:" + actualAttrValue + "\n";
				}
			}			
		}	
		if(node.hasChildNodes){ 			
			var childNodes = node.childNodes; 
			var actualChildNodes = actualNode.childNodes;			
			for (var i = 0; i < childNodes.length; i++) { 				
				var child = childNodes.item(i);
				var actualChild = actualChildNodes.item(i);
				path = path + ">" + child.nodeName;							
				traverseNodes(child,actualChild);  
			}  
		}  
	}else{
		if(node.nodeValue != actualNode.nodeValue){
			msg += "Text diff in path:" + path + ". Expected:" + node.nodeValue + ", Actual:" + actualNode.nodeValue + "\n";
		}
	}
} 

function getComputedCssValue(node, propertyName){
	var v="";	
	if(window.getComputedStyle){
		v=window.getComputedStyle(node,false)[propertyName];
	}else if(node.currentStyle){ 
		v=node.currentStyle[propertyName]; 
	}else{
		v="not support style";
	}	
	return v;
}

function getChildLength(node){
	var length = 0;
	var sibling = node.firstChild;
	while(sibling != null){		
		if(sibling.nodeName.toLowerCase() != "br"){
			length++;			
		}
		sibling = sibling.nextSibling;
	}
	return length;
}

