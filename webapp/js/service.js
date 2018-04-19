/**
 *  服务运行状态
 *
 */

define([],function(){
	var s = {
		init:function() {
			//确定服务是否被开启或正常
			for(var i = 0; i < Monitor.elems.length; i++) {
				s.setStatus(i);
				s.setLogStatus(i);
			}
		},
		setStatus: function(index) {		//判断当前线程状态
			var color = 'green';
			if(Monitor.elems[index].thread && Monitor.elems[index].thread.length > 0) {	//已启动
				$('#start' + index).hide();
				$('#stop' + index).show();
				var time = new Date().getTime();
				if(time - Monitor.elems[index].time > 3 * 60 * 60 * 1000) {		//三小时内没有新的日志
					color = 'orange';
				}
			} else {	//未启动
				color = 'red';
				$('#start' + index).show();
				$('#stop' + index).hide();
			}
			$('#title' + index).css('color', color);
			$('#content' + index).append('最后日志时间:' + Monitor.elems[index].timestring);
		},
		setLogStatus:function(index) {
			if(Monitor.elems[index].error > 0) {
				$('#log' + index).css('color', 'red');
			}
		},
		queryStatus :function(index)  {		//查询单条线程运行状态
			var name = s.list[index];
			var param = '?type=' + name;
			$.ajax({
				url: Monitor.url + 'operation/status/' + param,
				dataType:'json',
				success: function(res){
					if(res.message && res.message.length > 0) {	//已启动
						$('#start' + index).hide();
						$('#stop' + index).show();
					} else {	//未启动
						$('#start' + index).show();
						$('#stop' + index).hide();
					}
				},
				error: function(res){
					console.log(name + "运行状态查询失败");
				}
			});
		},
		verification: function(action, index) {
			$('#vInput')[0].vAction = action;	//设置一个新的属性存放当前动作
			$('#vInput')[0].vIndex = index;		//设置一个新的属性存放当前索引
		},
		aSubmit:function() {		//提交操作及密码
			var pd = $('#vInput')[0].value.trim();
			if(!pd){
				alert("密码不能为空");
				return;
			}
			if(pd.indexOf(" ") > 0) {
				alert("密码中不能含有空格");
				$('#vInput')[0].value = "";
				return;
			}
			var action = $('#vInput')[0].vAction;
			var index = $('#vInput')[0].vIndex;
			if(action == 0) {
				s.start(index, pd);
			} else if(action == 1) {
				s.stop(index, pd);
			}
			$('#vInput')[0].value = "";		//清空密码框
			$('#vModal').modal('hide');
		},
		start:function(index, pd){		//启动线程
			var name = Monitor.elems[index].type;
			var param = '?type=' + name + '&password=' + pd;
			$.ajax({
				url: Monitor.url + 'operation/start/' + param,
				dataType:'json',
				success: function(res){
					$('#start' + index).hide();
					$('#stop' + index).show();
					$('#title' + index).css('color', 'green');
				},
				error: function(res){
					console.log(name + "启动失败！");
				}
			});
		},
		stop:function(index, pd){		//停止线程
			var name = Monitor.elems[index].type;
			var param = '?type=' + name + '&password=' + pd;
			$.ajax({
				url: Monitor.url + 'operation/end/' + param,
				dataType:'json',
				success: function(res){
					$('#start' + index).show();
					$('#stop' + index).hide();
					$('#title' + index).css('color', 'red');
				},
				error: function(res){
					console.log(name + "停止失败！");
				}
			});
		},
		logIndex:null,
		log:function (index, key){		//查询日志
			if(index >= 0) {
				logIndex = index;
			} else{
				index = logIndex;
			}
			var name = Monitor.elems[index].cname;
			var param = Monitor.elems[index].type;
			var urlParam = '?type=' + param;
			
			if(key) {
				key = key == 1 ? 'info' : 'error';
				urlParam += '&key=' + key;
			} else {
				$('.logkey').show();
				$('.logkey:last').hide();
			}
			$.ajax({
				url: Monitor.url + 'loginfo/tail' + urlParam,
				dataType:'json',
				success: function(res){
					$('#logType').html(name);
					$('#logTitle').html(name + "日志");
					var log = "";
					for(var i = 0; i < res[param + '.log'].length; i++) {
						var color = 'black';
						if(res[param + '.log'][i].indexOf("ERROR") > -1) {
							color = 'red';
						}
						log += "<p style = 'color:"+ color +";word-wrap:break-word'>" + res[param + '.log'][i] + "</p>";
					}
					$('#logContent').html(log);
				},
				error: function(res){
					console.log(name + "运行log查询失败");
				}
			});
		},
	}
	
	return Monitor.service = {
		init:s.init,
		verification:s.verification,
		aSubmit:s.aSubmit,
		log:s.log
	}
});