/**
 *  硬件监控
 *
 */

define(['library/echarts.simple.min'],function(echarts){
	var h = {
		list:["redisinfo",
			  "info"],
		names:['redis内存监控','硬件监控'],
		data:{
			redisinfo:null,
			info:null,
		},
		myChart:null,
		init:function() {
			//初始化布局
			h.queryHardwareStatus();
			h.queryRedisStatus();
		},
		queryHardwareStatus:function () {	//硬件监控
			var name = "info";
			var param = '?type=' + name + '.down';
			$.ajax({
				url: Monitor.url + 'hardware/' + name,
				dataType:'json',
				success: function(res){
					
					h.showHardwareStatus(res)
					setTimeout(function(){
						h.queryHardwareStatus()
					}, '30000');
				},
				error: function(res){
					console.log("查询硬件信息失败");
					$('#' + name).html("查询硬件信息失败");
					setTimeout(function(){
						h.queryHardwareStatus()
					}, '30000');
				}
			});
		},
		showHardwareStatus: function(res){		//显示硬件状态
			var harddisk = res.harddisk.split(" ");
			var loadavg = res.loadavg.split(", ");
			var color_cpu = res.cpu >= 80 ? 'red' : 'green';
			var color_harddisk = harddisk[2].split("%")[0] >= 80 ? 'red' : 'green';
			var color_1 = loadavg[0] >= 1.5 ? 'red' : 'green';
			var color_2 = loadavg[1] >= 1.5 ? 'red' : 'green';
			var color_3 = loadavg[2] >= 1.5 ? 'red' : 'green';
			var html = "<table border='0'>"
					 + "<tr>"
					 + "<td>cpu使用率 :</td>"
					 + "<td style='color:" + color_cpu + "'>" + res.cpu + "%</td>"
					 + "</tr>"
					 + "<tr>"
					 + "<td>硬盘使用空间 :</td>"
					 + "<td style='color:" + color_harddisk + ";width:40px;'>" + harddisk[0] + "</td>"
					 + "</tr>"
					  + "<tr>"
					 + "<td>硬盘剩余空间 :</td>"
					 + "<td style='color:" + color_harddisk + ";width:40px;'>" + harddisk[1] + "</td>"
					 + "</tr>"
					  + "<tr>"
					 + "<td>硬盘使用率 :</td>"
					 + "<td style='color:" + color_harddisk + ";width:40px;'>" + harddisk[2] + "</td>"
					 + "</tr>"
					 + "<tr>"
					 + "<td>系统负载 :</td>"
					 + "<td style='color:" + color_1 + ";width:40px;'>" + loadavg[0] + "</td>"
					 + "<td style='color:" + color_2 + ";width:40px;'>" + loadavg[1] + "</td>"
					 + "<td style='color:" + color_3 + ";width:40px;'>" + loadavg[2] + "</td>"
					 + "</tr>"
					 + "</table>";
			
			$('#info').html(html)
		},
		queryRedisStatus:function () {		//redis内存监控
			var name = "redisinfo";
			var param = '?type=' + name + '.down';
			$.ajax({
				url: Monitor.url + 'hardware/' + name,
				dataType:'json',
				success: function(res){
					$('#' + name).html("");
					for(var i = 0; i < res.nodes.length; i++) {
						var val = res.nodes[i];
						if(val.used_memory) {
							res.nodes[i].memory = (val.used_memory / 1073741824).toFixed(1);
							res.nodes[i].memory_ = ((val.used_memory / 17179869184) * 100).toFixed(1);
						}
					}
					
					h.showRedisStatus(res.nodes);
					setTimeout(function(){
						h.queryRedisStatus();
					}, 10 * 60 * 1000);
				},
				error: function(res){
					console.log("查询Redis内存信息失败");
					$('#' + name).html("查询Redis内存信息失败");
					setTimeout(function(){
						h.queryRedisStatus()
					}, 10 * 60 * 1000);
				}
			});
		},
		showRedisStatus: function(nodes){//显示Redis状态
			var yAxiss = [];
			var values = [];
			var color = '#008000';
			for(var i = 0; i < nodes.length; i++) {
				yAxiss[i] = nodes[i].port;
				values[i] = [nodes[i].memory_, i, nodes[i].memory];
				if(nodes[i].memory_ >= 80) {
					color = 'red';
				}
			}
			
			option = {
				title: {
					text: 'redis内存监控',
					show: false,
				},
				tooltip: {},
				grid:{
					left:40,
					top: 20,
					bottom: 20
				},
				legend: {
					data:['已用内存']
				},
				xAxis: {
				   max:100,
				   splitLine:{
					   show:false
				   }
				},
				yAxis: {
					axisTick:{
						inside:true
					},
					data:yAxiss
				},
				series: [{
					name: '已用内存',
					type: 'bar',
					data:values,
					label:{
						show:true,
						position:'right',
						color:'#000',
						formatter: '{@0}% {@2}GB'
					},
					itemStyle:{
						color: color
					}
				}]
			};

        // 使用刚指定的配置项和数据显示图表。
		h.myChart = echarts.init(document.getElementById('redisinfo'));
			h.myChart.setOption(option);
		},
		showDown:function (index) {
			var name = h.list[index]
			$('#logType').html(name);
			$('#logTitle').html(name + "下载日志");
			var log = "";
			for(var i = 0; i < h.data[name].length; i++) {
				log += "<p style = 'word-wrap:break-word'>" + h.data[name][i] + "</p>";
			}
			$('#logContent').html(log);
			$('#logModal').show();
		}
	}
	
	h.init();
	
	return Monitor.hardware = {
		showDown:h.showDown
	}
});