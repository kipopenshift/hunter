
var kendoKipHelperInstance;
var hunterWindow;

var ReceiverGroupModel = kendo.data.Model.define({
	id:"groupId",
	fields : {
		"groupId" : {
			type : "number", validation : {required : true},editable:false, defaultValue:"0"
		},
		"receiverCount" : {
			type : "number", validation : {required : true},editable:false, defaultValue:"0"
		},
		"ownerUserName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"firstName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"lastName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"groupName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"receiverType" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"groupDesc" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"cretDate" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"createdBy" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"lastUpdate" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"lastUpdatedBy" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		}
	},
	getTaskGroupDeleteTemplate : function(){
		var groupName = this.get("groupName");
		var id = this.get("groupId");
		var idString = '"'+ id +'",';
		var message = '"<u><b>'+ groupName +'</u></b> will be deleted. <br/> Are you sure?"';
		var html = kendoKipHelperInstance.createDeleteButton(false, 'hunterAdminClientUserVM.showPopupToDeleteTaskGroup('+  idString + message+ ')');
		return html;
	}
});

var ReceiverGroupDS = new kendo.data.DataSource({
	  transport: {
	    read:  {
	      url: "http://localhost:8080/Hunter/messageReceiver/action/group/read",
	      dataType: "json",
	      contentType:"application/json",
	      method: "POST"
	    },
	    create: {
	        url: "http://localhost:8080/Hunter/messageReceiver/action/group/create",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST",
	        success: function(result) {
	            kendoKipHelperInstance.popupWarning('', JSON.stringify(result), "Success");
	         }
	    },
	    destroy: {
	        url: "http://localhost:8080/Hunter/messageReceiver/action/group/destroy",
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
	    	model:ReceiverGroupModel
	  },
	  error: function (e) {
	        kendoKipHelperInstance.popupWarning(e.status, e.errorThrown, "Error");
	  },
	  requestStart: function(e) {
	        var type = e.type;
	        var message = null;
	        if(type === 'read')
	        	return;
	        if(type === 'update')
	        	message = "Updating record...";
	        if(type === 'destroy')
	        	message = "Deleting record...";
	        if(type === 'create')
	        	message = "Creating record...";
	        
	        if(message != null){
	        	kendoKipHelperInstance.popupWarning(message, "Success");
	        }
	        
	  },
	  requestEnd: function(e) {
	        var response = e.response;
	        var type = e.type;
	        var message = null;
	        if(type === 'update')
	        	message = "Successfully updated record!";
	        if(type === 'destroy')
	        	message = "Successfully deleted record!";
	       /* if(type === 'create')
	        	message = "Successfully created record!";*/
	        if(message != null){
	        	kendoKipHelperInstance.popupWarning(message, "Success");
	        }
	  },
	  pageSize:1000
});

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
	        var message = null;
	        if(type === 'read')
	        	return;
	        if(type === 'update')
	        	message = "Updating record...";
	        if(type === 'destroy')
	        	message = "Deleting record...";
	        if(type === 'create')
	        	message = "Creating record...";
	        
	        if(message != null){
	        	kendoKipHelperInstance.popupWarning(message, "Success");
	        }
	        
	  },
	  requestEnd: function(e) {
	        var response = e.response;
	        var type = e.type;
	        var message = "";
	        if(type === 'read')
	        	/*if(response !== 'undefined' && response != null){
	        		message = "Successfully obtained ( "+ response.length +" ) records!";
	        	}else{
	        		return;
	        	}*/
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
	        var message = null;
	        if(type === 'read')
	        	return;
	        if(type === 'update')
	        	message = "Updating record...";
	        if(type === 'destroy')
	        	message = "Deleting record...";
	        if(type === 'create')
	        	message = "Creating record...";
	        
	        if(message != null){
	        	kendoKipHelperInstance.popupWarning(message, "Success");
	        }
	  },
	  requestEnd: function(e) {
	        var response = e.response;
	        var type = e.type;
	        var message = null ;
	        if(type === 'read'){
	        	/*if(response !== 'undefined' && response != null){
	        		message = "Successfully obtained ( "+ response.length +" ) records!";
	        	}else{
	        		return;
	        	}*/
	        	hunterAdminClientUserVM.set("selUserId", null);
	        }
	        
	        if(type === 'update')
	        	message = "Successfully updated record!";
	        if(type === 'destroy')
	        	message = "Successfully deleted record!";
	        if(type === 'create')
	        	message = "Successfully created record!";
	        
	        if(message != null){
	        	kendoKipHelperInstance.popupWarning(message, "Success");
	        }
	        
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
	        	  //kendoKipHelperInstance.popupWarning("Reading data!");
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
	        var message = null;
	        if(type === 'read')
	        	return;
	        if(type === 'update')
	        	message = "Updating record...";
	        if(type === 'destroy')
	        	message = "Deleting record...";
	        if(type === 'create')
	        	message = "Creating record...";
	        
	        if(message != null){
	        	kendoKipHelperInstance.popupWarning(message, "Success");
	        }
	  },
	  requestEnd: function(e) {
	        var response = e.response;
	        var type = e.type;
	        var message = null;
	        if(type === 'read'){
	        	/*if(response !== 'undefined' && response != null){
	        		message = "Successfully obtained ( "+ response.length +" ) records!";
	        	}else{
	        		return;
	        	}*/
	        	hunterAdminClientUserVM.set("selUserId", null);
	        }
	        
	        if(type === 'update')
	        	message = "Successfully updated record!";
	        if(type === 'destroy')
	        	message = "Successfully deleted record!";
	        if(type === 'create')
	        	message = "Successfully created record!";
	        
	        if(message != null){
	        	kendoKipHelperInstance.popupWarning(message, "Success");
	        }
	        
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
		"tskMsgType" : {
			type : "object", validation : {required : true},editable:true, defaultValue:null
		},
		"taskName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"taskObjective" : {
			type : "string", editable:true, defaultValue:null,
				validation: {
                required: { message: "Task objective is required!" },
                validateTaskObjective: function (input) {
                    if (input.attr("data-bind") == "value:taskObjective") { 
                        if (input.val().length > 100) {
                            input.attr("data-validateTaskObjective-msg", "Task objective cannot be more than 100 characters");
                            return false;
                        }
                        else
                            return true;
                    }
                    return true;
                }
            }
		},
		"description" : {
			type : "string" ,editable:true, defaultValue:null,
			validation: {
                required: { message: "Task description is required!" },
                validateTaskDescription: function (input) {
                    if (input.attr("data-bind") == "value:description") { 
                        if (input.val().length > 50) {
                            input.attr("data-validateTaskDescription-msg", "Task description cannot be more than 50 characters");
                            return false;
                        }
                        else
                            return true;
                    }
                    return true;
                }
            }
		},
		"taskName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"processedBy" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"processedOn" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
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
		"taskRegions" : {
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
		var delStatus = this.get("taskLifeStatus");
		if(delStatus === "Processed"){
			return "";
			//return kendoKipHelperInstance.createDisabledContextEditButton(false);
		}else{
			return kendoKipHelperInstance.createContextEditButton(false);
		}
		
	},
	getTaskDeleteTemplate : function(){
		var delStatus = this.get("taskLifeStatus");
		if(delStatus === "Processed"){
			return "";
			//return kendoKipHelperInstance.createDisabledContextEditButton(false);
		}
		var id = this.get("taskId");
		var taskName = this.get("taskName");
		var idString = '"'+ id +'",';
		var message = '"<b>' + taskName  + '</b> will be deleted.<br/> Are you sure?"';
		var html = kendoKipHelperInstance.createDeleteButton(false, 'kendoKipHelperInstance.showOKCancelToDeleteHunterTask('+  idString + message+ ')');
		return html;
	},
	getTaskHistoryTemplate : function(){
		var taskId = this.get("taskId");
		var html = kendoKipHelperInstance.createSimpleHunterButton("seek-s",null,'hunterAdminClientUserVM.downloadTaskHistory("'+ taskId +'")');
		return html;
	},
	getTaskProcessTemplate : function(){
		var delStatus = this.get("taskLifeStatus");
		if(delStatus === "Processed"){
			return "";
			//return kendoKipHelperInstance.createDisabledContextEditButton(false);
		}
		var id = this.get("taskId");
		var idString = "\""+ id +"\"";
		var html = kendoKipHelperInstance.createSimpleHunterButton("arrow-e",null, "hunterAdminClientUserVM.showPopupForProcessTask("+ idString +")" );
		return html;
	},
	getTaskEditStatusTemplate : function(){
		var currentStatus = this.get("taskLifeStatus");
		if(currentStatus === 'Processed'){
			return "<center><span style='color:#00B655;' >Processed</center></span>";
		}
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
		var color = '';
		if(dStatus === 'Processed'){
			color = '00B655';
		}else if(dStatus === 'Conceptual'){
			color = 'A200FF';
		}else{
			color = 'FF0000';
		}
		return "<center><a style='color:#"+ color +"; cursor:pointer'>"+ dStatus +"</a></center>";
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
	taskProcessManager : null,
	
	taskRegionsOpen : true,
	taskGroupsOpen : false,
	taskGroupDropdownData : [],
	taskGroupDropDownSelVal : null,
	
	taskGroupsTotalReceivers : 0,
	taskAllCombinedReceivers : null,
	
	
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
	tskMsgCretBy : null,
	tskMsgLstUpdate : "05/12/2045",
	tskMsgLstUpdatedBy : null,
	tskMsgProvider : null,
	tskMsgOwner : null,
	deleteCurMsgFlag : false,
	hunterTaskHistoryGrid : null,
	
	
	
	onChangeTaskMsgSendDate : function(e){
		console.log(this.get("taskMsgSendDate")); 
	},
	onChangeTaskMsgStatus : function(e){
		console.log("Changed message status too : " + this.get("tskMsgLifeStatus"));
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
		this.loadExistentEmailTemplates();
		console.log("hunterAdminClientUserVM successfully initialized!!");
	},
	taskHistoryDS : new kendo.data.DataSource({
	  batch: true,
	  height:400,
	  transport: {
	    read:  {
	      url: function(){
	    	  var taskId = hunterAdminClientUserVM.get("selTaskId");
	    	  var url = HunterConstants.HUNTER_BASE_URL + "/task/action/task/history/getForTask/" + taskId;
	    	  return url;
	      },
	      dataType: "json",
	      method: "POST"
	    }
	  },
	  schema: {
	    model: { 
			id: "historyId",
	    		fields : {
	    			"historyId" : {type : "number", defaultValue:0, editable : false},
	    			"taskId" : {type : "number", defaultValue:0, editable : false},
	    			"evenName" : {type : "string", defaultValue:null, editable : false},
	    			"eventStatus" : {type : "string", defaultValue:null, editable : false},
	    			"eventMessage" : {type : "string", defaultValue:null, editable : false},
	    			"eventUser" : {type : "string", defaultValue:null, editable : false},
	    			"eventDate" : {type : "string", defaultValue:null, format:"yyyy-MM-dd HH:mm:ss", editable : false},
	    		}, 
	    		getEventStatusTemplate : function(){
	    			var status = this.get("eventStatus");
	    			var color = status == 'Failed' ? 'red' : 'green';
	    			var template = "<span style='color:"+ color +"' >"+ status +"</span>";
	    			return template;
	    		}
			}
	  }
	}),
	destroyTaskHistroyGrid : function(){
		var exstGrid = this.get("hunterTaskHistoryGrid");
		if(exstGrid != null){
			exstGrid.destroy();
			$("#taskHistoryPopupGrid").empty();
			$("#taskHistoryPopupGrid").html("");
		}
	},
	displayPopupForTaskHistoryGrid : function(taskId){
		var r = $.Deferred();
		this.set("selTaskId",taskId);
		var content = "<div id='taskHistoryPopupGrid'  style='width:1200px;height:700px;background-color:#EBF7FF;border:1px solid #B6D2E2;border-radius:4px;' ></div>";
		kendoKipHelperInstance.showWindowWithOnClose(content, "Hunter Task History");
		console.log("Finished displaying kendo window!!");
		return r;
	},
	createTaskHistoryGrid : function(){
		var ds = hunterAdminClientUserVM.get("taskHistoryDS");
		var hunterTaskHistoryGrid = $("#taskHistoryPopupGrid").kendoGrid({
			dataSource : ds,
			toolbar : kendo.template($("#taskHistoryToolBarTemplate").html()),
			height : 350,
			pageable: {
                refresh: true,
                pageSizes: true,
                buttonCount: 5
            },
            sortable: true,
            columns: [
               { field: "historyId", title: "ID", width: 30 },
               { field: "taskId", title: "Task Id", width: 60 },
               { field: "evenName", title: "Event", width: 80},
               { field: "eventStatus", title: "Event Status", width: 60, template : "#=getEventStatusTemplate()#" },
               { field: "eventMessage", title: "Event Message", width: 250 },
               { field: "eventUser", title: "User", width: 60 },
               { field: "eventDate", title: "Event Date", width: 85 },
            ]
		}).data("kendoGrid"); 
		this.set("hunterTaskHistoryGrid", hunterTaskHistoryGrid);
		ds.read();
		console.log("Done creating task grid!!");
	},
	downloadTaskHistory : function(taskId){
		this.displayPopupForTaskHistoryGrid(taskId).done(hunterAdminClientUserVM.createTaskHistoryGrid());
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
	deleteCurrentTaskMsg : function(){
		kendoKipHelperInstance.closeHelperKendoWindow();
		this.clearCurrentTskMsg();
		this.set("deleteCurMsgFlag", true);
		var taskId = this.get("selTaskId"); 
		console.log("Deleting task for task Id : " + taskId); 
		this.loadNewTaskMessageView(taskId); 
		this.set("deleteCurMsgFlag", false);
		kendoKipHelperInstance.popupWarning("Deleted Message Successfully!!", "Success");
	},
	showPopupForDeleteCurrentTskMsg : function(){
		var iconName = "close" ,text = "Delete", onClick = "hunterAdminClientUserVM.deleteCurrentTaskMsg()";
		var deleteButton = kendoKipHelperInstance.createSimpleHunterButton(iconName,text,onClick);
		onClick = "kendoKipHelperInstance.closeHelperKendoWindow()";
		text = "Cancel";
		iconName = "cancel";
		var cancelButton = kendoKipHelperInstance.createSimpleHunterButton(iconName,text,onClick);
		var warningTxt = "This message will be deleteda and replaced with default message. Are you sure?";
		var warnButtons = "<table style='width:60%;margin-left:20%;' ><tr><td>" + deleteButton + "</td><td>" + cancelButton + "</td></table></tr>";
		var content = warningTxt + "</br>" + warnButtons;
		kendoKipHelperInstance.showPopupWithNoButtons("Delete Message", content);
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
		var isMsgViewOpen = this.get("isMsgViewOpen");
		var isEmailSectionOpen = this.get("isEmailSectionOpen");
		
		if(isRegionViewOpen){
			this.canceltEditRegionAndClose();
			this.set("isTaskRegionOpen",false);
			console.log("Successfull closed task region view !!!"); 
		}else if(isMsgViewOpen){
			this.canceltEditMessageAndClose();
			this.set("isMsgViewOpen",false);
			console.log("Successfull closed message region view !!!");
		}else if(isEmailSectionOpen){
			this.closeEmailTemplateSection();
			this.set("isEmailSectionOpen", false);
			console.log("Successfull closed email message view !!!");
		}
		
		var userId = this.get("selUserId"); 
		if(userId == null){
			kendoKipHelperInstance.showErrorNotification("Please select user first!!");
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
		$("#hunterUserGrid").slideDown(500, function(){
		});
	},
	loadSelUserTasksDetails : function(id){
		var clientBean = this.get("clientBean"); 
		var clientId = clientBean["clientId"];
		if(clientBean == null || clientId != id ){
			kendoKipHelperInstance.popupWarning("Please load this user first!","Error"); 
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
		this.set("tskMsgOwner", json.user.userName);
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
				        		  var tskMsgType = options.tskMsgType["msgTypVal"];
				        		  console.log("Task Message Type value !! >> " + tskMsgType);
				        		  json["tskMsgType"] = tskMsgType;
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
			        var message = null;
			        if(type === 'read')
			        	return;
			        if(type === 'update')
			        	message = "Updating record...";
			        if(type === 'destroy')
			        	message = "Deleting record...";
			        if(type === 'create')
			        	message = "Creating record...";
			        
			        if(message != null){
			        	kendoKipHelperInstance.popupWarning(message, "Success");
			        }
			        
			  },
			  requestEnd: function(e) {
			        var response = e.response;
			        var type = e.type;
			        var message = null;
			        if(type === 'read')
			        	/*if(response !== 'undefined' && response != null){
			        		message = "Successfully obtained ( "+ response.length +" ) records!";
			        	}else{
			        		return;
			        	}*/
			        if(type === 'update')
			        	message = "Successfully updated record!";
			        if(type === 'destroy')
			        	message = "Successfully deleted record!";
			        if(type === 'create')
			        	message = "Successfully created record!";
			      
			        if(message != null){
			        	kendoKipHelperInstance.popupWarning(message, "Success");
			        }
			        
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
		var url = HunterConstants.HUNTER_BASE_URL + "/task/action/task/changeStatus";
		var components = keyStr.split(":::");
		var toStatus = components[0];
		var id = components[1];
		var data = {"taskId" : id, "toStatus" : toStatus}; 
		data = JSON.stringify(data);
		console.log("Current status = " + toStatus);
		console.log("Task Id = " + id);
		kendoKipHelperInstance.ajaxPostData(data, "application/json", "json", "POST", url, "hunterAdminClientUserVM.afterChangeTaskStatus");
		kendoKipHelperInstance.closeHelperKendoWindow();
	},
	afterChangeTaskStatus : function(data){
		data = jQuery.parseJSON(data);
		var status = data["status"];
		var message = data["message"];
		if(status == "Failed"){
			message = replaceAll(message, ",", "</br>");  
			kendoKipHelperInstance.showSimplePopup("Error updating Task!","<span style='color:red;' >" + message + "</span>");
		}else if(status === "Success"){ 
			kendoKipHelperInstance.popupWarning("Successfully updated task!", "Success");
			var tskGridDs = this.get("hunterClientTaskGrid").dataSource;
			tskGridDs.read();
		}
	},
	fetchUniqueCountForTaskId : function(taskId){
		var url = HunterConstants.HUNTER_BASE_URL + "/messageReceiver/action/taskReceivers/getAllCounts/" + taskId;
		kendoKipHelperInstance.ajaxPostData(null, "application/json", "json", "POST", url, "hunterAdminClientUserVM.afterFetchUniqueCountForTaskId");
	},
	afterFetchUniqueCountForTaskId : function(data){
		var json = $.parseJSON(data);
		console.log(data);
		var headers = "<tr><td class='tskDtlHdr' >Label</td><td class='tskDtlHdr'  >Number </td><td class='tskDtlHdr'  >Total Receivers </td></tr>";
		var groupsLabel = "<td>Task Groups</td>";
		var groupTotalNumber = "<td>"+ (json == null ? "" : json.groupNumber) +"</td>";
		var groupReceiverCount = "<td>"+ (json == null ? "" : json.groupCount) +"</td>";
		var string1 = "<tr>" + (groupsLabel + groupTotalNumber + groupReceiverCount) + "</tr>";
		var regionsLabel = "<td>Task Regions</td>";
		var regionsNumber = "<td>"+ (json == null ? "" : json.regionsNumber) +"</td>";
		var regionReceiverCount = "<td>"+ (json == null ? "" : json.regionCount) +"</td>";
		var string2 = "<tr>" + (regionsLabel + regionsNumber + regionReceiverCount) + "</tr>";
		var table = "<table id='taskRegionsDetailsTable'  style='border:1px solid #A9C8D5;border-radius:4px;width:100%;' >"+ headers + string1 + string2 +"</table>";
		this.showPopupForTaskRegionsDetails(table);
	},
	showPopupForTaskRegionsDetails : function(table){
		var id = this.get("selTaskId"); 
		var editButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("pencil","Edit", 'hunterAdminClientUserVM.loadEdiTaskRegionView("'+ id +'")') + "</td>";
		var okButton = "<td>" + kendoKipHelperInstance.createSimpleHunterButton("tick","Close", 'kendoKipHelperInstance.closeHelperKendoWindow()');
		var buttTable = "<br/><table style='width:60%;margin-left:20%;' ></tr>" + editButton + okButton + "</tr></table>";
		var totalContent = table + buttTable;
		kendoKipHelperInstance.showPopupWithNoButtons("Task Regions Details",totalContent);
		$("#taskRegionsDetailsTable").css({"border-collapse":"collapse","padding" : "5px","webkit-box-shadow": "0 0 5px 2px #E2F3FF","-moz-box-shadow": "0 0 5px 2px #E2F3FF","box-shadow": "0 0 5px 2px #E2F3FF"}); 
		$("#taskRegionsDetailsTable tr td").css({"text-align" : "center"});
		$(".tskDtlHdr").css({"background-color" : "#E0F1F9","color" : "black","font-weight" : "bolder","padding" : "5px","height" : "15px"}); 
	},
	loadTaskRegionView: function(id){
		this.set("selTaskId",id);
		this.loadGroupDropdown(id);
		this.fetchUniqueCountForTaskId(id);
	},
	loadNewTaskRegionView : function(taskId){
		hunterAdminClientUserVM.set("selTaskId", taskId);
		hunterAdminClientUserVM.getTotalReceivers();
		var taskRegionsDs = hunterAdminClientUserVM.get("selTaskRegionsDS");
		taskRegionsDs.read();
		hunterAdminClientUserVM.opendTaskRegionView();
		kendoKipHelperInstance.closeHelperKendoWindow();
		this.set("isTaskRegionOpen", true);
		$("#taskRegionsButton").css("background-color","rgb(155,255,229)");
		
		// update counts of receivers.
		 hunterAdminClientUserVM.getTaskGroupsTotalReceivers();
		hunterAdminClientUserVM.getAllReceiversForTaskReceivers();
		
	},
	loadEdiTaskRegionView : function(taskId){
		kendoKipHelperInstance.closeHelperKendoWindow();
		this.loadNewTaskRegionView(taskId);
	},
	loadDetailsTaskRegionView : function(taskId){
		kendoKipHelperInstance.closeMeasuredWindow();
		//this.showTabStripNumber(2);
		$("#taskGridHolder").hide(1000, function(){
			$("#taskRegionStrip").slideDown(500);
		});
	},
	saveCurrentEditRegion : function(){
		var selTaskId = this.get("selTaskId"); 
		console.log("Adding task regions. Task Id = " + selTaskId);
		var selCountry = this.get("selCountry");
		var selState = this.get("selState");
		var selCounty = this.get("selCounty");
		var selConstituency = this.get("selConstituency");
		var selConstituencyWard = this.get("selConstituencyWard");
		var model = {"selCountry":selCountry, "selState":selState, "selCounty":selCounty, "selConstituency" : selConstituency, "selConstituencyWard" : selConstituencyWard, "selTaskId" : selTaskId };
		var json =  JSON.stringify(model);
		kendoKipHelperInstance.ajaxPostData(json, "application/json", "json", "POST", HunterConstants.HUNTER_BASE_URL+"/region/action/task/regions/addTotask", "hunterAdminClientUserVM.afterSaveCurrentEditRegion"); 
		console.log("Successfully added regions to task!!!");
	},
	afterSaveCurrentEditRegion : function(data){
		var ds = hunterAdminClientUserVM.get("selTaskRegionsDS");
		if(ds != null){
			ds.read();
		}
		console.log(data);
		data = jQuery.parseJSON(data);
		var status = data[HunterConstants.STATUS_STRING];
		var message = data[HunterConstants.MESSAGE_STRING];
		var count = data["count"];
		var groupCount = data["groupCount"]; 

		if(count != null && groupCount != null){
			hunterAdminClientUserVM.set("taskUniqueReceivers",count);
			hunterAdminClientUserVM.set("taskGroupsTotalReceivers", groupCount);
			hunterAdminClientUserVM.set("taskAllCombinedReceivers",(count + groupCount));
			hunterAdminClientUserVM.getTaskGroupsTotalReceivers();
		}
		kendoKipHelperInstance.showErrorOrSuccessMsg(status, message);
	},
	canceltEditRegionAndClose : function(){
		/* If the groups section is open, it should close. When user comes back, region should be default. */
		this.showTaskRegionsPart(); 
		this.closeTaskRegionView();
	},
	showTaskRegionsPart : function(){
		var isAlreadyOpen = this.get("taskRegionsOpen");
		if(isAlreadyOpen){
			console.log("Regions part is already open. Returning..."); 
			return;
		}else{
			$("#taskGroupsCoverDiv" ).slideUp(300,function(){
				$("#taskRegionsCoverDiv").slideDown(300,function(){
				});
			});
		}
		$("#taskRegionsButton").css("background-color","rgb(155,255,229)");
		$("#taskGroupsButton").css("background-color","rgb(212,239,249)");
		this.set("taskRegionsOpen", true);
		this.set("taskGroupsOpen",false);
		var grid = $("#selTaskRegionsGrid").data("kendoGrid");
		if(grid != null){
			console.log("Found the grid. Refreshing...");
			grid.refresh();
		}
		//this.getAllReceiversForTaskReceivers();
		this.getTaskGroupsTotalReceivers(); // read the total receivers.
	},
	showTaskGroupsPart : function(){
		var selTaskId = this.get("selTaskId");
		var isAlreadyOpen = this.get("taskGroupsOpen");
		if(isAlreadyOpen){
			console.log("Groups part is already open. Returning..."); 
			return;
		}else{
			$("#taskRegionsCoverDiv").slideUp(300,function(){
				$("#taskGroupsCoverDiv").slideDown(300,function(){
				});
			});
		}
		$("#taskGroupsButton").css("background-color","rgb(155,255,229)");
		$("#taskRegionsButton").css("background-color","rgb(212,239,249)");
		this.get("receiverGroupDS").read(); // refresh to load groups for selected task.
		this.getTaskGroupsTotalReceivers(); // read the total receivers.
		this.set("taskGroupsOpen",true);
		this.set("taskRegionsOpen", false);
		this.getAllReceiversForTaskReceivers();
	},
	closeTaskRegionView : function(){
		this.set("isTaskRegionOpen", false); // this is important!!
		$("#taskRegionStrip").slideUp(500, function(){
			$("#hunterUserDetailsStrip").slideDown(500);
		});
	},
	opendTaskRegionView : function(taskId){
		$("#hunterUserDetailsStrip").slideUp(500, function(){
			hunterAdminClientUserVM.showTabStripNumber(2);
			$("#taskRegionStrip").slideDown(500);
		});
		hunterAdminClientUserVM.getAllReceiversForTaskReceivers();
	},
	loadTaskMessageView: function(id){
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
		kendoKipHelperInstance.initKendoWindow();
		this.loadEdiTaskMessageView(id);
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
		this.getMessageDetails();
		this.closeEditMessageView();
		// refresh the grid when you close taskMsgDetails.
		this.loadSelUserDetails();
	},
	saveCurrentEditMessage : function(){
		this.getMessageDetails();
		kendoKipHelperInstance.popupWarning("Successfully saved the changes.", "Success");
	},
	closeEditMessageView : function(){
		$("#taskMessageStrip").slideUp(500, function(){
			$("#hunterUserDetailsStrip").slideDown(500);
		});
		this.set("isMsgViewOpen", false);
	},
	opendEditMessageView : function(){
		$("#hunterUserDetailsStrip").slideUp(500,function(){
			$("#taskMessageStrip").slideDown(500);
		});
		this.set("isMsgViewOpen", true);
	},
	canceltEditMessageAndClose : function(){
		this.closeEditMessageView();
		this.loadSelUserDetails();
	},
	loadNewTaskEmailMessageView : function(task){
		$("#hunterUserDetailsStrip").slideUp(500,function(){
			$("#emailEditTemplateCover").slideDown(500,function(){
				hunterAdminClientUserVM.set("isEmailSectionOpen", true);
			});
		});
		var message = task.get("taskMessage");
		hunterAdminClientUserVM.setEmailMsgvalues(message);
	},
	loadNewTaskMessageView : function(taskId){
		
		console.log("loading create New Task Message view for task id >> " + taskId);
		hunterAdminClientUserVM.set("selTaskId", taskId);
		kendoKipHelperInstance.closeHelperKendoWindow();
		var task = this.get("hunterClientTaskGrid").dataSource.get(taskId);
		
		if(task.get("tskMsgType") === 'EMAIL' ){
			console.log("Loading email section...");
			this.loadNewTaskEmailMessageView(task);
			return;
		}else{
			console.log("Message is not email. Loading ordinary message section...");
		}
		
		var msg = task.get("taskMessage");
		var delTskMsg = this.get("deleteCurMsgFlag");
		console.log("Deleted message ?  " + delTskMsg );
		// set it to null to it can load default message.
		msg = delTskMsg ? msg = null : msg; 
		console.log("Task : \n" + JSON.stringify(task));
		if(msg == null){
			console.log("Task has message null, getting default message..."); 
			$.ajax({
				url: "http://localhost:8080/Hunter/task/action/tskMsg/getDefault/" + taskId,
			    method: "POST",
			    dataType: "json",
			    contentType : "application/json"
			}).done(function(data) {
				var json = jQuery.parseJSON(data);
				console.log("Message obtained for task : " + JSON.stringify(json));
				hunterAdminClientUserVM.updateTaskMsgFields(json);
			 }).fail(function(data) {
				 var json = jQuery.parseJSON(data);
				 kendoKipHelperInstance.popupWarning(data.statusText + " (" + json.status + ")", "Error");
			 });
		}else{
			console.log("Task has message already, updating VM... : " + JSON.stringify(msg) ); 
			hunterAdminClientUserVM.updateTaskMsgFields(msg);
		}
		this.opendEditMessageView();
	},
	clearCurrentTskMsg : function(){
		hunterAdminClientUserVM.set("taskMsgSendDate", null); 
		hunterAdminClientUserVM.set("taskMsgTypeVal",  null);
		hunterAdminClientUserVM.set("desiredReceivers",  null);
		hunterAdminClientUserVM.set("actualReceivers",  null);
		hunterAdminClientUserVM.set("confirmedReceivers",  null);
		hunterAdminClientUserVM.set("tskMsgId",  null);
		hunterAdminClientUserVM.set("tskMsgDelStatus",  null);
		hunterAdminClientUserVM.set("tskMsgLifeStatus",  null);
		hunterAdminClientUserVM.set("tskMstTxt",  null);
		hunterAdminClientUserVM.set("tskMsgCretDate",  null);
		hunterAdminClientUserVM.set("tskMsgCretBy",  null);
		hunterAdminClientUserVM.set("tskMsgLstUpdate",  null);
		hunterAdminClientUserVM.set("tskMsgLstUpdatedBy",  null);
		hunterAdminClientUserVM.set("tskMsgProvider",  null);
		hunterAdminClientUserVM.set("tskMsgOwner",  null);
	},
	updateTaskMsgFields : function(data){
		console.log("Date before : " + data["msgSendDate"]); 
		var date = kendo.parseDate(data["msgSendDate"]);
		console.log("Converted date " + date);
		hunterAdminClientUserVM.set("taskMsgSendDate", new Date(data["msgSendDate"])); 
		hunterAdminClientUserVM.set("taskMsgTypeVal", data["msgTaskType"]);
		hunterAdminClientUserVM.set("desiredReceivers", data["desiredReceivers"]);
		hunterAdminClientUserVM.set("actualReceivers", data["actualReceivers"]);
		hunterAdminClientUserVM.set("confirmedReceivers", data["confirmedReceivers"]);
		hunterAdminClientUserVM.set("tskMsgId", data["msgId"]);
		hunterAdminClientUserVM.set("tskMsgDelStatus", data["msgDeliveryStatus"]);
		hunterAdminClientUserVM.set("tskMsgLifeStatus", data["msgLifeStatus"]);
		hunterAdminClientUserVM.set("tskMstTxt", data["msgText"]);
		hunterAdminClientUserVM.set("tskMsgCretDate", data["cretDate"]);
		hunterAdminClientUserVM.set("tskMsgCretBy", data["createdBy"]);
		hunterAdminClientUserVM.set("tskMsgLstUpdate", data["lastUpdate"]);
		hunterAdminClientUserVM.set("tskMsgLstUpdatedBy", data["lastUpdatedBy"]);
		hunterAdminClientUserVM.set("tskMsgProvider", data["provider"]);
		hunterAdminClientUserVM.set("tskMsgOwner", data["msgOwner"]);
		
		var statusDropdownList = $("#tskMsgStatusDropdownList").data("kendoDropDownList");
		statusDropdownList.value(data["msgLifeStatus"]["statusValue"]);
		statusDropdownList.refresh();
		
		var statusDropdownList = $("#msgMsgType").data("kendoDropDownList");
		statusDropdownList.value(data["msgTaskType"]);
		statusDropdownList.refresh();
		
	},
	getMessageDetails : function(){
		var clientBean = this.get("clientBean"); 
		console.log("Client Bean > " + JSON.stringify(clientBean));
		var msgDetails = {
			"msgSendDate" : hunterAdminClientUserVM.get("taskMsgSendDate"),
			"msgTaskType" : hunterAdminClientUserVM.get("taskMsgTypeVal") != null ? hunterAdminClientUserVM.get("taskMsgTypeVal")["value"] : null, 
			"desiredReceivers" : hunterAdminClientUserVM.get("desiredReceivers"),
			"actualReceivers" : hunterAdminClientUserVM.get("actualReceivers"),
			"confirmedReceivers" : hunterAdminClientUserVM.get("confirmedReceivers"),
			"msgId" : hunterAdminClientUserVM.get("tskMsgId"),
			"msgDeliveryStatus" : hunterAdminClientUserVM.get("tskMsgDelStatus"),
			"msgLifeStatus" : hunterAdminClientUserVM.get("tskMsgLifeStatus"),
			"msgText" : hunterAdminClientUserVM.get("tskMstTxt"),
			"text" : hunterAdminClientUserVM.get("tskMstTxt"),
			"cretDate" : hunterAdminClientUserVM.get("tskMsgCretDate"),
			"createdBy" : hunterAdminClientUserVM.get("tskMsgCretBy"),
			"lastUpdate" : hunterAdminClientUserVM.get("tskMsgLstUpdate"),
			"lastUpdatedBy" : hunterAdminClientUserVM.get("tskMsgLstUpdatedBy"),
			"provider" : hunterAdminClientUserVM.get("tskMsgProvider"),
			"msgOwner" : hunterAdminClientUserVM.get("tskMsgOwner")
		};
		var data = JSON.stringify(msgDetails);
		console.log("Successfully obtained message details :  \n " + data);
		var taskId = hunterAdminClientUserVM.get("selTaskId"); 
		console.log("Selected task Id : " + taskId); 

		$.ajax({
			url: "http://localhost:8080/Hunter/message/action/tskMsg/create/" + taskId,
		      data : data,
		      method: "POST",
		      contentType : "applicaiton/json",
		      dataType : "json"
		}).done(function(data) {
			var json = jQuery.parseJSON(data);
			alert(json);
		 }).fail(function(data) {
			 var json = jQuery.parseJSON(data);
			 console.log("Failed to process task !!!!!!!");
			 console.log(json);
			 kendoKipHelperInstance.popupWarning(data.statusText + " (" + json.status + ")", "Error");
		 });
		
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
		this.set("selTaskId", taskId);
		/*
		var model = this.get("hunterClientTaskGrid").dataSource.get(taskId);
		var data  = JSON.stringify(model);
		var url = "http://localhost:8080/Hunter/task/action/processTask/" + taskId;
		kendoKipHelperInstance.ajaxPostDataForJsonResponse(data, "application/json", "json", "POST", url , "hunterAdminClientUserVM.afterProcessTask");*/
		var html = $("#processTaskProgressPopupTemplate").html();
		kendoKipHelperInstance.showWindowWithOnClose(html,"Task Process Progress");
		$(".k-window-action k-link").css("visibility", "hidden");
		var taskProcessManager_ = this.get("taskProcessManager");
		taskProcessManager_.execute();
	},
	afterProcessTask : function(data){
		var json = jQuery.parseJSON(data);
		var status = json.status;
		if(status == "Failed"){
			var errors = json.errors;
			errors = replaceAll(errors, ",", "</br>");  
			kendoKipHelperInstance.showSimplePopup("Error Processing Task!","<span style='color:red;' >" + errors + "</span>");
		}else if(status == null || status === ""){ 
			kendoKipHelperInstance.showSimplePopup("Success!!","<span style='color:green;' >Successfully processed Task!</span>");
		}
		
		var ds = hunterAdminClientUserVM.get("hunterClientTaskGrid").dataSource;
		ds.read();
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
               { field: "tskMsgType", title: "Type", width: 65 },
               { field: "cretDate", title: "Created On", width: 120 },
               { field: "createdBy", title: "Created By", width: 150 },
               { field: "lastUpdate", title: "Last Motified", width: 120 },
               { field: "updatedBy", title: "Motified By", width: 150 },
               { field: "taskDeliveryStatus", title: "Progress", width: 80 , template : "#=getProgressTemplate()#" },
               { field: "taskLifeStatus", title: "Status", width: 70, template : "#=getTaskEditStatusTemplate()#"},
               { field: "message", title: "Message", width: 60 , template : "#=getTaskMessageTemplate()#" },
               { field: "taskRegion", title: "Region", width: 60,template : "#=getTaskRegionTemplate()#"  },
               { field: "loadDetails", title : "Details", width:60, template : "#=getTaskLoadDetailsTemplate()#"},
               { "name": "taskHistory", title : "History", width:60, template : "#=getTaskHistoryTemplate()#"},
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
        kendoKipHelperInstance.showPercentMeasuredOKCancelTitledPopup(content, "Task Details", "45", "70");
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
	
	/* This is purely for task groups */
	receiverGroupDS : new kendo.data.DataSource({
		  transport: {
		    read:  {
		      url: function(){
		    	  var baseUrl = "http://localhost:8080/Hunter/task/action/tskGrp/read/";
		    	  var taskid = hunterAdminClientUserVM.get("selTaskId");
		    	  if(taskid == null){
		    		  taskid = 0;
		    	  }
		    	  var url = baseUrl + taskid;
		    	  console.log("Receiver group read url for task : " + url);
		    	  return url;
		      },
		      dataType: "json",
		      contentType:"application/json",
		      method: "POST"
		    },
		    create: {
		        url : function(){
			    	  var baseUrl = "http://localhost:8080/Hunter/task/action/tskGrp/create/";
			    	  var taskid = hunterAdminClientUserVM.get("selTaskId");
			    	  var url = baseUrl + taskid;
			    	  console.log("Receiver group create url for task : " + url);
			    	  return url;
			      },
		        dataType: "json", 
		        contentType:"application/json",
		        method:"POST",
		        success: function(result) {
		            kendoKipHelperInstance.popupWarning('', JSON.stringify(result), "Success");
		         }
		    },
		    destroy: {
		    	 url : function(){
			    	  var baseUrl = "http://localhost:8080/Hunter/task/action/tskGrp/destroy/";
			    	  var taskid = hunterAdminClientUserVM.get("selTaskId");
			    	  var url = baseUrl + taskid;
			    	  console.log("Receiver group destroy url for task : " + url);
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
		    	model:ReceiverGroupModel
		  },
		  error: function (e) {
		        kendoKipHelperInstance.popupWarning(e.status, e.errorThrown, "Error");
		  },
		  requestStart: function(e) {
		        var type = e.type;
		        var message = null;
		        if(type === 'read')
		        	return;
		        if(type === 'update')
		        	message = "Updating record...";
		        if(type === 'destroy')
		        	message = "Deleting record...";
		        if(type === 'create')
		        	message = "Creating record...";
		       
		        if(message != null){
		        	kendoKipHelperInstance.popupWarning(message, "Success");
		        }
		        
		  },
		  requestEnd: function(e) {
		        var response = e.response;
		        var type = e.type;
		        var message = null;
		        if(type === 'read')
		        	/*if(response !== 'undefined' && response != null){
		        		message = "Successfully obtained ( "+ response.length +" ) records!";
		        	}else{
		        		return;
		        	}*/
		        if(type === 'update')
		        	message = "Successfully updated record!";
		        if(type === 'destroy')
		        	message = "Successfully deleted record!";
		        if(type === 'create')
		        	message = "Successfully created record!";
		        
		        if(message != null){
		        	kendoKipHelperInstance.popupWarning(message, "Success");
		        }
		  },
		  pageSize:1000
	}),
	addGroupToTask : function(){
		var taskId = this.get("selTaskId");
		var selTaskGroup = this.get("taskGroupDropDownSelVal");
		var data = [{"taskId" : taskId, "groupId" : selTaskGroup}];
		var url = HunterConstants.HUNTER_BASE_URL + "/task/action/tskGrp/create";
		data = JSON.stringify(data);
		console.log("Loading group dropdown data...");
		kendoKipHelperInstance.ajaxPostData(data, "application/json", "json", "POST", url , "hunterAdminClientUserVM.afterAddingGroupToTask");
	},
	afterAddingGroupToTask : function(data){
		var dataStr = data+"";
		var dataJson = jQuery.parseJSON(dataStr);
		var status = dataJson["status"]; 
		var message = dataJson["Message"];
		var groupReceiverCount = dataJson["groupReceiverCount"];
		if(status != null && message != null){
			if(status !== "Failed"){
				kendoKipHelperInstance.popupWarning(status + " : " + message, "Success");
				var tskGrpDs = this.get("receiverGroupDS");
				tskGrpDs.read();
				hunterAdminClientUserVM.set("taskGroupsTotalReceivers", groupReceiverCount);
			}else{
				kendoKipHelperInstance.showErrorNotification(status + " : " + message);
			}
		}		
	},
	loadGroupDropdown : function(taskId){
		var url = HunterConstants.HUNTER_BASE_URL + "/messageReceiver/action/group/dropDown/" + taskId;
		console.log("Loading group dropdown data...");
		kendoKipHelperInstance.ajaxPostDataForJsonResponse(null, "application/json", "json", "POST", url , "hunterAdminClientUserVM.afterLoadingGroupDropdown");
		console.log("Successfully loaded group dropdown data!!");
	},
	afterLoadingGroupDropdown : function(data){
		var json = jQuery.parseJSON(data);
		console.log(JSON.stringify(json));
		hunterAdminClientUserVM.set("taskGroupDropdownData",json);
	},
	showPopupToDeleteTaskGroup : function(id, message){
		kendoKipHelperInstance.showOKToDeleteItem(id, message,"hunterAdminClientUserVM.deleteSelTaskGroup");
	},
	deleteSelTaskGroup : function(id){
		kendoKipHelperInstance.closeHelperKendoWindow();
		var taskId = this.get("selTaskId");
		var data = [{"taskId" : taskId, "groupId" : id}];
		data = JSON.stringify(data);
		$.ajax({
			url: HunterConstants.HUNTER_BASE_URL + "/task/action/tskGrp/destroy",
		    method: "POST",
		    data : data,
		    dataType : "json", 
		    contentType : "application/json"
		}).done(function(data) {
			var dataStr = data+"";
			var dataJson = jQuery.parseJSON(dataStr);
			var status = dataJson["status"]; console.log(status);
			var message = dataJson["Message"];console.log(message); 
			var groupReceiverCount = dataJson["groupReceiverCount"];console.log(groupReceiverCount);
			if(status != null && message != null){
				kendoKipHelperInstance.popupWarning(status + " : " + message, "Success");
				if(status !== "Failed"){
					hunterAdminClientUserVM.set("taskGroupsTotalReceivers", groupReceiverCount);
					hunterAdminClientUserVM.getAllReceiversForTaskReceivers();
				}
			}else{
				kendoKipHelperInstance.popupWarning("<span style='color:yellow;' >Finished process. Status unknown!</span>", "Success");
			}	
		 }).fail(function(data) {
			 var json = jQuery.parseJSON(data);
			 kendoKipHelperInstance.popupWarning("Failed to load task receiver count ! " + data.statusText + " (" + json.status + ")", "Error");
		 });
		var tskGrpDs = this.get("receiverGroupDS");
		tskGrpDs.read();
		hunterAdminClientUserVM.getAllReceiversForTaskReceivers();
		
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
	getSelectedTaskBean : function(){
		var selTaskId = this.get("selTaskId"); 
		var task = this.get("hunterClientTaskGrid").dataSource.get(selTaskId);
		return task;
	},
	
	
	
	/*::::::::::::::::::::    This is purely for selected task regions grid       :::::::::::::::::::::::::::::*/
	
	selTaskId : null,
	taskUniqueReceivers : 0,
	isTaskRegionOpen : false,
	isMsgViewOpen : false, 
	
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
	        	hunterAdminClientUserVM.getAllReceiversForTaskReceivers()
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
	deleteSelectedTaskRegion : function(regionId){
		kendoKipHelperInstance.closeHelperKendoWindow();
		var taskId = this.get("selTaskId");
		var url = HunterConstants.HUNTER_BASE_URL+"/region/action/task/regions/delete/requestBody";
		var data = {"regionId" : regionId, "taskId" : taskId};
		data = JSON.stringify(data);
		kendoKipHelperInstance.ajaxPostData(data, "application/json", "json", "POST", url , "hunterAdminClientUserVM.afterDeleteSelectedTaskRegion");
	},
	afterDeleteSelectedTaskRegion : function(data){
		data = jQuery.parseJSON(data);
		if(data != null){
			var status = data[HunterConstants.STATUS_STRING];
			var message = data[HunterConstants.MESSAGE_STRING];
			var count = data["regionCount"]; 
			if(count != null){
				hunterAdminClientUserVM.set("taskUniqueReceivers", count);
			}
			kendoKipHelperInstance.popupWarning(status + " : " + message, "Success"); 
		}else{
			kendoKipHelperInstance.popupWarning("<span style='color:yellow;' >Finished process. Status unknown!</span>", "Success");
		}
		var ds = hunterAdminClientUserVM.get("selTaskRegionsDS");
		ds.read();
		hunterAdminClientUserVM.getAllReceiversForTaskReceivers();
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
	},
	getTaskGroupsTotalReceivers : function(){
		var taskId = this.get("selTaskId");
		var ds = this.get("hunterClientTaskGrid").dataSource;
		var model = ds.get(taskId);
		var groups = model.get("taskGroups");
		var count = 0;
		if(groups != null && groups !== "undefined"){
			for(var i=0; i<groups.length; i++){
				var thisGroup = groups[i];
				var count_ = thisGroup["receiverCount"];
				count += count_;
			}
		}
		console.log("Total receivers for groups : " + count);
		this.set("taskGroupsTotalReceivers", count); 
	},
	getAllReceiversForTaskReceivers : function(){
		console.log("Updating combined receiver counts...");
		 this.getTotalReceivers();
		 var regionsCount = this.get("taskUniqueReceivers"); 
		 var groupsCount = this.get("taskGroupsTotalReceivers");
		 var total = regionsCount + groupsCount;
		 console.log("combined receivers : " + total);
		 this.set("taskAllCombinedReceivers",total);
		 console.log("Finished updating combined receiver counts!!"); 
	}, 
	
	
	
	
	
	
	
	/*:::::::::::::::::::::::::::::::::::::::::::::::::   This is only for email editing   :::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	
	
	
	
	
	taskEmailHtml : null,
	isEmailSectionOpen : null,
	isParamsSectionOpen : true,
	currentParamButtonText : "Show Parameters",
	emailTemplateName : null,
	emailMsgCretDate : '',
	emailMsgCretBy : '',
	emailMsgLstUpdate : '',
	emailMsgLstUpdatedBy : '',
	emailSubject : '',
	
	
	hasAttachment : true,
	tskEmailMsgStatus : null,
	taskEmailMsgSendDate : null,
	msgDeliveryStatus : null,
	
	existentTemplateNames : [],
	currentTemplateIndx : -1,
	getNextTemplateName : function(){
    	var current = this.get("currentTemplateIndx");
    	console.log(":::::::::::current:::::::: " + current);
    	console.log("Length : " + this.get("existentTemplateNames").length);
    	if( (current + 1) >= this.get("existentTemplateNames").length){  
    		current = -1;
    	}
    	current += 1;
    	console.log(":::::::::::current after:::::::: " + current);
    	this.set("currentTemplateIndx", current);
    	var name = this.get("existentTemplateNames")[current];
    	console.log("returning name ( " + name + " )"); 
    	return name;
    },
    getPrevTemplateName : function(){
    	var current = this.get("currentTemplateIndx");
    	console.log(":::::::::::current:::::::: " + current);
    	console.log("Length : " + this.get("existentTemplateNames").length);
    	if( (current - 1) <= 0){  
    		current = (this.get("existentTemplateNames").length);
    	}
    	current -= 1;
    	console.log(":::::::::::current after:::::::: " + current);
    	this.set("currentTemplateIndx", current);
    	var name = this.get("existentTemplateNames")[current];
    	console.log("returning name ( " + name + " )"); 
    	return name;
    },
	loadExistentEmailTemplates : function(){
		var url = HunterConstants.HUNTER_BASE_URL + "/message/action/tskMsg/email/getAllTemplateNames";
		kendoKipHelperInstance.ajaxPostDataForJsonResponse(null, "application/json", "json", "POST",url,"hunterAdminClientUserVM.afterFetchingAllTemplateNames" );
	},
	afterFetchingAllTemplateNames : function(data){
		var json = $.parseJSON(data);
		for(var i=0; i<json.length;i++){
			var name = json[i];
			hunterAdminClientUserVM.get("existentTemplateNames").push(name);
		}
	},
	onChangeTaskEmailMsgSendDate : function(e){
		
	},
	saveEmailMessageConfigurations : function(){
		console.log("Saving email message configurations..."); 
		this.saveCurrentEditorContent();
	},
	templatedHtml : function(){
		return this.get("taskEmailHtml"); 
	},
	onChangeTaskEmailHtml: function() {
        console.log("event :: change (" + kendo.htmlEncode(this.get("taskEmailHtml")) + ")");
    },
    saveCurrentEditorContent : function(){
    	if(this.getWordCount() >= 4000 || this.getWordCount() == 0 ){
    		kendoKipHelperInstance.showErrorOrSuccessMsg("Error", "Email cannot be 0 or more than 4000 characters!");
    		return;
    	}
    	if(!this.validateSubject()){
    		kendoKipHelperInstance.showErrorOrSuccessMsg("Error", "Task subject is required.");
    		return;
    	}
    	if(this.get("emailTemplateName") == null || this.get("emailTemplateName").trim().length == 0){
    		kendoKipHelperInstance.showErrorOrSuccessMsg("Error", "Please select email template name before saving!");
    		return;
    	}
    	console.log("Saving contents... \n " + kendo.htmlEncode(this.get("taskEmailHtml") ));
    	var data = {
    			"msgDeliveryStatus" : this.get("msgDeliveryStatus"),
    			"tskId" : this.get("selTaskId"),
    			"emailTemplateName" : this.get("emailTemplateName"),
    			"hasAttachment" : this.get("hasAttachment"),
    			"tskEmailMsgStatus" : this.get("tskEmailMsgStatus"),
    			"taskEmailMsgSendDate" : this.get("taskEmailMsgSendDate"),
    			"taskEmailHtml" : this.get("taskEmailHtml"),
    			"emailSubject" : this.get("emailSubject")
    	};
    	data = JSON.stringify(data);
    	var url = HunterConstants.HUNTER_BASE_URL + "/message/action/tskMsg/email/createOrUpdate";
    	kendoKipHelperInstance.ajaxPostDataForJsonResponse(data, "application/json", "json", "POST",url,"hunterAdminClientUserVM.afterSaveEmailMsgConfigs" );
    },
    afterSaveEmailMsgConfigs : function(data){
    	console.log(data);
    	kendoKipHelperInstance.showErrorOrSuccessMsg("Success", "Configurations Saved!");
    },
    clearCurrentEditorContent : function(){
    	this.set("taskEmailHtml", "");
    	this.set("templatedHtml", "");
    },
    viewCurrentEditorContent : function(){
    	var isParamsSectionOpen = this.get("isParamsSectionOpen"); 
    	if(isParamsSectionOpen){
    		this.showEmailTemplateProcessed();
    	}
    	$("#emailTemplateProcessed").html(this.get("taskEmailHtml")); 
    },
    forwardTemplateMove : function(){
    	var templateName = this.getNextTemplateName();
    	this.loadForName(templateName);
    },
    loadForName : function(templateName){
    	this.updateTemplateNameTitle(templateName);
    	templateName = templateName.replace(/ /g, '+');
    	var url = HunterConstants.HUNTER_BASE_URL + "/message/action/tskMsg/email/getEmailTemplateForName/" + templateName;
    	$( "#allHunterEmailTemplates" ).load( url , function(data) {
    		console.log("...............................Loaded..................................................");
    		var replaced = hunterAdminClientUserVM.sanitizeString(data);
    		var html = $( "#allHunterEmailTemplates" ).html();
    		console.log(data);
    		$("#templateContentContainer").html(replaced);
    	});
    },
    sanitizeString : function(string){
    	var replaced = string.replace(/\\"/g, ':::_:::'); 
		replaced = replaced.replace(/"/g, '');
		replaced = replaced.replace(/:::_:::/g, '"');
		return replaced;
    },
    backwardTemplateMove : function(){
    	var templateName = this.getPrevTemplateName();
    	this.loadForName(templateName);
    },
    updateTemplateNameTitle : function(templateName){
    	$("#measuredWindowBackUp_wnd_title").html("<span style='text-align:center;font-weight:bolder;with:100%;' >" + templateName + "</span>");
    },
    displayAllCurrentTemplates : function(){
    	var templateName = this.getNextTemplateName();
    	templateName = templateName.replace(/ /g, '+');
    	var url = HunterConstants.HUNTER_BASE_URL + "/message/action/tskMsg/email/getEmailTemplateForName/" + templateName;
    	$( "#allHunterEmailTemplates" ).load( url , function() {
    		console.log("...............................Loaded..................................................");
    		var contents = $("#allHunterEmailTemplates").html();
    		var replaced = hunterAdminClientUserVM.sanitizeString(contents);
        	var part1 = "<div id='templateContentContainer' style='height:85%;with:96%;margin-left:1%;border:1px solid #A6CADE;border-radius:4px;overflow-y:scroll;padding:10px;'  >";
        	var part2 = 
        		"</div><table id='templatesInnerTable' style='with:40%;margin:0 auto;table-layout: fixed;' ><tr><td><a href='#' onClick='hunterAdminClientUserVM.backwardTemplateMove()' class='ui-btn ui-corner-all ui-icon-arrow-l ui-btn-icon-notext'></a></td><td>"+
        		"<button class='k-button' style=''background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);' onClick='hunterAdminClientUserVM.selectEmailTemplate()' ><span class='k-icon k-i-tick'></span>Select</button></td>"+
        		"<td><button class='k-button' style=''background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);' onClick='hunterAdminClientUserVM.closeTemplatesWindow()' ><span class='k-icon k-i-close'></span>Close</button></td>"+
        		"<td><a href='#' class='ui-btn ui-corner-all ui-icon-arrow-r ui-btn-icon-notext' onClick='hunterAdminClientUserVM.forwardTemplateMove()' ></a></td></tr></table>";
        	var html = $( "#allHunterEmailTemplates" ).html();
        	kendoKipHelperInstance.showBackUpPercentMeasuredOKCancelTitledPopup(part1+replaced+part2, templateName, "40", "60");
        	hunterAdminClientUserVM.updateTemplateNameTitle(templateName);
    	});
    },
    selectEmailTemplate : function(){
    	var current = this.get("currentTemplateIndx");
    	var name = this.get("existentTemplateNames")[current];
    	hunterAdminClientUserVM.set("emailTemplateName",name);
    	kendoKipHelperInstance.showErrorOrSuccessMsg("Success", "Successfully save template!");
    	hunterAdminClientUserVM.closeTemplatesWindow()
    },
    showPopupToDeleteEmail : function(){
    	var messageId = 1;
    	var message = "<span style='color:red' >Are you sure you want to delete the email?</span>";
    	var okButton = kendoKipHelperInstance.createSimpleHunterButton('tick','OK', 'hunterAdminClientUserVM.deleteSelectedEmailMsg("' + messageId +'")');
    	var cancelButton = kendoKipHelperInstance.createSimpleHunterButton('cancel','Cancel', 'kendoKipHelperInstance.closeHelperKendoWindow()');
    	var tableBut = "<table style='with:60%;margin-left:25%;' ><tr><td>" + okButton +"</td><td>" + cancelButton + "</td></tr></table>";
    	var content = "<br/>" + message + "<br/><br/>" + tableBut;
    	kendoKipHelperInstance.showPopupWithNoButtons("Delete Email Confirmation", content);
    },
    deleteSelectedEmailMsg : function(msgId){
    	kendoKipHelperInstance.closeHelperKendoWindow();
    	var url = HunterConstants.HUNTER_BASE_URL + "/message/action/tskMsg/email/deleteEmail/" + this.get("selTaskId");
    	kendoKipHelperInstance.ajaxPostData(null, "application/json", "json", "POST", url, "hunterAdminClientUserVM.afterDeletingEmailMsg");
    },
    afterDeletingEmailMsg : function(data){
    	data = $.parseJSON(data);
    	if(data != null){
    		var status = data.status;
    		var message = data.message;
    		kendoKipHelperInstance.showErrorOrSuccessMsg(status, message);
    	}
    },
    closeTemplatesWindow : function(){
    	$("#templatesInnerTable").remove();
    	$("#templateContentContainer").remove();
    	this.set("currentTemplateIndx", -1);
    	kendoKipHelperInstance.closeMeasuredWindowBackUp();
    },
    closeEmailTemplateSection : function(){
    	$("#emailEditTemplateCover").slideUp(500,function(){
    		$("#hunterUserDetailsStrip").slideDown(500,function(){
    			hunterAdminClientUserVM.set("isEmailSectionOpen", false);
    			hunterAdminClientUserVM.clearEmailConfigurations();
    			var ds = hunterAdminClientUserVM.get("selTaskRegionsDS");
    			ds.read();
    		});
		});
    }, 
    setEmailMsgvalues : function(message){
    	if(message != null){
    		this.set("taskEmailHtml", message.msgText);
    		this.set("emailMsgCretDate", message.cretDate);
    		this.set("emailMsgLstUpdate", message.lastUpdate);
    		this.set("emailMsgCretBy", message.createdBy);
    		this.set("lastUpdatedBy", message.lastUpdatedBy);
    		this.set("taskEmailMsgSendDate", message.msgSendDate);
    		this.set("tskEmailMsgStatus", message.msgLifeStatus);
    		this.set("hasAttachment", message.hasAttachment);
    		this.set("emailSubject", message.eSubject);
    		this.set("emailTemplateName",message.emailTemplateName); 
    	}
    },
    clearEmailConfigurations : function(){
    	hunterAdminClientUserVM.set("taskEmailHtml", null);
    	hunterAdminClientUserVM.set("emailMsgCretDate", null);
    	hunterAdminClientUserVM.set("emailMsgLstUpdate", null);
    	hunterAdminClientUserVM.set("emailMsgCretBy", null);
    	hunterAdminClientUserVM.set("lastUpdatedBy", null);
    	hunterAdminClientUserVM.set("taskEmailMsgSendDate", null);
    	hunterAdminClientUserVM.set("tskEmailMsgStatus", null);
    },
    showEmailTemplateProcessed : function(){
    	hunterAdminClientUserVM.set("currentParamButtonText", "Show Parameters");
    	$("#emailTemplateParamsEditor").slideUp(500,function(){
    		$("#emailTemplateProcessed").slideDown(500,function(){
    			hunterAdminClientUserVM.set("isParamsSectionOpen", false);
    		});
		});
    },
    refreshElemeAttributes : function(){
    	var msgId = this.getSelectedTaskBean().get("taskMessage")["msgId"]; 
    	var url = HunterConstants.HUNTER_BASE_URL + "/message/action/email/getRefreshValues/" + msgId;
    	kendoKipHelperInstance.ajaxPostDataForJsonResponse(null, "application/json", "json", "POST", url, "hunterAdminClientUserVM.afterGettingRefreshValues");
    },
    afterGettingRefreshValues : function(data){
    	data = $.parseJSON(data);
    	hunterAdminClientUserVM.set("emailMsgCretDate", data.CRETDATE);
    	hunterAdminClientUserVM.set("emailMsgLstUpdate", data.LASTUPDATE);
    	hunterAdminClientUserVM.set("emailMsgCretBy", data.CREATEDBY);
    	hunterAdminClientUserVM.set("lastUpdatedBy", data.LASTUPDATEDBY);
    	hunterAdminClientUserVM.set("taskEmailMsgSendDate", data.MSGSENDDATE);
    	hunterAdminClientUserVM.set("tskEmailMsgStatus", data.MSGLIFESTATUS);
    	hunterAdminClientUserVM.set("hasAttachment", data.HASATTACHMENT == 'true');
    	hunterAdminClientUserVM.set("emailSubject", data.E_SUBJECT);
    	hunterAdminClientUserVM.set("emailTemplateName",data.EML_TMPLT_NAM);
    },
    showEmailParameters : function(){
    	var isParamsSectionOpen = this.get("isParamsSectionOpen"); 
    	if(isParamsSectionOpen){
    		this.showEmailTemplateProcessed();
    	}else{
    		console.log("param section is closed. Opening it..."); 
    		hunterAdminClientUserVM.set("currentParamButtonText", "Close Parameters");
    		$("#emailTemplateProcessed").slideUp(500,function(){
        		$("#emailTemplateParamsEditor").slideDown(500,function(){
        			hunterAdminClientUserVM.set("isParamsSectionOpen", true);
        		});
    		});
    	}
    },
    getWordCount : function(){
    	var html = this.get("taskEmailHtml");
    	if(html == null){
    		$("#styledTickOK").css({"display":"none"});
    		$("#styledTickCancel").css({"display":""});
    		return "0";
    	}
    	var len = html.length;
    	if(len >=  4000){
    		$("#styledTickCancel").css({"display":""});
    		$("#styledTickOK").css({"display":"none"});
    		$("#wordCountSpan").css({"color":"red"});
    	}else{
    		$("#styledTickOK").css({"display":""}); 
    		$("#styledTickCancel").css({"display":"none"}); 
    		$("#wordCountSpan").css({"color":"green"});
    	}
    	return len;
    },
    
    validateSubject : function(){
    	var subject = this.get("emailSubject");
    	return subject != null && subject.length > 0;
    }
	
	
});






