
var kendoKipHelperInstance;
var hunterWindow;

var hunterClientUserModel = kendo.data.Model.define({
	id:"clientId",
	fields : {
		"userId" : {
			type : "number", validation : {required : true},editable:false, defaultValue:"0"
		},
		"clientTotalBudget" : {
			type : "number", validation : {required : true},editable:false, defaultValue:"0"
		},
		"lastName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"middleName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"email" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"phoneNumber" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"userType" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"userName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"password" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"clientTotalBudget" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"receiver" : {
			type : "boolean", validation : {required : true},editable:false, defaultValue:"0"
		},
		"tasks" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"cretDate" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"createdBy" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"lastUpdate" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		},
		"lastUpdatedBy" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"0"
		}
	}
});


var taskRegionsModel = kendo.data.Model.define({
		id : "regionId",
		fields : {
			"regionId" : {
				type : "number", validation : {required : true},editable:false, defaultValue:0
			},
			"country" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"state" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"hasState" : {
				type : "boolean", validation : {required : true},editable:false, defaultValue:null
			},
			"county" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"constituency" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"city" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"ward" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"village" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"currentLevel" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"borderLongLats" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"currentLevel" : {
				type : "string", validation : {required : true},editable:false, defaultValue:null
			},
			"receiverCount" : {
				type : "count", validation : {required : true},editable:false, defaultValue:0
			}
		},
		getRegionsDeleteTemplate : function(){
			
			var id = this.get("regionId");
			var country = this.get("county");
			var county = this.get("country");
			var cons = this.get("constituency");
			var consWard = this.get("ward");
			
			var regionName = country + '/' + county + "/" + cons  + "/" + consWard;
			
			var idString = '"'+ id +'",';
			var message = '"<u><b>'+ regionName +'</u></b> will be deleted. <br/> Are you sure?"';
			var html = kendoKipHelperInstance.createDeleteButton(false, 'kendoKipHelperInstance.showOKCancelToDeleteTaskRegion('+  idString + message+ ')');
			return html;
		}
});


var hunterUserModel = kendo.data.Model.define({
	id:"userId",
	fields : {
		"userId" : {
			type : "number", validation : {required : true},editable:false, defaultValue:"0"
		},
		"firstName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"lastName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"middleName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"email" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"phoneNumber" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"userType" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"userName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"password" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"addresses" : {
			type : "object", validation : {required : true},editable:true, defaultValue:null
		},
		"creditCards" : {
			type : "object", validation : {required : true},editable:true, defaultValue:null
		},
		"cretDate" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"createdBy" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"lastUpdatedBy" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"lastUpdate" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"lastUpdatedBy" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		}
	},
	getHunterUserDeleteButton : function(){
		  var id = this.get("id");
		  var firstName = this.get("firstName");
		  var lastName = this.get("lastName");
		  var idString = '"'+ id +'",';
		  var message = '"<b>' + firstName + ' ' + lastName + '</b> will be deleted.<br/> Are you sure?"';
		  var html = kendoKipHelperInstance.createDeleteButton(false, 'kendoKipHelperInstance.showOKCancelToDeleteHunterUser('+  idString + message+ ')',"Delete");
		  return html;
	},
	getHunterUserCheckBox : function(){
		var id = "hunter_model_check_box_class_" + this.get("userId");
		var clazz = "hunter_model_check_box_class";
		var onChange = "hunterAdminClientUserVM.unCheckOthersAndSaveSelected(this)";
		var booleanChecked = false;
		var booleanDisabled = false;
		var html = kendoKipHelperInstance.createCheckBox(id, clazz, onChange, booleanChecked, booleanDisabled);
		return html;
	},
	getEditTemplate : function(){
		return kendoKipHelperInstance.createContextEditButton(false);
	},
	getClientTemplate : function(){
		var id = this.get("userId");
		return "<center><a cursor='ponter' style='color:blue;cursor:pointer'  id='"+ id +"' onClick='hunterAdminClientUserVM.loadSelUserTasksDetails(\""+ id +"\")'  >Client</a></center>";
	}
});

