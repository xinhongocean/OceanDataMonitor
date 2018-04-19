define(['service', 'down'],function(){
	var l = {
		list:[],
		init: function() {
			l.queryElems_1();
			l.buttonOnClickEvent();
		},
		queryElems_1: function() {
			$.ajax({
				url: Monitor.url + 'loginfo/firstQuery',
				dataType:'json',
				success: function(res){
					//console.log(res);
					var length = res.message.length / 2;
					for(var i = 0; i < length; i++) {
						Monitor.elems[i] = res.message[i];
						l.list[i] = res.message[i]["type"];
					}
					for(var i = 0; i < length; i++) {
						var type = res.message[length + i].type.split('.start')[0];
						var index = l.list.indexOf(type);
						
						Monitor.elems[index]["thread"] = res.message[length + i].message;
					}
					l.queryElems_2();
				},
				error: function(res){
					console.log(name + "运行状态查询失败");
				}
			});
		},
		queryElems_2: function() {
			$.ajax({
				url: Monitor.url + 'loginfo/firstQuery82',
				dataType:'json',
				success: function(res){
					//console.log(res);
					var el = Monitor.elems.length;
					var length = res.message.length / 2;
					for(var i = 0; i < length; i++) {
						Monitor.elems[el + i] = res.message[i];
						l.list[el + i] = res.message[i]["type"];
					}
					for(var i = 0; i < length; i++) {
						var type = res.message[length + i].type.split('.start')[0];
						var index = l.list.indexOf(type);
						Monitor.elems[index]["thread"] = res.message[length + i].message;
					}
					l.addLayout();
					
					Monitor.service.init();	//填充状态
					Monitor.down.init();	//下载
				},
				error: function(res){
					console.log(name + "运行状态查询失败");
				}
			});
		},
		addLayout: function() {
			var html = '';
			for(var i = 0; i < Monitor.elems.length; i++) {
				var name = Monitor.elems[i].cname ? Monitor.elems[i].cname : Monitor.elems[i].type;
				
				html += "<div class='col-sm-6 col-md-4 col-lg-3'>"
					+ "<div class='thumbnail' style='height: auto;'>"
					+ "<h4 style='margin:5px;font-size:17px' id='title" + i + "'>"+ name +"</h4>"
					+ "<div  id='content" + i + "'>"
					+ "<ul class='bc-social-buttons'>"
					+ "<li class='social-weibo'>"
					+ "<a class='btn btn-lg btn-shadow-medium' id='start" + i + "' role='button' onclick='Monitor.service.verification(0, " + i + ")' data-toggle='modal' data-target='#vModal' >启动</a>"
					+ "<a class='btn btn-lg btn-shadow-medium' id='stop" + i + "' role='button'  onclick='Monitor.service.verification(1, " + i + ")' data-toggle='modal' data-target='#vModal'>停止</a>"
					+ "</li>"
					+ "<li class='social-weibo'>"
					+ "<a class='btn btn-lg btn-shadow-medium' id='log" + i + "' role='button' onclick='Monitor.service.log(" + i + ")' data-toggle='modal' data-target='#logModal'>日志</a>"
					+ "</li>"
					+ "</ul>"
					+ "</div>"
					+ "</div>"
					+ "</div>";
			}
			$('#service').append(html);
		},
		buttonOnClickEvent: function() {
			$('.logkey').on('click', function(e) {
				$('.logkey').show();
				e.currentTarget.style.display = 'none';
			});
		},
	}
	
	l.init();
	
	return Monitor.layout = {
		list: l.list,
		elems: l.elems
	}
});