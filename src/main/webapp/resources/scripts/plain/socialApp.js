
registerNavigation("Groups", "Social Apps");
var kendoKipHelperInstance = new kendoKipHelper();

var SocialAppModel = kendo.data.Model.define({
	id:"appId",
	fields : {
		"appId" : {
			type : "number", validation : {required : true},editable:false, defaultValue:0
		},
		"appName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"appDesc" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"extrnlId" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"extrnalPassCode" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"socialType" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
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
	getSocialAppDelTemplate : function(){
		var id = this.get("appId"),
			shortName = this.get("appName"),
			idString = '"'+ id +'",',
			message = '"<u><b>'+ shortName +'</u></b> will be deleted. <br/> Are you sure?"', 
			html = kendoKipHelperInstance.createDeleteButton(false, 'kendoKipHelperInstance.showOKToDeleteItem('+  idString + message + ',"UserRoleVM.deleteUserRole"' +  ')');

		return html;
	}
});

var SocialAppDS = new kendo.data.DataSource({
	  transport: {
	    read:  {
	      url: "http://localhost:8080/Hunter/social/action/apps/read",
	      dataType: "json",
	      contentType:"application/json",
	      method: "POST"
	    },
	    create: {
	        url: "http://localhost:8080/Hunter/social/action/apps/created",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST",
	        success: function(result) {
	            kendoKipHelperInstance.popupWarning('', JSON.stringify(result), "Success");
	         }
	    },
	    destroy: {
	        url: "http://localhost:8080/Hunter/social/action/apps/destroy",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    update: {
	        url: "http://localhost:8080/Hunter/social/action/apps/update",
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
	    	model:SocialAppModel
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
	  pageSize:100
});

var SocialAppVM = kendo.observable({
	
	SocialAppDS_ : SocialAppDS,
	isEverVisible : true,
	
	beforeInit : function(){
		console.log("Before initializing user role VM..."); 
	},
	init : function(){
		console.log("Initializing user role VM...");
		kendo.bind($("#hunterSocialAppContainer"), SocialAppVM);
	},
	afterInit : function(){
		console.log("Finishing up initializing user role VM...");
	},
	deleteUserRole : function(id){
		kendoKipHelperInstance.closeHelperKendoWindow();
		var data = {"userRoleId" : id};
		data = JSON.stringify(data);
		kendoKipHelperInstance.ajaxPostData(data, "application/json", "json", "POST", HunterConstants.getHunterBaseURL("admin/action/roles/delete")  , "UserRoleVM.afterDeletingUserRole");
	},
	afterDeletingUserRole : function(data){
		data = $.parseJSON(data);
    	if(data != null){
    		var status = data.status;
    		var message = replaceAll(data.message, ",", "<br/>");
    		if( status != null && HunterConstants.STATUS_SUCCESS === status ){
    			UserRoleVM.get("userRoleDS").read(); 
    			kendoKipHelperInstance.showSimplePopup("Successfully Deleted User Role", "<span style='color:green' >"+ message +"</span>");
    		}else{
    			kendoKipHelperInstance.showSimplePopup("Error Deleting User Role", "<span style='color:red' >"+ message +"</span>");
    		}
    	}
	}
});

$("document").ready(function(){
	SocialAppVM.init();
});







