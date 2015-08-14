<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />
<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="refresh" content="10"/>
<style>
<xsl:text disable-output-escaping='yes'>
	body { 
		-webkit-text-size-adjust:none; 
		font-family: arial, sans-serif; 
		padding: 0; 
		margin: 0
	}
	table {
		border-collapse: collapse; 
		border-spacing: 0;
	}
	th {
		text-align:left;
		padding:2px;
		background: #000;
		color: #FFF;
		font-size:14px;
	}
	td {
		vertical-align: top;
		font-size:11px;
		padding:2px;
	}
	tr {
		border-bottom: 1px solid #ccc;
	}
	.clearfix:after {
		visibility: hidden;
		display: block;
		font-size: 0;
		content: " ";
		clear: both;
		height: 0;
	}
	.stats {
		padding: 2px;
		border-bottom: 1px solid #ccc;
	}
	.stats span {
		vertical-align:middle;
		margin-right:8px;
		font-size: 16px;
		line-height: 32px;
	}
	.stats span.tit {
	    font-size: 20px;
		font-weight: bold;
	}
	.stats span.num {
		font-size: 20px;
		font-weight: bold;
	}
	span.passed, tr.passed>td:nth-child(2) a, tr.passed>td:nth-child(3) {
		color: #090;
	}
	span.skipped, tr.skipped>td:nth-child(2) a, tr.skipped>td:nth-child(3) {
		color: #0F0;
	}
	span.ignored, tr.ignored>td:nth-child(2) a, tr.ignored>td:nth-child(3) {
		color: #FA0;
	}
	span.failure, tr.failure>td:nth-child(2) a, tr.failure>td:nth-child(3) {
		color: #F08;
	}
	span.error, tr.error>td:nth-child(2) a, tr.error>td:nth-child(3) {
		color: #F00;
	}
	pre {
		white-space: 
		pre-wrap;
		margin: 0;
	}
	a {
		color:#000;
	}
	#props {
		padding: 2px;
	}
	.key {
		font-weight:bold;
		display: inline-block;
		padding-right:5px;
		font-size:12px;
	}
	.value {
		padding-right:15px;
		display: inline-block;
		font-size:12px;
	}
</xsl:text>
</style>
<title>Report</title>
</head>
<body onload="onload()">

<div class="stats">
	<span class="tit">Suite: </span><span id="suite-name">Test</span>
	<span class="tit">Progress: </span>
	<span class="tests num" id="tests-num">0</span><span>tests</span>
	<span class="running num" id="running-num">0</span><span>running</span>
	<span class="finished num" id="finished-num">0</span><span>finished</span>
	<span class="tit">Duration: </span><span id="duration">0</span>
	<span class="tit">Result: </span>
	<span class="passed num" id="passed-num">0</span><span>passed</span>
	<span class="skipped num" id="skipped-num">0</span><span>skipped</span>
	<span class="ignored num" id="ignored-num">0</span><span>ignored</span>
	<span class="failure num" id="failure-num">0</span><span>failures</span>
	<span class="error num" id="error-num">0</span><span>errors</span>
</div>
<div id="props">Properties</div>
<div id="records">No Test</div>

<script>
<xsl:text disable-output-escaping='yes'>
<![CDATA[

function text(s) {
	return s.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}
function firstElement(e, tag){
	var es = e.getElementsByTagName(tag);
	return es[0] || null;
}

function mkLink(text) {
	if (!text)
		return '';
	var pattern = /(.*)#(.*)/;
	return pattern.test(text) ? '<a href="' + RegExp.$2 + '" target="_blank">' + RegExp.$1 + '</a>': text;
}

function env(e) {
	var html = '';
	for (var i = 0; i < e.children.length;i++) {
		var p = e.children.item(i);
		if (p.tagName.toUpperCase() == 'PROPERTY') {
			html += '<span class="key">' + p.getAttribute('name') + '</span><span class="value">' + p.getAttribute('value') + '</span>';
		}
	}
	return html;
}

function onload() {
	var data = document.getElementById('data');
	if (!data)
		return;
	var testsuite = firstElement(data, 'testsuite');
	document.title = testsuite.getAttribute("name") + ' Report';
	document.getElementById('suite-name').innerHTML = testsuite.getAttribute("name");
	document.getElementById('tests-num').innerHTML = testsuite.getAttribute("tests");
	document.getElementById('finished-num').innerHTML = testsuite.getAttribute("finished");
	document.getElementById('running-num').innerHTML = testsuite.getAttribute("running");
	document.getElementById('passed-num').innerHTML = testsuite.getAttribute("passed");
	document.getElementById('skipped-num').innerHTML = testsuite.getAttribute("skipped");
	document.getElementById('ignored-num').innerHTML = testsuite.getAttribute("ignored");
	document.getElementById('failure-num').innerHTML = testsuite.getAttribute("failures");
	document.getElementById('error-num').innerHTML = testsuite.getAttribute("errors");
	document.getElementById('duration').innerHTML = (testsuite.getAttribute("duration") / 1000) + "s";
	document.getElementById('props').innerHTML = env(testsuite);
	var testcases = data.getElementsByTagName('testcase');
	var html = '<table width="100%"><tr><th>Test</th><th width="60px">Status</th><th>Message</th><th width="80px">Duration</th></tr>';
	for (var i = 0; i < testcases.length; i++){
		var e = testcases[i];
		var classname = e.getAttribute('classname');
		var methodname = e.getAttribute('methodname');
		var testname = classname + '-' + methodname;
		var start = e.getAttribute('start');
		var end = e.getAttribute('end');
		var link = e.getAttribute('link');
		var doc = e.getAttribute('doc');
		var status = 'untested';
		var message = '';
		var duration = '';
		if (start)
			status = 'running';
		if (end) {
			duration = (end - start) / 1000;
			if ((r = firstElement(e, 'failure'))) {
				status = 'failure';
				message = '<pre>' + r.innerHTML + '</pre>';
			} else if ((r = firstElement(e, 'error'))) {
				status = 'error';
				message = '<pre>' + r.innerHTML + '</pre>';
			} else if ((r = firstElement(e, 'ignored'))) {
				status = 'ignored';
				message = mkLink(r.getAttribute("message"));
			} else if ((r = firstElement(e, 'skipped'))) {
				status = 'skipped';
				message ='<pre>' + r.innerHTML + '</pre>';
			} else {
				status = 'passed';
			}
		}
		
		html += '<tr class="' + status + '"><td><a href="' + (link||'') + '" title="'+ (doc||'') + '">' 
			+ testname + '</a></td><td><a href="' + (status != 'ignored' && status != 'untested' ? testname + '/log.html'  : '') + '" target="_blank">' 
			+ status + '</a></td><td>' 
			+ message + '</td><td>' 
			+ duration + '</td></tr>';
			
	}
	
	html+='</table>';
	document.getElementById('records').innerHTML = html;
}
]]>
</xsl:text>
</script>
<div id="data" style="display:none">
	<xsl:copy-of select="*" />
</div>
</body>
</html>
</xsl:template>
</xsl:stylesheet>    
   
