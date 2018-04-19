/**
 *  数据下载
 *
 */
 
 define([],function(){
	var d = {
		list:["hycom.down",
			  "wavewatch.down"],
		data:{
			hycom:[],
			wavewatch:[],
		},
		init:function() {
			//初始化布局
			for(var i = 0; i < d.list.length; i++) {
				var name = d.list[i];
				var index = Monitor.layout.list.indexOf(name);
				Monitor.elems[index]['downFile'] = [];
				var date = new Date();
				var t =  24*60*60*1000;
				
				date.setTime(date.getTime() - 2 * t);
				var html = "<div id='down_" + index + "_0'>" + date.getFullYear()  + "-"+ (date.getMonth() + 1) + "-" + date.getDate() + "下载记录: </div>"	
				$('#content' + index).append(html);
				d.queryDownStatus(index, date, 0);
				
				date.setTime(date.getTime() + t);
				html = "<div id='down_" + index + "_1'>" + date.getFullYear()  + "-"+ (date.getMonth() + 1) + "-" + date.getDate() + "下载记录: </div>"
				$('#content' + index).append(html);
				d.queryDownStatus(index, date, 1);
			}
		},
		queryDownStatus:function (index, date, count) {
			var name = Monitor.elems[index].type;
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			month = month < 10 ? '0' + month : month;
			var day = date.getDate();
			day = day < 10 ? '0' + day : day;
			var param = '?type=' + name + '&year=' + year + '&month=' + month + '&day=' + day;
			$.ajax({
				url: Monitor.url + 'loginfo/downfile/' + param,
				dataType:'json',
				success: function(res){
					var html =   "<a class='btn btn-lg btn-shadow-medium' role='button' onclick='Monitor.down.showDown("+index+"," + count + ")' data-toggle='modal' data-target='#downModal'>"
								+ res['DownloadedFilesNum'] + "/" +res['TotalFiles']
								+ "</a>";
					$('#down_' + index + '_' + count).append(html);
					Monitor.elems[index]['downFile'][count] = res['DownloadedFiles'];
				},
				error: function(res){
					console.log(name + ".down 下载数据查询失败");
				}
			});
		},
		showDown:function (index, count) {
			var elem = Monitor.elems[index];
			var name = elem.cname;
			var type = elem.type;
			$('#downType').html(name);
			$('#downTitle').html(name + "下载日志");
			var log = "";
			for(var i = 0; i < elem['downFile'][count].length; i++) {
				log += "<p style = 'word-wrap:break-word'>" + elem['downFile'][count][i] + "</p>";
			}
			$('#downContent').html(log);
			$('#downModal').show();
		}	
	}
	
	return Monitor.down = {
		init: d.init,
		showDown: d.showDown
	}
});