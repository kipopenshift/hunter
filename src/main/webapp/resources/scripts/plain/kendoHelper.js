
console.log("loading kendKipHelper file");

var kendoKipHelper = kendo.Class.extend({
	
	kendoWindow : null,
	measuredWindowBackUp : null,
	measuredWindow : null, 
	kendoInitTitle : "Kendo Window",
	notification : null, 
	
	beforeInit : function(){
		console.log("Setting up kendoHelper...");
		this.loadTemplate();
		this.initKendoWindow();
		this.initMeasuredWindow();
		this.initNotification();
	},
	loadTemplate : function(){
		$.get('/Hunter/static/resources/uiTemplates/kendoHelperTemplate.html', function(templates) {
			  $('body').append(templates);
		});
	},
	init : function(){
		this.beforeInit();
		this.afterInit();
	},
	createCheckBox : function(id, clazz, onChange, booleanChecked, booleanDisabled){
		var checked = booleanChecked ? "checked='checked'" : "";
		var disabled = booleanDisabled ? "disabled='disabled'" : "";
		var input =  "<input type='checkbox' id='"+ id +"' href='#' class='"+ clazz +"' onChange='"+ onChange +"' "+ checked +" "+ disabled +" />";
		return input;
	},
	afterInit : function(){
		
		console.log("KendoHelper helper initialized successfully!!");
	}, 
	initKendoWindow : function(){
		this.kendoWindow = $("#kendoWindow").kendoWindow({
 	        title: kendoKipHelper.kendoInitTitle,
 	        modal: true,
 	        visible: false,
 	        resizable: false,
 	        width: 300
     }).data("kendoWindow");
    	if(this.kendoWindow != null)
    		this.centerKendoWindow();
	},
	notifyForSucess : function(message){
		this.initNotification();
		this.notification.show({
            message: message
        }, "upload-success");
	},
	notifyForError : function(message){
		this.initNotification();
		this.notification.show({
             title: "Error!",
             message: message
         }, "error");
	},
	initNotification : function(){
		this.notification = $("#kendoNotificationDiv").kendoNotification({
            position: {
                pinned: true,
                top: 30,
                right: 30
            },
            autoHideAfter: 3000,
            stacking: "down",
            button: true,
            templates: [{
                type: "info",
                template: $("#emailTemplate").html()
            }, {
                type: "error",
                template: $("#errorTemplate").html()
            }, {
                type: "upload-success",
                template: $("#successTemplate").html()
            }]

        }).data("kendoNotification");
	},
	initMeasuredWindow : function(){
		this.measuredWindow = $("#measuredWindow").kendoWindow({
			title: kendoKipHelper.kendoInitTitle,
 	        modal: true,
 	        visible: false,
 	        resizable: false,
 	        width: "300", 
 	        height:"300", 
 	        resizable: false
		});
	},
	centerKendoWindow : function(){
    	this.kendoWindow.center(); 
    },
    centerMeasuredKendoWindow : function(){
    	/*this.measuredWindow.center(); */
    },
    closeHelperKendoWindow : function(){
    	this.kendoWindow.close();
    },
    closeMeasuredWindow : function(){
    	this.measuredWindow.close();
    },
    closeMeasuredWindowBackUp : function(){
    	this.measuredWindowBackUp.close();
    },
	getOKCancelKendoWindow : function(){
		
	},
	showSimplePopup : function(title, content){
		var OKButton = this.createKendoSmallIconTagTemplateNoIcon("kendoKipHelperInstance.closeHelperKendoWindow()", "<span style='padding-right:10px;padding-left:10px;' >OK</span>", false);
		if(this.initKendoWindow == null)
			this.initKendoWindow();
		this.kendoWindow.title = title;
		this.kendoWindow.content(content + "<br/><br/> " + OKButton);
		this.kendoWindow.center();
		this.kendoWindow.open();
	},
	showPopupWithNoButtons : function(title, content){
		this.initKendoWindow();
		this.kendoWindow.title = title;
		this.kendoWindow.content(content);
		this.kendoWindow.center();
		this.kendoWindow.open();
	},
	createKendoSmallIconTagTemplate : function(_kendoUiImgName, _toBeCalledScript,buttonName){
		var buttonNameStr = '';
		if(buttonName != null)
			buttonNameStr = buttonName;
		var scriptStr = "<center><a onclick='"+ _toBeCalledScript + "' class='k-button' style='padding:5px;'><span class='k-icon k-i-"+_kendoUiImgName+"'></span>"+ buttonNameStr +"</a></center>";
		return scriptStr;
	},
	createKendoSmallIconTagTemplateNoIcon : function(_toBeCalledScript,buttonName){
		var buttonNameStr = '';
		if(buttonName != null)
			buttonNameStr = buttonName;
		var scriptStr = "<center><a onclick='"+ _toBeCalledScript + "' class='k-button' style='padding:5px;'>"+ buttonNameStr +"</a></center>";
		return scriptStr;
	},
	createHidableIconTagTemplate : function(_kendoUiImgName, _toBeCalledScript,buttonName, isHide){
		var buttonNameStr = '';
		if(buttonName != null)
			buttonNameStr = buttonName;
		var display;
		if(isHide)
			display = "display:none";
		else 
			display = "";
		var string = "<center><a onclick='"+ _toBeCalledScript + "' class='k-button' style='padding:5px;"+ display +";'><span class='k-icon k-i-"+_kendoUiImgName+"'></span>"+ buttonNameStr +"</a></center>";
		return string;
	},
	showPopupToDeleteAddress : function(id, message){
		  this.kendoWindow.title("Delete Item Class Or Group");
		  this.kendoWindow.content(message);
		  this.kendoWindow.open();
	 },
	 showPercentMeasuredOKCancelTitledPopup : function(content, title, width, height){
		 if(this.measuredWindow != null){
			 this.measuredWindow = null;
			 $("#measuredWindow").empty();
			 $("#measuredWindow").html("");
		 }
		 
		var window = $("#measuredWindow").kendoWindow({
			  title : title,
			  width : width + "%",
			  height : height + "%",
			  pinned : true,
			  resizable: false
		  }).data("kendoWindow");
		this.measuredWindow = window;
		this.measuredWindow.content(content);
		this.measuredWindow.center();
		this.measuredWindow.open();
	 },
	 showBackUpPercentMeasuredOKCancelTitledPopup : function(content, title, width, height){
		var window = $("#measuredWindowBackUp").kendoWindow({
			  title : title,
			  width : width + "%",
			  height : height + "%",
			  pinned : true,
			  resizable: false
		  }).data("kendoWindow");
		this.measuredWindowBackUp = window;
		this.measuredWindowBackUp.content(content);
		this.measuredWindowBackUp.center();
		this.measuredWindowBackUp.open();
	 },
	 showOKCancelToDeleteItemGroup : function(id, message){
		  var b1 = this.createKendoSmallIconTagTemplate("close", "itemGroupVM.deleteSelectedItemGroup(" + id +")", "Delete");
		  var b2 = this.createKendoSmallIconTagTemplate("cancel", "kendoKipHelperInstance.closeHelperKendoWindow()", "Cancel");
		  var butTable = "<br/> <table style='width:60%;margin-left:20%;'><tr><td>" + b1 + "</td><td>" + b2 + "</tr></table>";
		  this.kendoWindow.content(message + butTable);
		  this.kendoWindow.open();
	 },
	 showOKCancelToDeleteTaskRegion : function(id, message){
		  var b1 = this.createKendoSmallIconTagTemplate("close", "hunterAdminClientUserVM.deleteSelectedTaskRegionGroup(" + id +")", "Delete");
		  var b2 = this.createKendoSmallIconTagTemplate("cancel", "kendoKipHelperInstance.closeHelperKendoWindow()", "Cancel");
		  var butTable = "<br/> <table style='width:60%;margin-left:20%;'><tr><td>" + b1 + "</td><td>" + b2 + "</tr></table>";
		  this.kendoWindow.content(message + butTable);
		  this.kendoWindow.open();
	 },
	 showOKCancelToDeleteHunterUser : function(id, message){
		 if(this.kendoWindow == null)
			  this.initKendoWindow();
		  var b1 = this.createKendoSmallIconTagTemplate("close", "hunterAdminClientUserVM.deleteSelectedUser(" + id +")", "Delete");
		  var b2 = this.createKendoSmallIconTagTemplate("cancel", "kendoKipHelperInstance.closeHelperKendoWindow()", "Cancel");
		  var butTable = "<br/> <table style='width:60%;margin-left:20%;'><tr><td>" + b1 + "</td><td>" + b2 + "</tr></table>";
		  this.kendoWindow.content(message + butTable);
		  this.kendoWindow.open();
	 },
	 showOKCancelToDeleteHunterTask : function(id, message){
		 if(this.kendoWindow == null)
			  this.initKendoWindow();
		  var b1 = this.createKendoSmallIconTagTemplate("close", "hunterAdminClientUserVM.deleteSelectedTask(" + id +")", "Delete");
		  var b2 = this.createKendoSmallIconTagTemplate("cancel", "kendoKipHelperInstance.closeHelperKendoWindow()", "Cancel");
		  var butTable = "<br/> <table style='width:60%;margin-left:20%;'><tr><td>" + b1 + "</td><td>" + b2 + "</tr></table>";
		  this.kendoWindow.content(message + butTable);
		  this.kendoWindow.open();
	 },
	 showOKCancelToSaveAs : function(id, message){
		  if(this.kendoWindow == null)
			  this.initKendoWindow();
		  var b1 = this.createKendoSmallIconTagTemplate("tick", "itemGroupVM.performSaveOperation(" + id +")", "Save As");
		  var b2 = this.createKendoSmallIconTagTemplate("cancel", "kendoKipHelperInstance.closeHelperKendoWindow()", "Cancel");
		  var butTable = "<br/> <table style='width:60%;margin-left:20%;'><tr><td>" + b1 + "</td><td>" + b2 + "</tr></table>";
		  this.kendoWindow.content(message + butTable);
		  this.kendoWindow.open();
	 },
	 showOKCancelToDeleteWorkflowConfig : function(configId){
		 
		 	var grid = $("#programTestMeasureGrid").data("kendoGrid");
			var model = grid.dataSource.get(configId);
			var objType = model.objType;
			var shortPath = model.shortPath;
			shortPath = shortPath === "N" ? "None short Path" : "Short Path";
			console.log("Object Type >> " + objType + " shortPath >> " + shortPath);
			var message = '"<b>' + shortPath + " " + objType + '</b>  will be deleted. <br/> Are you sure?"';
			 
			var b1 = this.createKendoSmallIconTagTemplate("close", 'workflowConfigVM.deleteSelectedWorkflowConfig("' + configId +'")', "Delete");
			var b2 = this.createKendoSmallIconTagTemplate("cancel", "kendoKipHelperInstance.closeHelperKendoWindow()", "Cancel");
			var butTable = "<br/> <table style='width:60%;margin-left:20%;'><tr><td>" + b1 + "</td><td>" + b2 + "</tr></table>";
			this.initKendoWindow();
			this.kendoWindow.content(message + butTable);
			this.kendoWindow.open();
	 },
	 createContextEditButton : function(hasText){
		 if(hasText){
			 return '<center><a class="k-button k-button-icontext k-grid-edit" href="#"  style="min-width:28px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210)">&nbsp;&nbsp;<span class="k-icon k-edit">Edit</span></a></center>';
		 }else{
			 return '<center><a class="k-button k-button-icontext k-grid-edit" href="#"  style="min-width:28px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210)">&nbsp;&nbsp;<span class="k-icon k-edit"></span></a></center>';
		 }
	 },
	 createSearchButton : function(hasText, onClick){
		 if(hasText){
			 return "<center><a onClick='" + onClick +"' class='k-button' href='#'  style='min-width:28px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210)'><span class='k-icon k-i-search'>Search</span></a></center>";
		 }else{
			 return "<center><a onClick='" + onClick +"' class='k-button' href='#'  style='min-width:28px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210)'><span class='k-icon k-i-search'></span></a></center>";
		 }
	 },
	 createDeleteButton : function(hasText, onClick){
		 if(hasText){
			 return "<center><a onclick='"+ onClick +"' class='k-button' style='min-width:28px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210)'><span class='k-icon k-i-close'></span>Delete</a></center>";
		 }else{
			 return "<center><a onclick='"+ onClick +"' class='k-button' style='min-width:28px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210)'><span class='k-icon k-i-close'></span></a></center>";
		 }
	 },
	 createSimpleHunterButton : function(iconName,text, onClick){
		 if(text != null){
			 return "<center><a onclick='"+ onClick +"' class='k-button' style='min-width:28px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210)'><span class='k-icon k-i-"+ iconName +"'></span>"+ text +"</a></center>";
		 }else{
			 return "<center><a onclick='"+ onClick +"' class='k-button' style='min-width:28px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210)'><span class='k-icon k-i-"+ iconName +"'></span></a></center>";
		 }
	 },
	 showOKCancelToAssociateWorkflowConfig : function(configId, groupId, message){
		 	var params = configId + '","' + groupId;
			var b1 = this.createKendoSmallIconTagTemplate("close", 'workflowConfigVM.updateDSAndSyncForAssociateTree("' + params + '")', "Associate");
			var b2 = this.createKendoSmallIconTagTemplate("cancel", "kendoKipHelperInstance.closeHelperKendoWindow()", "Cancel");
			var butTable = "<br/> <table style='width:80%;margin-left:10%;'><tr><td>" + b1 + "</td><td>" + b2 + "</tr></table>";
			this.initKendoWindow();
			this.kendoWindow.content(message + butTable);
			this.kendoWindow.open();
	 },
	popupWarning : function(message, msgTyp){
		$("#kendoWarningPopup").html(message);
		$("#kendoWarningPopup").kendoNotification({
	    	position: {
	            pinned: true,
	            top: 53,
	            right: 10
	        },
	        autoHideAfter: 3000,
	        stacking: "down",
	        width: 300,
	        height: 80, 
	        templates: [
	         {
	            type: "upload-success",
	            template: $("#successTemplate").html()
	        }]
	    });
	    var notificationWidget = $("#kendoWarningPopup").data("kendoNotification");
	    if(notificationWidget != null)
		    notificationWidget.show({
		        message: message
		    }, "upload-success");
	    else
	    	console.log("Error, cannot show notification  sinces it's null!!");
	},
	constainsJavascriptString : function(main,sub){
		var index = main.indexOf(sub);
		console.log("index found >> " + index);
		var contains = index == -1 ? false : true;
		return contains;
	},
	ajaxLoadEleWithContents : function(elementId, url){
		console.log("loading content from URL(" + url +")"); 
		$.get(url, function(contents) {
			  $('#' + elementId).append(contents);
			  console.log("Successfully loaded contents from URL(" + url +")"); 
		});
	}
});


