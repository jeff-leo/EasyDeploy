$(document).ready(function() {

	//获取
	var uuid = $("#text-uuid").html();

	// 初始化刷新运行状态
	refreshStatus();

	// 部署按钮
	$("#btn-deploy").click(function () {
		ajaxShell("../deploy", {uuid: uuid}, function() {
			refreshStatus();
		});
	});

	// 重启按钮
	$("#btn-restart").click(function () {
		ajaxShell("../restart", {uuid: uuid}, function() {
			refreshStatus();
		});
	});

	// 停止按钮
	$("#btn-stop").click(function () {
		ajaxShell("../stop", {uuid: uuid}, function() {
			refreshStatus();
		});
	});


	/**
	 * ajax请求后台运行脚本
	 */
	function ajaxShell(url, postData, successCallback) {
		$("#loader-modal").openModal({dismissible: false});
		$.ajax({
			url: url,
			type: "POST",
			data: postData,
			cache: false,
			dataType: "text",
			success: function (data) {
				$("#loader-modal").closeModal();
				$("#layer-modal .modal-content").html(data.replace(/\n/g,"<br>"));
				$("#layer-modal").openModal({dismissible: false});
				if(successCallback) {
					successCallback();
				}
			},
			error: function () {
				$("#loader-modal").closeModal();
				layerAlert("发生异常，请重试！");
			}
		});
	}

    /**
     * 进入detail界面时，刷新当前项目的运行状态
     */
	function refreshStatus() {
		$.ajax({
			url: "../status",
			type: "GET",
			data: {uuid: uuid},
			cache: false,
			dataType: "text",
			success: function (data) {
				//首先先隐藏所有的span
				$(".service-status").children("span").hide();
				if(data === "true") {
					//显示正在运行
					$(".service-status").find(".green-text").show();
				} else {
					//显示已停止
					$(".service-status").find(".red-text").show();
				}
			},
			error: function () {
				layerAlert("发生异常，请重试！");
			}
		});
	}

	/**
	 * 用于代替alert
	 * @param text
	 */
	function layerAlert(text) {
		$("#alert-modal .text-alert").html(text);
		$("#alert-modal").openModal({dismissible: false});
	}
	
});