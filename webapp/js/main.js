require.config({
	//baseUrl: "/js/"
	baseUrl: "/OceanDataMonitor/js/"
});

var Monitor = {};

define([], function (){
	//Monitor.url = 'http://ocean.xinhong.net:9188/OceanDataMonitor/';
	Monitor.url = '/OceanDataMonitor/'
	Monitor.elems = [];
	
	require(["layout"], function(layout){
		
	});
	
	require(["hardware"], function(hardware) {
	});
});