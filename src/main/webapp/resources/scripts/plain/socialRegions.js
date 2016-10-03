
registerNavigation("Regions", "Social Regions");
var kendoKipHelperInstance = new kendoKipHelper();

var SocialRegionModel = kendo.data.Model.define({
	id:"regionId",
	fields : {
		"regionId" : {
			type : "number", validation : {required : true},editable:false, defaultValue:0
		},
		"regionName" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"population" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"regionDesc" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"countryId" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"countyId" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"consId" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"wardId" : {
			type : "number", validation : {required : true},editable:true, defaultValue:0
		},
		"coordinates" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"assignedTo" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"countryName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"countyName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"consName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"wardName" : {
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
	getSocialRegionsDelTemplate : function(){
		var id = this.get("regionId"),
			shortName = this.get("regionName"),
			idString = '"'+ id +'",',
			message = '"<u><b>'+ shortName +'</u></b> will be deleted. <br/> Are you sure?"', 
			html = kendoKipHelperInstance.createDeleteButton(false, 'kendoKipHelperInstance.showOKToDeleteItem('+  idString + message + ',"UserRoleVM.deleteUserRole"' +  ')');

		return html;
	}
});

var SocialRegionsDS = new kendo.data.DataSource({
	  transport: {
	    read:  {
	      url: "http://localhost:8080/Hunter/social/action/regions/read",
	      dataType: "json",
	      contentType:"application/json",
	      method: "POST"
	    },
	    create: {
	        url: "http://localhost:8080/Hunter/social/action/regions/createOrUpdate",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST",
	        success: function(result) {
	            kendoKipHelperInstance.popupWarning('', JSON.stringify(result), "Success");
	         }
	    },
	    destroy: {
	        url: "http://localhost:8080/Hunter/social/action/regions/destroy",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    update: {
	        url: "http://localhost:8080/Hunter/social/action/regions/createOrUpdate",
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
	    	model:SocialRegionModel
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

var SocialRegionsVM = kendo.observable({
	
	SocialRegionsDS_ : SocialRegionsDS,
	isEverVisible : true,
	
	beforeInit : function(){
		console.log("Before initializing social regions VM..."); 
	},
	init : function(){
		console.log("Initializing social regions VM...");
		kendo.bind($("#hunterSocialRegionsContainer"), SocialRegionsVM);
	},
	afterInit : function(){
		console.log("Finishing up initializing social regions VM...");
	},
	deleteUserRole : function(id){
		kendoKipHelperInstance.closeHelperKendoWindow();
		var data = {"regionId" : id};
		data = JSON.stringify(data);
		kendoKipHelperInstance.ajaxPostData(data, "application/json", "json", "POST", HunterConstants.getHunterBaseURL("social/action/regions/delete")  , "SocialRegionsVM.afterDeletingSocialRegion");
	},
	afterDeletingSocialRegion : function(data){
		data = $.parseJSON(data);
    	if(data != null){
    		var status = data.status;
    		var message = replaceAll(data.message, ",", "<br/>");
    		if( status != null && HunterConstants.STATUS_SUCCESS === status ){
    			SocialRegionsVM.get("SocialRegionsDS_").read(); 
    			kendoKipHelperInstance.showSimplePopup("Successfully Deleted User Role", "<span style='color:green' >"+ message +"</span>");
    		}else{
    			kendoKipHelperInstance.showSimplePopup("Error Deleting User Role", "<span style='color:red' >"+ message +"</span>");
    		}
    	}
	}
});

$("document").ready(function(){
	SocialRegionsVM.init();
});