var hunterClientDS = new kendo.data.DataSource({
	  transport: {
	    read:  {
	      url: "http://localhost:8080/Hunter/client/action/read",
	      dataType: "json",
	      contentType:"application/json",
	      method: "POST"
	    },
	    create: {
	        url: "http://localhost:8080/Hunter/client/action/create",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST",
	        success: function(result) {
	            kendoKipHelperInstance.popupWarning('', JSON.stringify(result), "Success");
	         }
	    },
	    update: {
	        url: "http://localhost:8080/Hunter/client/action/update",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    destroy: {
	        url: "http://localhost:8080/Hunter/client/action/destroy",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    parameterMap: function(options, operation) {
	          if (operation == "read") {
	        	  console.log("Reading data...");
	          }else if(operation == "destroy"){ 
	        	  var json = kendo.stringify(options);
	        	  var id = json.id;
	        	  console.log("id >> " + id);
	        	  return json;
	          }else{
	        	  var json = kendo.stringify(options); 
	        	  return json;
	          }
	    }
	  },
	  schema: {
	    	model:hunterClientUserModel
	  },
	  error: function (e) {
	        kendoKipHelperInstance.popupWarning(e.status, e.errorThrown, "Error");
	  },
	  requestStart: function(e) {
	        var type = e.type;
	        var message = "";
	        if(type === 'read')
	        	return;
	        if(type === 'update')
	        	message = "Updating record...";
	        if(type === 'destroy')
	        	message = "Deleting record...";
	        if(type === 'create')
	        	message = "Creating record...";
	        kendoKipHelperInstance.popupWarning(message, "Success");
	  },
	  requestEnd: function(e) {
	        var response = e.response;
	        var type = e.type;
	        var message = "";
	        if(type === 'read')
	        	if(response !== 'undefined' && response != null){
	        		message = "Successfully obtained ( "+ response.length +" ) records!";
	        	}else{
	        		return;
	        	}
	        if(type === 'update')
	        	message = "Successfully updated record!";
	        if(type === 'destroy')
	        	message = "Successfully deleted record!";
	        if(type === 'create')
	        	message = "Successfully created record!";
	        
	        kendoKipHelperInstance.popupWarning(message, "Success");
	  },
	  pageSize:1000
});

var serviceProviderDS = new kendo.data.DataSource({
	  pageSize: 20,
	  sort: { field: "cretDate", dir: "desc" },
	  transport: {
	    read:  {
	      url: "http://localhost:8080/Hunter/message/action/providers/read",
	      dataType: "json",
	      accept: "application/json",
	      contentType:"application/json; charset=utf-8",
	      processdata : false,
	      method: "POST"
	    },
	    create: {
	        url: "http://localhost:8080/Hunter/message/action/providers/create",
	        dataType: "json",
	        Accept: "application/json",
	        contentType:"application/json",
	        method:"POST",
	        success: function(result) {
	            kendoKipHelperInstance.popupWarning(JSON.stringify(result), "Success");
	         }
	    },
	    update: {
	        url: "http://localhost:8080/Hunter/message/action/providers/update",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    destroy: {
	        url: "http://localhost:8080/Hunter/message/action/providers/destroy",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    parameterMap: function(options, operation) {
	    	console.log("Operation >> " + operation);
	          if (operation == "read") {
	        	  kendoKipHelperInstance.popupWarning("Reading data!");
	          }else {
	        	 var json = JSON.stringify(options);
	        	 return json;
	          }
	    }
	  },
	  schema: {
	    	model:{ 
	    		id:"providerId",
	    		fields : {
	    			"providerId" : {
	    				type : "number", validation : {required : true},editable:false, defaultValue:"0"
	    			},
	    			"providerName" : {
	    				type : "string", validation : {required : true},editable:false, defaultValue:null
	    			},
	    			"cstPrAudMsg" : {
	    				type : "number", validation : {required : true},editable:false, defaultValue:0
	    			},
	    			"cstPrTxtMsg" : {
	    				type : "number", validation : {required : true},editable:false, defaultValue:0
	    			}
	    		}
	    	}
	  },
	  error: function (e) {
	        kendoKipHelperInstance.popupWarning(e.errorThrown, "Success");
	  },
	  requestStart: function(e) {
	        var type = e.type;
	        var message = "";
	        if(type === 'read')
	        	return;
	        if(type === 'update')
	        	message = "Updating record...";
	        if(type === 'destroy')
	        	message = "Deleting record...";
	        if(type === 'create')
	        	message = "Creating record...";
	        
	        kendoKipHelperInstance.popupWarning(message, "Success");
	  },
	  requestEnd: function(e) {
	        var response = e.response;
	        var type = e.type;
	        var message = "";
	        if(type === 'read'){
	        	if(response !== 'undefined' && response != null){
	        		message = "Successfully obtained ( "+ response.length +" ) records!";
	        	}else{
	        		return;
	        	}
	        	hunterAdminClientUserVM.set("selUserId", null);
	        }
	        
	        if(type === 'update')
	        	message = "Successfully updated record!";
	        if(type === 'destroy')
	        	message = "Successfully deleted record!";
	        if(type === 'create')
	        	message = "Successfully created record!";
	        
	        kendoKipHelperInstance.popupWarning(message, "Success");
	        
	  }
});


var hunterUserDS = new kendo.data.DataSource({
	  pageSize: 20,
	  sort: { field: "cretDate", dir: "desc" },
	  transport: {
	    read:  {
	      url: "http://localhost:8080/Hunter/hunteruser/action/read/post",
	      dataType: "json",
	      accept: "application/json",
	      contentType:"application/json; charset=utf-8",
	      processdata : false,
	      method: "POST"
	    },
	    create: {
	        url: "http://localhost:8080/Hunter/hunteruser/action/create",
	        dataType: "json",
	        Accept: "application/json",
	        contentType:"application/json",
	        method:"POST",
	        success: function(result) {
	            kendoKipHelperInstance.popupWarning(JSON.stringify(result), "Success");
	         }
	    },
	    update: {
	        url: "http://localhost:8080/Hunter/hunteruser/action/update",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    destroy: {
	        url: "http://localhost:8080/Hunter/hunteruser/action/destroy",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    parameterMap: function(options, operation) {
	    	console.log("Operation >> " + operation);
	          if (operation == "read") {
	        	  kendoKipHelperInstance.popupWarning("Reading data!");
	          }else if(operation === "destroy"){ 
	        	  var json = kendo.stringify(options);
	        	 /* 
	        	  	This is important!! 
	        	 	If the selected user is deleted, the selected user must be cleared from observable. 
	        	 */
	        	  var id = jQuery.parseJSON(json)["userId"];
	        	  var selUserId = hunterAdminClientUserVM.get("selUserId");
	        	  console.log("Selected User Id >> " + selUserId);
	        	  if(id !== "undefined" && "hunter_model_check_box_class_" + id == selUserId + "" ){ // this needed to be converted to string
	        		  console.log("Deleted selected user. Clearing selected user!");
	        		  hunterAdminClientUserVM.set("selUserId", null);
	        	  }else{
	        		  console.log("id >> " + id);
	        	  }
	        	  return json;
	          }else{ 
	        	 var json = JSON.stringify(options);
	        	 return json;
	          }
	    }
	  },
	  schema: {
	    	model:hunterUserModel
	  },
	  error: function (e) {
	        alert(e.errorThrown);
	  },
	  requestStart: function(e) {
	        var type = e.type;
	        var message = "";
	        if(type === 'read')
	        	return;
	        if(type === 'update')
	        	message = "Updating record...";
	        if(type === 'destroy')
	        	message = "Deleting record...";
	        if(type === 'create')
	        	message = "Creating record...";
	        
	        kendoKipHelperInstance.popupWarning(message, "Success");
	  },
	  requestEnd: function(e) {
	        var response = e.response;
	        var type = e.type;
	        var message = "";
	        if(type === 'read'){
	        	if(response !== 'undefined' && response != null){
	        		message = "Successfully obtained ( "+ response.length +" ) records!";
	        	}else{
	        		return;
	        	}
	        	hunterAdminClientUserVM.set("selUserId", null);
	        }
	        
	        if(type === 'update')
	        	message = "Successfully updated record!";
	        if(type === 'destroy')
	        	message = "Successfully deleted record!";
	        if(type === 'create')
	        	message = "Successfully created record!";
	        
	        kendoKipHelperInstance.popupWarning(message, "Success");
	        
	  }
});

var hunterTaskModel = kendo.data.Model.define({
	id:"taskId",
	fields : {
		"taskId" : {
			type : "number", validation : {required : true},editable:false, defaultValue:"0"
		},
		"taskType" : {
			type : "object", validation : {required : true},editable:true, defaultValue:null
		},
		"taskName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"taskObjective" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"description" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"taskName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"taskBudget" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"desiredReceiverCount" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"availableReceiverCount" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"confirmedReceiverCount" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"taskCost" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"recurrentTask" : {
			type : "boolean", validation : {required : true},editable:true, defaultValue:false
		},
		"taskDateline" : {
			type : "string", validation : {required : false},editable:false, defaultValue:new Date()
		},
		"taskLifeStatus" : {
			type : "string", validation : {required : false},editable:true, defaultValue:"Draft"
		},
		"taskDeliveryStatus" : {
			type : "string", validation : {required : false},editable:true, defaultValue:"Conceptual"
		},
		"taskApproved" : {
			type : "boolean", validation : {required : false},editable:true, defaultValue:false
		},
		"taskApprover" : {
			type : "string", validation : {required : false},editable:true, defaultValue:null
		},
		"gateWayClient" : {
			type : "object", validation : {required : false},editable:true, defaultValue:null
		},
		"cretDate" : {
			type : "string", validation : {required : false},editable:true, defaultValue:new Date()
		},
		"createdBy" : {
			type : "string", validation : {required : false},editable:true, defaultValue:null
		},
		"lastUpdate" : {
			type : "string", validation : {required : false},editable:true, defaultValue:new Date()
		},
		"lastUpdatedBy" : {
			type : "string", validation : {required : false},editable:true, defaultValue:null
		},
		"taskMessage" : {
			type : "object", validation : {required : false},editable:true, defaultValue:null
		},
		"taskRegion" : {
			type : "object", validation : {required : false},editable:true, defaultValue:null
		},
		"taskRegions" : {
			type : "object", validation : {required : false},editable:true, defaultValue:null
		},
		"taskReceivers" : {
			type : "object", validation : {required : false},editable:true, defaultValue:null
		}
	},
	getTaskLoadDetailsTemplate : function(){
		var id = this.get("taskId");
		  var idString = '"'+ id +'"';
		  var onClick = 'hunterAdminClientUserVM.showDetails('+  idString + ')';
		var html = kendoKipHelperInstance.createSearchButton(false, onClick);
		return html;
	},
	getEditTaskTemplate : function(){
		return kendoKipHelperInstance.createContextEditButton(false);
	},
	getTaskDeleteTemplate : function(){
		var id = this.get("taskId");
		var taskName = this.get("taskName");
		var idString = '"'+ id +'",';
		var message = '"<b>' + taskName  + '</b> will be deleted.<br/> Are you sure?"';
		var html = kendoKipHelperInstance.createDeleteButton(false, 'kendoKipHelperInstance.showOKCancelToDeleteHunterTask('+  idString + message+ ')');
		return html;
	},
	getTaskProcessTemplate : function(){
		var id = this.get("taskId");
		var idString = "\""+ id +"\"";
		var html = kendoKipHelperInstance.createSimpleHunterButton("arrow-e",null, "hunterAdminClientUserVM.showPopupForProcessTask("+ idString +")" );
		return html;
	},
	getTaskEditStatusTemplate : function(){
		var currentStatus = this.get("taskLifeStatus");
		var keyStr = this.get("taskId") + ":::" + currentStatus;
		var html = "<center><span style='color:blue;'><a style='cursor:pointer' onClick='hunterAdminClientUserVM.showPopupToChangeTaskStatus(\""+ keyStr +"\")' >"+ currentStatus +"</a></span></center>";
		return html;
	}, 
	getTaskRegionTemplate : function(){
		var id = this.get("taskId"); 
		var html = kendoKipHelperInstance.createSimpleHunterButton("group",null, "hunterAdminClientUserVM.loadTaskRegionView("+ id +")" );
		return html;
	},
	getTaskMessageTemplate : function(){
		var id = this.get("taskId"); 
		var html = kendoKipHelperInstance.createSimpleHunterButton("folder-up",null, "hunterAdminClientUserVM.loadTaskMessageView("+ id +")" );
		return html;
	},
	getProgressTemplate : function(){
		var dStatus = this.get("taskDeliveryStatus"); 
		return "<center><a style='color:blue; cursor:pointer' onClick='hunterAdminClientUserVM.loadTaskMessageView(\""+ dStatus +"\")'  >"+ dStatus +"</a></center>";
	}
});

var hunterAdminClientUserVM = kendo.observable({
	
	userGrid : null,
	selUserId : null,
	clientBean : {},
	draftClientBean : {},
	wasClientUpdated : false,
	isTaskOpen : false,
	hunterClientEditValues : {},
	hunterTaskDS : null,
	taskTypeDsData : ["Polytical", "Educational", "Corporate", "Testing"],
	hunterClientTaskGrid : null,
	tabStripWidget : null,
	taskTypeDs : null,
	
	isEverVisible : true,
	isNeverVisible : false,
	isEverEnabled : true,
	isNeverEnabled : false,
	
	taskMsgSendDate : null,
	taskMsgTypeVal : null,
	desiredReceivers : null,
	actualReceivers : null, 
	confirmedReceivers : null,
	tskMsgId : null, 
	tskMsgDelStatus : null,
	tskMsgLifeStatus: null,
	tskMstTxt : null,
	tskMsgCretDate : null,
	tskMsgLstUpdate : "05/12/2045",
	tskMsgLstUpdatedBy : null,
	tskMsgProvider : null,
	
	
	
	onChangeTaskMsgSendDate : function(e){
		console.log(this.get("taskMsgSendDate")); 
	},
	onChangeServiceProvider : function(e){
		console.log(this.get("providerId"));
	},
	
	beforeInit : function(){
		kendoKipHelperInstance = new kendoKipHelper();
		kendoKipHelperInstance.init();
		console.log("Preparing to initialized hunterAdminClientUserVM...");
		this.prepareTabStrips();
	},
	init : function(){
		console.log("Initializing hunterAdminClientUserVM...");
		this.beforeInit();
		kendo.bind($("body"), hunterAdminClientUserVM);
		this.afterInit();
		this.initRegionVM();
	},
	afterInit : function(){
		this.createUserGrid();
		this.createTaskTypesDS();
		console.log("hunterAdminClientUserVM successfully initialized!!");
	},
	alertUpdate : function(){
		alert("updateClicked"); 
	},
	prepareTabStrips : function(){
		console.log("Preparing client tab strips...");
		var tabStripWidget = $("#hunterUserDetailsStrip").kendoTabStrip({
	         animation:  {
	             open: {
	                 effects: "fadeIn"
	             }
	         },
	         dataSpriteCssClass: "spriteCssClass"
	     }).data("kendoTabStrip");
		this.set("tabStripWidget", tabStripWidget); 
		console.log("Successfully prepared client tab strips!!");
		
		// hide message and region tab contents. 
		//this.slideUpTaskTabs();
	}/*,
	slideUpTaskTabs : function(){
		$("#taskRegionStrip").slideUp();
		$("#taskMessageStrip").slideUp();
	}*/,
	showTabStripNumber : function(n){
		var tabstrip = this.get("tabStripWidget"); 
		tabstrip.select(n);
	},
	createTaskTypesDS : function(){
		var taskTypeDS = new kendo.data.DataSource({
			data : HunterConstants.TASK_TYPES_ARRAY
		}); 
		this.set("taskTypeDs", taskTypeDS);
		console.log("successfully created task types DS !!"); 
	},
	deleteSelectedUser : function(id){
		var grid = this.get("userGrid");
		var mDS = grid.dataSource;
		var model = mDS.get(id);
		mDS.remove(model);
		mDS.sync();
		kendoKipHelperInstance.popupWarning("Successfully deleted!", "Success"); 
		kendoKipHelperInstance.closeHelperKendoWindow();
	},
	deleteSelectedTask  : function(id){
		console.log("deleting selected task >> " + id);
		var taskDs = this.get("hunterClientTaskGrid").dataSource;
		var model = taskDs.get(id);
		taskDs.remove(model);
		taskDs.sync();
		kendoKipHelperInstance.closeHelperKendoWindow();
	},
	unCheckOthersAndSaveSelected  : function(this_){
		var thisElement = $(this_);
		var id = $(this_).attr("id");
		this.set("selUserId", id);
		var clazz = $(".hunter_model_check_box_class");
		if(thisElement.prop( "checked" )){
			$( clazz ).attr( "checked", false );
			thisElement.prop( "checked",true );
		}else{
			this.set("selUserId", null);
		}
		console.log("Selected >> " + this.get("selUserId")); 
	},
	loadSelUserDetails : function(){
		
		var isRegionViewOpen = this.get("isTaskRegionOpen");
		
		if(isRegionViewOpen){
			this.canceltEditRegionAndClose();
			this.set("isTaskRegionOpen",false);
			console.log("Successfull closed task region view !!!"); 
		}
		
		var userId = this.get("selUserId"); 
		if(userId == null){
			kendoKipHelperInstance.showSimplePopup("Please select user.", "No user is selected. Please select user first.<br/>");
			return;
		}
		userId = userId.replace("hunter_model_check_box_class_","");
		var userIdData = {"userId":userId};
		$.ajax({
			url: "http://localhost:8080/Hunter/client/action/getClientForUserId",
		      data : userIdData,
		      method: "POST"
		}).done(function(data) {
			var json = jQuery.parseJSON(data);
			hunterAdminClientUserVM.updateClientDetails(json);
			hunterAdminClientUserVM.refreshHunterTaskDS(json);
			hunterAdminClientUserVM.createTasksGrid(json);
			kendoKipHelperInstance.popupWarning("Successfully loaded user!", "Success");
		 }).fail(function(data) {
			 var json = jQuery.parseJSON(data);
			 console.log("Failed to load user details!!!!!!!");
			 console.log(json);
			 kendoKipHelperInstance.popupWarning(data.statusText + " (" + json.status + ")", "Error");
		 });
		
		
		// After loading user details, force tabstrip to show the loaded tab.
		this.showTabStripNumber(0);
		
	},
	loadSelUserDetailsAndCollapseGrid : function(){
		var userId = this.get("selUserId");
		if(userId == null){
			kendoKipHelperInstance.showSimplePopup("Please select user.", "No user is selected. Please select user first.<br/>");
			return;
		}
		
		// update name when grid is closed.
		var userId_ = userId.replace("hunter_model_check_box_class_","");
		var model = this.get("userGrid").dataSource.get(userId_);
		var fullName = model.get("firstName") + "&nbsp;&nbsp;&nbsp;" + model.get("lastName"); 
		$("#selectedUserNameSpan").html(fullName);
		
		// then load user details in the bottom pane.
		this.loadSelUserDetails();
		
		$("#hunterUserGrid").slideUp(800, function(){
			$("#expandGridButtonDiv").removeClass("hidden");
		});
	},
	expandHunterUserGrid : function(){
		$("#expandGridButtonDiv").addClass("hidden");
		$("#hunterUserGrid").slideDown(800, function(){
		});
	},
	loadSelUserTasksDetails : function(id){
		console.log(id);
		var clientBean = this.get("clientBean"); 
		var clientId = clientBean["clientId"];
		if(clientBean == null || clientId != id ){
			kendoKipHelperInstance.showSimplePopup("Warning!", "Please load this user first!"); 
			return;
		}
		var html = $("#clientDetailsTemplateDiv").html();
		var template = kendo.template(html);
		var contents = template(clientBean);
		kendoKipHelperInstance.showBackUpPercentMeasuredOKCancelTitledPopup(contents, "Task Details", "35", "25");
		$("#clientTotalBudget").kendoNumericTextBox({
		    downArrowText: "Less"
		});
	},
	ajaxSaveClientDetails : function(clientDetails){
		console.log("making ajax request to update client >> " + JSON.stringify(clientDetails));
		$.ajax({
			url: "http://localhost:8080/Hunter/client/action/editHunterClient",
		      data : clientDetails,
		      method: "POST"
			}).done(function(data) {
				var json = jQuery.parseJSON(data);
				if(json.status !== 'Success'){
					kendoKipHelperInstance.popupWarning(json.status + "! " + json.message, "Error");
				}else{
					kendoKipHelperInstance.popupWarning(json.message, "Success");
				}
			 }).fail(function(data) {
				 var json = jQuery.parseJSON(data);
				 kendoKipHelperInstance.popupWarning(data.statusText + " (" + json.status + ")", "Error");
			 });
		
	},
	saveDraftClientChanges : function(){
		
		var value = $("#clientTotalBudget").val();
		var checked = $("#receiver").prop("checked"); 
		var clientBean = this.get("clientBean"); 
		
		clientBean["clientTotalBudgetValue"] = value;
		clientBean["receiverValue"] = checked;
		clientBean["clientTotalBudget"] = this.getClientBudgetHtmlWithValue(value);
		clientBean["receiver"] = this.getCheckBoxWithValue(checked ? 'checked="checked" ' : '');
		console.log("client bean after changes >>> " + JSON.stringify(clientBean));
		
		var lastClientBean = {"clientTotalBudget" : value, "receiver" : checked, "clientId" :  clientBean["clientId"]};
		this.ajaxSaveClientDetails(lastClientBean);
		
		kendoKipHelperInstance.closeMeasuredWindowBackUp();
		
	},
	updateClientDetails : function(json){
		var clientBean = this.get("clientBean");
		var checked = json.receiver ? 'checked="checked" ' : '';
		clientBean["clientId"] = json.clientId;
		clientBean["clientTotalBudget"] = this.getClientBudgetHtmlWithValue(json.clientTotalBudget);
		clientBean["clientTotalBudgetValue"] = json.clientTotalBudget;
		clientBean["receiver"] = this.getCheckBoxWithValue(checked);
		clientBean["receiverValue"] = json.receiver;
		clientBean["cretDate"] = json.cretDate;
		clientBean["createdBy"] = json.createdBy;
		clientBean["lastUpdate"] = json.lastUpdate;
		clientBean["lastUpdatedBy"] = json.lastUpdatedBy;
		this.setClientBean(clientBean);
	},
	getClientBudgetHtmlWithValue : function(value){
		return '<input id="clientTotalBudget" value="'+ value +'" />';
	},
	getCheckBoxWithValue : function(checked){
		return '<input id="receiver" type="checkbox"  '+ checked +' />';
	},
	setClientBean : function(clientBean){
		this.set("clientBean", clientBean);
	},
	refreshHunterTaskDS : function(json){
		//var tasks = json.tasks;
		var clientId = json.clientId;
		console.log("Creating dataSource for client Id >> " + clientId);
		var hunterTaskDS_ = new kendo.data.DataSource({
			  pageSize: 20,
			  transport: {
				    read:  {
				      url: "http://localhost:8080/Hunter/task/action/read/getTasksForClientId/" + clientId,
				      dataType: "json",
				      method: "POST"
				    },
				    create: {
				        url: "http://localhost:8080/Hunter/task/action/create/createTaskForClientId",
				        dataType: "json", 
				        contentType:"application/json",
				        processData : false,
				        method:"POST",
				        success: function(result) {
				            kendoKipHelperInstance.popupWarning('', JSON.stringify(result), "Success");
				         }
				    },
				    update: {
				        url: "http://localhost:8080/Hunter/task/action/update/updateTaskForClientId",
				        dataType: "json", 
				        contentType:"application/json",
				        method:"POST"
				    },
				    destroy: {
				        url: "http://localhost:8080/Hunter/task/action/destroy/destroyTaskForClientId",
				        dataType: "json", 
				        contentType:"application/json",
				        method:"POST"
				    },
				    parameterMap: function(options, operation) {
				          if (operation == "read") {
				        	  console.log("Reading data...");
				          }else if(operation === "destroy" || operation === "update" || operation === "create"){ 
				        	  var json1 = kendo.stringify(options); 
				        	  json = jQuery.parseJSON(json1);
				        	  if(operation === "create"){
				        		  json["clientId"] = clientId;
				        		  console.log(JSON.stringify(json)); 
				        		  var taskType = options.taskType["value"];
				        		  console.log("Task type value !! >> " + taskType);
				        		  json["taskType"] = taskType;
				        		  var gateWayClient = options.gateWayClient["value"];
				        		  console.log("gateWayClient>>>>>> " + gateWayClient); 
				        		  console.log("Task gateWayClient value !! >> " + gateWayClient);
				        		  json["gateWayClient"] = gateWayClient;
				        	  }
				        	  json1 = JSON.stringify(json);
				        	  console.log("JSON for update, create, destroy >> " + json1); 
				        	  return json1;
				          }else{
				        	  kendoKipHelperInstance.popupWarning("Bad operation >> " + operation,"Error");
				          }
				    }
			  },
			  sort: { field: "cretDate", dir: "desc" },
			  schema: {
			    	model:hunterTaskModel
			  },
			  requestStart: function(e) {
			        var type = e.type;
			        var message = "";
			        if(type === 'read')
			        	return;
			        if(type === 'update')
			        	message = "Updating record...";
			        if(type === 'destroy')
			        	message = "Deleting record...";
			        if(type === 'create')
			        	message = "Creating record...";
			        kendoKipHelperInstance.popupWarning(message, "Success");
			  },
			  requestEnd: function(e) {
			        var response = e.response;
			        var type = e.type;
			        var message = "";
			        if(type === 'read')
			        	if(response !== 'undefined' && response != null){
			        		message = "Successfully obtained ( "+ response.length +" ) records!";
			        	}else{
			        		return;
			        	}
			        if(type === 'update')
			        	message = "Successfully updated record!";
			        if(type === 'destroy')
			        	message = "Successfully deleted record!";
			        if(type === 'create')
			        	message = "Successfully created record!";
			        
			        kendoKipHelperInstance.popupWarning(message, "Success");
			  }
		});
		this.set("hunterTaskDS", hunterTaskDS_);
	},
	destroyHunterClientTaskGrid : function(){
		$("#noDataH3ForHunterClientDetails").remove();
		var taskGrid = this.get("hunterClientTaskGrid");
		if(taskGrid == null){
			console.log("No tasks grid widget found. Returning..."); 
			return;
		}
		taskGrid.destroy();
		$("#selectedUserClientTasks").empty();
		console.log("Successfully destroyed tasks grid widget!!"); 
	},
	showPopupToChangeTaskStatus : function(keyStr){
		
		console.log("Key String >> " + keyStr);
		
		var components = keyStr.split(":::");
		var id = components[0];
		var currentStatus = components[1];
		var includePreview = false;
		var step = "";
		
		if(currentStatus == "Draft"){
			step = "<span><b><u>Review</b></u></span>";
			includePreview = true;
		}else if(currentStatus == "Review"){
			step = "<span><b><u>Approved</b></u></span>";
			includePreview = false;
		}else if(currentStatus == "Approved"){
			step = "<span><b><u>Review</b></u></span>";
			includePreview = true;
		}
		
		var message = "Please click status button to move Task to <b><u>" + step + " </b></u> or back to <b><u>Draft</b></u><br/><br/> ";
		
		var preTab = "<table style='with:50%;margin-left:20%;' ><tr>";
		var toDraft = "<td><button style='background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);'  class='k-button' onClick=hunterAdminClientUserVM.updateStatusForSelectedTask('Draft:::"+ id +"') >Draft</button></td>";
		var toReview = "<td><button style='background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);' class='k-button' onClick=hunterAdminClientUserVM.updateStatusForSelectedTask('Review:::"+ id +"') >Review</button></td>";
		var toApproved = "<td><button style='background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);' class='k-button' onClick=hunterAdminClientUserVM.updateStatusForSelectedTask('Approved:::"+ id +"') >Approved</button></td>";
		var cancel = "<td><button style='background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);' class='k-button' onClick=kendoKipHelperInstance.closeHelperKendoWindow()>Cancel</button></td>";
		var postTab = "</tr></table>";
		
		var reviewOrApproved = "";
		
		if(includePreview){
			reviewOrApproved = toReview;
		}else{
			reviewOrApproved = toApproved;
		}
		
		var finalStr = message + preTab + toDraft + reviewOrApproved + cancel + postTab;
		
		kendoKipHelperInstance.showPopupWithNoButtons("Change status!", finalStr);
		
	},
	updateStatusForSelectedTask : function(keyStr){
		var components = keyStr.split(":::");
		var toStatus = components[0];
		var id = components[1];
		console.log("Current status = " + toStatus);
		console.log("Task Id = " + id);
		var tskGridDs = this.get("hunterClientTaskGrid").dataSource;
		var model = tskGridDs.get(id);
		model.set("taskLifeStatus", toStatus);
		tskGridDs.sync();
		kendoKipHelperInstance.closeHelperKendoWindow();
	},
	loadTaskRegionView: function(id){
		///this.showTabStripNumber(1);
		var msg = hunterAdminClientUserVM.get("hunterClientTaskGrid").dataSource.get(id).get("taskRegion"); 
		var msgTxt = msg != null ? msg["msgText"]  : "<span style='color:#F51A1A;' > No region found for this task! <br/> Would you like to create one? </span>";  
		console.log("Message Text >> " + msgTxt);
		var okButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("tick","OK", 'kendoKipHelperInstance.closeHelperKendoWindow()');
		var div = "<div style='height:80px;width:98%;margin-left:1%;border-radius:5px;padding:5px;'  class='k-block k-info-colored'>"+ msgTxt +"</div>" + "</td>";
		if(msg == null){
			var createButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("plus","Create", 'hunterAdminClientUserVM.loadNewTaskRegionView("'+ id +'")') + "</td>";
			var creatButtTable = div + "<br/><table style='width:50%;margin-left:25%;' ></tr>" + createButton + okButton + "</tr></table>";
			kendoKipHelperInstance.showPopupWithNoButtons("Task Region", creatButtTable);
			return;
		}
		var editButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("pencil","Edit", 'hunterAdminClientUserVM.loadEdiTaskRegionView("'+ id +'")') + "</td>";
		var details = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("search","Details", 'hunterAdminClientUserVM.loadDetailsTaskRegionView("'+ id +'")') + "</td>"; 
		var buttTable = div + "<br/><table style='width:70%;margin-left:15%;' ></tr>" + editButton + details + okButton + "</tr></table>";
		kendoKipHelperInstance.showPopupWithNoButtons("Task Message", buttTable);
	},
	loadNewTaskRegionView : function(taskId){
		hunterAdminClientUserVM.set("selTaskId", taskId);
		hunterAdminClientUserVM.getTotalReceivers();
		var taskRegionsDs = hunterAdminClientUserVM.get("selTaskRegionsDS");
		taskRegionsDs.read();
		hunterAdminClientUserVM.opendTaskRegionView();
		kendoKipHelperInstance.closeHelperKendoWindow();
		
		this.set("isTaskRegionOpen", true);
		
	},
	loadEdiTaskRegionView : function(taskId){
		kendoKipHelperInstance.closeHelperKendoWindow();
		this.loadNewTaskRegionView(taskId);
	},
	loadDetailsTaskRegionView : function(taskId){
		kendoKipHelperInstance.closeHelperKendoWindow();
		//this.showTabStripNumber(2);
		$("#taskGridHolder").hide(1000, function(){
			$("#taskRegionStrip").slideDown(1000);
		});
	},
	saveCurrentEditRegionAndClose : function(){
		var selCountry = hunterAdminClientUserVM.get("selCountry");
		kendoKipHelperInstance.popupWarning("Successfully saved!", "Success");
		return;
		this.closeTaskRegionView();
	},
	saveCurrentEditRegion : function(){
		
		kendoKipHelperInstance.popupWarning("Successfully saved!", "Success");
		var selTaskId = this.get("selTaskId"); 
		console.log("Adding task regions. Task Id = " + selTaskId);
		
		var selCountry = this.get("selCountry");
		var selState = this.get("selState");
		var selCounty = this.get("selCounty");
		var selConstituency = this.get("selConstituency");
		var selConstituencyWard = this.get("selConstituencyWard");
		
		var model = {
						"selCountry":selCountry,
						"selState":selState,
						"selCounty":selCounty,
						"selConstituency" : selConstituency,
						"selConstituencyWard" : selConstituencyWard,
						"selTaskId" : selTaskId
					};
		var json =  JSON.stringify(model);
		
		$.ajax({
			url: "http://localhost:8080/Hunter/region/action/task/regions/addTotask",
			dataType : "json",
			data : json,
			contentType : "application/json",
			accept: "application/json", 
			method: "POST"
		}).done(function(data) {
			console.log("Data returned for add region to Task : " + data);
			var ds = hunterAdminClientUserVM.get("selTaskRegionsDS");
			if(ds != null){
				ds.read();
			}
			hunterAdminClientUserVM.getTotalReceivers();
		 }).fail(function(data) {
			 var json = data + "";
			 kendoKipHelperInstance.popupWarning("Failed to load task receiver count ! " + data.statusText + " (" + json.status + ")", "Error");
		 });
		
		console.log("Successfully added regions to task!!!");
		
	},
	canceltEditRegionAndClose : function(){
		this.closeTaskRegionView();
	},
	closeTaskRegionView : function(){
		this.set("isTaskRegionOpen", false); // this is important!!
		$("#taskRegionStrip").slideUp(1000, function(){
			$("#hunterUserDetailsStrip").slideDown(1000);
		});
	},
	opendTaskRegionView : function(taskId){
		$("#hunterUserDetailsStrip").slideUp(1000, function(){
			hunterAdminClientUserVM.showTabStripNumber(2);
			$("#taskRegionStrip").slideDown(1000);
		});
	},
	loadTaskMessageView: function(id){
		///this.showTabStripNumber(1);
		var msg = this.get("hunterClientTaskGrid").dataSource.get(id).get("taskMessage"); 
		var msgTxt = msg != null ? msg["msgText"]  : "<span style='color:#F51A1A;' > No message found for this task! <br/> Would you like to create one? </span>";  
		console.log("Message Text >> " + msgTxt);
		var okButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("tick","OK", 'kendoKipHelperInstance.closeHelperKendoWindow()');
		var div = "<div style='height:80px;width:98%;margin-left:1%;border-radius:5px;padding:5px;'  class='k-block k-info-colored'>"+ msgTxt +"</div>" + "</td>";
		if(msg == null){
			var createButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("plus","Create", 'hunterAdminClientUserVM.loadNewTaskMessageView("'+ id +'")') + "</td>";
			var creatButtTable = div + "<br/><table style='width:50%;margin-left:25%;' ></tr>" + createButton + okButton + "</tr></table>";
			kendoKipHelperInstance.showPopupWithNoButtons("Task Message", creatButtTable);
			return;
		}
		var editButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("pencil","Edit", 'hunterAdminClientUserVM.loadEdiTaskMessageView("'+ id +'")') + "</td>";
		var details = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("search","Details", 'hunterAdminClientUserVM.loadDetailsTaskMessageView("'+ id +'")') + "</td>"; 
		var buttTable = div + "<br/><table style='width:70%;margin-left:15%;' ></tr>" + editButton + details + okButton + "</tr></table>";
		kendoKipHelperInstance.showPopupWithNoButtons("Task Message", buttTable);
	},
	loadEdiTaskMessageView : function(taskId){
		console.log("loading edit view for message id >> " + taskId);
		kendoKipHelperInstance.closeHelperKendoWindow();
		this.loadNewTaskMessageView(taskId);
	},
	loadDetailsTaskMessageView : function(taskId){
		console.log("loading details view for message id >> " + taskId);
		kendoKipHelperInstance.closeHelperKendoWindow();
		this.loadNewTaskMessageView(taskId);
	},
	saveCurrentEditMessageAndClose : function(){
		kendoKipHelperInstance.popupWarning("Successfully saved the changes.", "Success");
		var sendDate = $("#selTaskMsgSendDatePicker").val();
		console.log("Send Date > " + sendDate); 
		this.closeEditMessageView();
	},
	saveCurrentEditMessage : function(){
		kendoKipHelperInstance.popupWarning("Successfully saved the changes.", "Success");
	},
	closeEditMessageView : function(){
		$("#taskMessageStrip").slideUp(1000, function(){
			$("#hunterUserDetailsStrip").slideDown(1000);
		});
	},
	opendEditMessageView : function(){
		$("#hunterUserDetailsStrip").slideUp(1000,function(){
			$("#taskMessageStrip").slideDown(1000);
		});
	},
	canceltEditMessageAndClose : function(){
		this.closeEditMessageView();
	},
	loadNewTaskMessageView : function(taskId){
		console.log("loading create New Task Message view for message id >> " + taskId);
		kendoKipHelperInstance.closeHelperKendoWindow();
		//this.showTabStripNumber(1);
		var task = this.get("hunterClientTaskGrid").dataSource.get(taskId); 
		this.opendEditMessageView();
	},
	showPopupForProcessTask : function(taskId){
		var taskDesc = "<b>" + this.get("hunterClientTaskGrid").dataSource.get(taskId).get("description") + "</b>"; 
		console.log("Task description found >> " + taskDesc ); 
		var msgTxt = "Task ( " + taskDesc + " ) will be processed. Are you sure?<br/><br/>";
		var processButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("tick","process", 'hunterAdminClientUserVM.ajaxProcessTask("'+ taskId +'")') + "</td>";
		var cancelButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("cancel","cancel", 'kendoKipHelperInstance.closeHelperKendoWindow()') + "</td>";
		var creatButtTable = msgTxt + "<table style='width:56%;margin-left:22%;' ></tr>" + processButton + cancelButton + "</tr></table>";
		kendoKipHelperInstance.showPopupWithNoButtons("Task Message", creatButtTable);
	},
	ajaxProcessTask : function(taskId){
		kendoKipHelperInstance.closeHelperKendoWindow();
		var model =this.get("hunterClientTaskGrid").dataSource.get(taskId);
		$.ajax({
			url: "http://localhost:8080/Hunter/task/action/destroy/processTask",
		      data : JSON.stringify(model),
		      method: "POST"
		}).done(function(data) {
			var json = jQuery.parseJSON(data);
			var status = json.status;
			if(status == "Failed"){
				var errors = json.errors;
				errors = replaceAll(errors, ",", "</br>");  
				kendoKipHelperInstance.showSimplePopup("Error Processing Task!","<span style='color:red;' >" + errors + "</span>");
			}else if(status == null || status === ""){ 
				kendoKipHelperInstance.showSimplePopup("Success!!","<span style='color:green;' >Successfully processed Task!</span>");
			}
		 }).fail(function(data) {
			 var json = jQuery.parseJSON(data);
			 console.log("Failed to process task !!!!!!!");
			 console.log(json);
			 kendoKipHelperInstance.popupWarning(data.statusText + " (" + json.status + ")", "Error");
		 });
	},
	performTaskNumericKendoConversions : function(){
		$("#taskTaskBudgetNumericInput").kendoNumericTextBox({
		    downArrowText: "Less"
		});
		$("#taskTaskCostNumericInput").kendoNumericTextBox({
		    downArrowText: "Less"
		});
		$("#desiredReceiverCountInput").kendoNumericTextBox({
		    downArrowText: "Less"
		});
	},
	createTasksGrid : function(json){
		
		//numericize input
		this.performTaskNumericKendoConversions();
		
		// destroy pre-existing widgets, first. 
		this.destroyHunterClientTaskGrid();
		console.log("Creating user grid..");
		var tasksDS_ = this.get("hunterTaskDS");
		
		var hunterClientTaskGrid_ = $("#selectedUserClientTasks").kendoGrid({
			dataSource : tasksDS_,
			toolbar : kendo.template($("#hunterClientTasksToolBar").html()),
			pageable: {
                refresh: true,
                pageSizes: true,
                buttonCount: 5
            },
            height: 350,
            sortable: true,
            editable: {
				mode: "popup",
				template: kendo.template($("#hunterClientTasksPopupEditTemplate").html())
            },
            columns: [
               { field: "taskId", title: "ID", width: 30 },
               { field: "taskName", title: "Name", width: 150 },
               { field: "taskType", title: "Task Type", width: 150},
               { field: "gateWayClient", title: "Client", width: 100 },
               { field: "taskDateline", title: "Date Line", width: 120 },
               { field: "desiredReceiverCount", title: "Receivers", width: 65 },
               { field: "cretDate", title: "Created On", width: 120 },
               { field: "createdBy", title: "Created By", width: 150 },
               { field: "lastUpdate", title: "Last Motified", width: 120 },
               { field: "updatedBy", title: "Motified By", width: 150 },
               { field: "taskDeliveryStatus", title: "Progress", width: 80 , template : "#=getProgressTemplate()#" },
               { field: "taskLifeStatus", title: "Status", width: 70, template : "#=getTaskEditStatusTemplate()#"},
               { field: "message", title: "Message", width: 60 , template : "#=getTaskMessageTemplate()#" },
               { field: "taskRegion", title: "Region", width: 60,template : "#=getTaskRegionTemplate()#"  },
               { field: "loadDetails", title : "Details", width:60, template : "#=getTaskLoadDetailsTemplate()#"},
               { field: "editTask", title : "Edit", width:60, template : "#=getEditTaskTemplate()#"},
               { "name": "Delete", "title": "Delete","width": 60, template : "#=getTaskDeleteTemplate()#", filterable: false,resizable: false  },
               { "name": "Process", "title": "Process","width": 60, template : "#=getTaskProcessTemplate()#", filterable: false,resizable: false  }
               
            ]
		}).data("kendoGrid"); 
		this.set("hunterClientTaskGrid", hunterClientTaskGrid_);  
		console.log("Successfully created hunter client task grid!!!"); 
		this.prepareTaskGridPopupTemplate();
		
		//this.slideUpTaskTabs();
	},
	prepareTaskGridPopupTemplate : function(){
		var dataSource = hunterAdminClientUserVM.get("taskTypeDs");
		 $("#taskTypeInput").kendoDropDownList({
	            dataTextField: "text",
	            dataValueField: "value",
	            dataSource: dataSource.data // bind it to the brands array
	        });
		 console.log("Preparing task type input!!");
	},
	showDetails : function(id) {
		var model = this.get("hunterClientTaskGrid").dataSource.get(id);
        var detailsTemplate = kendo.template($("#hunterClientTasksPopupViewTemplate").html());
        var content = detailsTemplate(model);
        kendoKipHelperInstance.showPercentMeasuredOKCancelTitledPopup(content, "Task Details", "30", "65");
    },
	createUserGrid : function(){
		console.log("Creating user grid..");
		var userGrid = $("#hunterUserGrid").kendoGrid({
			dataSource : hunterUserDS,
			toolbar : kendo.template($("#hunterUserToolBar").html()),
			pageable: {
                refresh: true,
                pageSizes: true,
                buttonCount: 5
            },
            height: 300,
            sortable: true,
            editable: {
				mode: "popup",
				template: kendo.template($("#hunterUserEditTemplate").html())
            },
            columns: [
               { template : "#=getHunterUserCheckBox()#", width:30 }, 
               { field: "firstName",title: "First Name",width: 150 }, 
               { field: "lastName", title: "Last Name", width: 150 }, 
               { field: "email", title: "Email",width: 150 }, 
               { field: "userName", title: "User Name", width: 150 },
               { field: "phoneNumber", title: "Phone Number", width: 150 }, 
	           { field: "cretDate", title: "Created On", width: 150 }, 
	           { field: "createdBy", title: "Created By", width: 150 }, 
	           { field: "lastUpdate", title: "Last Updated On", width: 150 }, 
	           { field: "lastUpdatedBy", title: "Updated By", width: 150 },
	           { field: "client", title: "Client", width: 60, template : "#=getClientTemplate()#" },
	           { field: "editTemplate", title: "Edit", width: 60, template : "#=getEditTemplate()#" },
	           { "name": "Delete", "title": "Delete","width": 60, template : "#=getHunterUserDeleteButton()#", filterable: false,resizable: false  }
	           
            ]
		}).data("kendoGrid"); 
		this.set("userGrid", userGrid);  
		console.log("Successfully created hunter user!!!"); 
	},
	
	
	/* This is purely for region VM */
	
	isCountryDropdownEnabled : true, 
	isCountyDropdownEnabled : true,
	isStateDropdownEnabled : false,
	isConstituencyDropdownEnabled : true,
	isConstituencyWardDropdownEnabled : true,
	
	isCountryDropdownVisible : true, 
	isCountyDropdownVisible : true,
	isStateDropdownVisible : false,
	isConstituencyDropdownVisible : true,
	isConstituencyWardDropdownVisible : true,
	
	selCountry : "UNSELECTED", 
	selState : "UNSELECTED", 
	selCounty : "UNSELECTED", 
	selConstituency : "UNSELECTED", 
	selConstituencyWard : "UNSELECTED", 
	hasState : "UNSELECTED",
	
	beforeInitRegionVM : function(){
		console.log("Before initializing task region VM..");
	},
	initRegionVM : function(){
		this.beforeInitRegionVM();
		console.log("initializing task region VM..");
		this.afterInitRegionVM();
	},
	afterInitRegionVM : function(){
		console.log("Successfully finished initializing task region VM!!");
		this.get("countryDS").read();
		this.get("countyDS").read();
		this.get("constituencyDS").read();
		this.get("constituencyWardDS").read();
		//this.refreshTaskRegionDS();
	},
	countryDS : new kendo.data.DataSource({
        type: "json",
        serverFiltering: true,
        transport: {
       	 read:  {
      	      url: "http://localhost:8080/Hunter/region/action/countries/read",
      	      dataType: "json",
      	      method: "POST"
       	 }
        },
        requestStart: function(e) {
	        var type = e.type;
	        if(type === 'read')
	        	console.log("reading countries...");
	     }
	}),
	countyDS : new kendo.data.DataSource({
        type: "json",
        serverFiltering: true,
        transport: {
       	 read:  {
      	      url: function(){
      	    	  var url =  "http://localhost:8080/Hunter/region/action/counties/read/" + hunterAdminClientUserVM.get("selCountry");
      	    	  console.log("CountyDS URL >> " + url);
      	    	  return url;
      	      }, 
      	      dataType: "json",
      	      method: "POST"
       	 }
        },
        requestStart: function(e) {
	        var type = e.type;
	        if(type === 'read')
	        	console.log("reading counties...");
	     }
	}),
	constituencyDS : new kendo.data.DataSource({
        type: "json",
        serverFiltering: true,
        transport: {
       	 read:  {
      	      url: function(){
      	    	  var url = "http://localhost:8080/Hunter/region/action/constituencies/read/" + hunterAdminClientUserVM.get("selCounty");
      	    	  console.log("ConstituencyDS URL >> " + url);
      	    	  return url;
      	      },
      	      dataType: "json",
      	      method: "POST"
       	 }
        },
        requestStart: function(e) {
	        var type = e.type;
	        if(type === 'read')
	        	console.log("reading constituencies...");
	     }
	}),
	constituencyWardDS : new kendo.data.DataSource({
        type: "json",
        serverFiltering: true,
        transport: {
       	 read:  {
      	      url: function(){
      	    	  var url = "http://localhost:8080/Hunter/region/action/constituencyWards/read/" + hunterAdminClientUserVM.get("selConstituency");
      	    	  console.log("ConstituencyWardDS URL >> " + url);
    	    	  return url;
      	      },
      	      dataType: "json",
      	      method: "POST"
       	 }
        },
        requestStart: function(e) {
	        var type = e.type;
	        if(type === 'read')
	        	console.log("reading constituencywards...");
	     }
	}),
	onChangeCountry : function(e){
		
		hunterAdminClientUserVM.resetValues(hunterAdminClientUserVM.get("selCountry"),"UNSELECTED", "UNSELECTED", "UNSELECTED"); 
		hunterAdminClientUserVM.set("isCountyDropdownEnabled", true);
		
		hunterAdminClientUserVM.get("countyDS").read();
		hunterAdminClientUserVM.get("constituencyDS").read();
		hunterAdminClientUserVM.get("constituencyWardDS").read();
		
	},
	onChangeCounty : function(){
		hunterAdminClientUserVM.resetValues(hunterAdminClientUserVM.get("selCountry"),hunterAdminClientUserVM.get("selCounty"), "UNSELECTED", "UNSELECTED");
		hunterAdminClientUserVM.set("isConstituencyDropdownEnabled", true);
		console.log("Selected county >> " + hunterAdminClientUserVM.get("selCounty"));		
		
		hunterAdminClientUserVM.get("constituencyDS").read();
		hunterAdminClientUserVM.get("constituencyWardDS").read();
	},
	onChangeConstituency : function(){
		hunterAdminClientUserVM.resetValues(hunterAdminClientUserVM.get("selCountry"),hunterAdminClientUserVM.get("selCounty"), hunterAdminClientUserVM.get("selConstituency"), "UNSELECTED"); 
		hunterAdminClientUserVM.set("isConstituencyWardDropdownEnabled", true);
		console.log("Selected constituency >> " + this.get("selConstituency")); 
		
		hunterAdminClientUserVM.get("constituencyWardDS").read();
	},
	onChangeConsituencyWard : function(e){
		hunterAdminClientUserVM.resetValues(hunterAdminClientUserVM.get("selCountry"),hunterAdminClientUserVM.get("selCounty"), hunterAdminClientUserVM.get("selConstituency"), hunterAdminClientUserVM.get("selConstituencyWard"));
		console.log("selConstituencyWard >> " + hunterAdminClientUserVM.get("selConstituencyWard")); 
	},
	resetValues : function(selCountry, selCounty, selConstituency, selConstituencyWard){
		console.log("Resetting values ... ("+ selCountry + "," + selCounty  + "," + selConstituency + "," + selConstituencyWard + ")");
		hunterAdminClientUserVM.set("selCountry",selCountry);
		hunterAdminClientUserVM.set("selCounty",selCounty);
		hunterAdminClientUserVM.set("selConstituency",selConstituency);
		hunterAdminClientUserVM.set("selConstituencyWard",selConstituencyWard);
	},
	onDataConsWardBound : function(e){
		
	},
	
	
	
	/*::::::::::::::::::::    This is purely for selected task regions grid       :::::::::::::::::::::::::::::*/
	
	selTaskId : null,
	taskUniqueReceivers : 0,
	isTaskRegionOpen : false,
	
	getSelectedTaskId : function(){
		var taskId = hunterAdminClientUserVM.get("selTaskId");
		if(taskId == null)
			taskId = "null";
		return taskId;
	},
	selTaskRegionsDS : new kendo.data.DataSource({
        type: "json",
        serverFiltering: true,
        refreshable:true,
        height:200,
        transport: {
	       	read:  {
	      	      url: function(){
	      	    	  var url = "http://localhost:8080/Hunter/region/action/task/regions/read/" + hunterAdminClientUserVM.getSelectedTaskId();
	      	    	  console.log("Selected task region URL >> " + url);
	    	    	  return url;
	      	      },
	      	      dataType: "json",
	      	      method: "POST"
	       	 },
	       	 destroy : {
	       		url : function(){
	       			var taskId = hunterAdminClientUserVM.getSelectedTaskId();
	       			if(taskId == null) tskId = "0";
	       			var url = "http://localhost:8080/Hunter/region/action/task/regions/delete/" + taskId;
	       			return url;
	       		},
	       		dataType: "json", 
		        contentType:"application/json",
		        method:"POST"
	       	 },
	       	 parameterMap: function(options, operation) {
		          if (operation == "read") {
		        	  console.log("Reading data...");
		          }else if(operation == "destroy"){ 
		        	  var json = kendo.stringify(options);
		        	  return json;
		          }else{
		        	  var json = kendo.stringify(options); 
		        	  return json;
		          }
		    }
        },
        requestEnd: function(e) {
	        var type = e.type;
	        if(type === 'destroy'){
	        	hunterAdminClientUserVM.getTotalReceivers();
	        }
	  },
  	  	schema: {
  	    	model:taskRegionsModel
  	  	},
  	  	aggregate: [
  	  	  { field: "receiverCount", aggregate: "sum" }
        ],
        requestStart: function(e) {
	        var type = e.type;
	        if(type === 'read')
	        	console.log("reading constituencywards...");
	     }
	}),
	onSaveSelTaskRegion : function(){
		alert("Saved task region!!"); 
	},
	deleteSelectedTaskRegionGroup : function(regionId){
		var ds = this.get("selTaskRegionsDS");
		var model = ds.get(regionId);
		ds.remove(model);
		ds.sync();
		kendoKipHelperInstance.closeHelperKendoWindow();
		kendoKipHelperInstance.popupWarning("Successfully removed the region!", "Success");
		this.getTotalReceivers();
	},
	getTotalReceivers : function(){
		var taskId = this.get("selTaskId");
		if(taskId == null) taskId = 0;
		console.log("Loading unique count for task Id : " + taskId);
		$.ajax({
			url: "http://localhost:8080/Hunter/region/action/task/regions/receivers/uniqueCount/" + taskId,
			dataType : "json",
			method: "POST"
		}).done(function(data) {
			hunterAdminClientUserVM.set("taskUniqueReceivers", data);
			$("#taskUniqueReceiversSpan").text(data);
			console.log("Successfully loaded unique count for task Id : " + taskId + ". Count ( " + data + " )");
		 }).fail(function(data) {
			 var json = jQuery.parseJSON(data);
			 kendoKipHelperInstance.popupWarning("Failed to load task receiver count ! " + data.statusText + " (" + json.status + ")", "Error");
		 });
	
	}
	
	
});







