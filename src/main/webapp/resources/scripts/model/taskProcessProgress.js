$("document").ready(function(){
	
	var TaskProcessManager = kendo.Class.extend({
		selTaskId : null,
		validateProcessWidget : null,
		processTaskProgBar : null,
		urlMapping : {
			"validate" : "/task/action/task/process/validate"
		},
		defaultRequestParam : {
			url: null,
		    data : null,
		    method: "POST",
		    dataType : "json", 
		    contentType : "application/json"
		},
		createProcessBar : function(eleId){
			var progBar = $("#"+ eleId).kendoProgressBar({
                type: "percent",
                animation: {
                    duration: 300
                }
            }).data("kendoProgressBar");
			return progBar;
		},
		replaceCommasInMsg : function(message){
			var split = replaceAll(message, ",", "<br/>");
			return split;
		},
		styleForError : function(ele, content){
			ele = $("#"+ele);
			ele.html("");
			content = "<span style='color:red;' >" + content + "</span>";
			this.setIntervalForHtmlUpdate(ele, content, 500);
		},
		styleForSuccess : function(ele, content){
			ele = $("#"+ele);
			ele.html("");
			content = "<span style='color:green;' >" + content + "</span>";
			this.setIntervalForHtmlUpdate(ele, content, 500);
		},
		setIntervalForHtmlUpdate : function(ele, content, duration){
			if(duration == null || duration == 0){
				duration = 500;
			}
			ele = $(ele);
			ele.css({"display":"none"});
			var interval = null;
			interval = setInterval(function() {
				ele.html(content);
				ele.slideDown(duration);
				clearInterval(interval);
				interval = null;
			}, duration);
		},
		afterAjaxFunction : function(type,data){
			data = $.parseJSON(data);
			if(type == 'validate'){
				if(data.status != null && data.status == 'Success'){
					var msg = "Task passed validations<span class='k-icon k-i-tick' ></span>";
					this.styleForSuccess("taskValidationResults", msg);
					this.validateProcessWidget.value(100);
					$("#taskProcessValidationTd").html("Validating <span class='k-icon k-i-tick' ></span>");
				}else{
					var msg = this.replaceCommasInMsg(data.message);
					this.styleForError("taskValidationResults", msg);
					this.validateProcessWidget.value(100);
					this.processTaskProgBar.value(0);
				}
			}
		},
		ajaxProcessTask : function(params){
			var type = params["type"];
			var ajaxParams = $.parseJSON(JSON.stringify(this.defaultRequestParam));
			ajaxParams["data"] = JSON.stringify({"selTaskId":hunterAdminClientUserVM.get("selTaskId")});
			ajaxParams["url"] = HunterConstants.HUNTER_BASE_URL+this.urlMapping["validate"];
			var this_ = this;
			$.ajax(ajaxParams).done(function(data) {
				this_.afterAjaxFunction(type, data);
			 }).fail(function(data) {
				 kendoKipHelperInstance.popupWarning(data.statusText + " (" + data.status + "). Please contact Production Support.", "Error");
			 });
		},
		validateTaskProcess : function(){
			var r = $.Deferred();
			console.log("Validating task processing...");  
			 var valProgBar = this.createProcessBar("validationProgressBar") ;
			 valProgBar.value(0);
             this.validateProcessWidget = valProgBar;
             valProgBar.value(10);
             kendo.ui.progress($("#validationProgressBarSpinner"), false);
             var params = {"type" : "validate", "selTaskId" : this.selTaskId, "progBar" : valProgBar};
             this.ajaxProcessTask(params);
			return r;
		},
		processTask : function(){
			var r = $.Deferred();
			var processTaskProgBar = this.createProcessBar("processTaskProgressBar") ;
			processTaskProgBar.value(0);
            this.processTaskProgBar = processTaskProgBar;
            processTaskProgBar.value(30);
			return r;
		},
		killTaskProcessManager : function(){
			var r = $.Deferred();
			console.log("Finished processing, killing task process manager..."); 
			return r;
		},
		removeKendoSpinner : function(){
			var r = $.Deferred();
			 setTimeout(function(){ 
				 kendo.ui.progress($("#taskProcessProgressBar"), false);
			 }, 400);
			return r;
		},
		execute : function(){
			this.validateTaskProcess().done(
			this.processTask()).done(
			this.removeKendoSpinner()
		  );
		}
	});
	hunterAdminClientUserVM.set("taskProcessManager", new TaskProcessManager());
});


