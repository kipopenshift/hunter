
var kendoKipHelperInstance;

var RawReceiverModel = kendo.data.Model.define({
	id:"rawReceiverId",
	fields : {
		"rawReceiverId" : {
			type : "number", validation : {required : true},editable:false, defaultValue:0
		},
		"receiverContact" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"receiverType" : {
			type : "string", validation : {required : true},editable:true, defaultValue:null
		},
		"firstName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:"Draft"
		},
		"lastName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
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
		"consWardName" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"village" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"verified" : {
			type : "boolean", validation : {required : true},editable:false, defaultValue:false
		},
		"verifiedBy" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"createdBy" : {
			type : "boolean", validation : {required : true},editable:false, defaultValue:false
		},
		"lastUpdate" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"cretDate" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		},
		"lastUpdatedBy" : {
			type : "string", validation : {required : true},editable:false, defaultValue:null
		}
	}
});

var RawReceiverDS = new kendo.data.DataSource({
	  transport: {
	    read:  {
	      data : {
	    	  getMode : "date"
	      },
	      url: HunterConstants.getHunterBaseURL("rawReceiver/action/raw/getRawReceiversForValidation"), 
	      dataType: "json",
	      contentType:"application/json",
	      method: "POST",
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
	    	model:RawReceiverModel
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
	        //var response = e.response;
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

var ValidateRawReceiverVM = kendo.observable({
	rawReceiverDS : RawReceiverDS,
	beforeInit : function(){
		kendoKipHelperInstance =  new kendoKipHelper();
		kendoKipHelperInstance.init();
	},
	init : function(){
		console.log("Hey");
		alert("hey!");
		console.log("Initializing validate raw receiver VM...");
		this.beforeInit();
		kendo.bind($("#validateRawReceiversContainer"), ValidateRawReceiverVM);
		this.afterInit();
		console.log("Done initializing validate raw receiver VM!!");
	},
	afterInit : function(){
		
	}
	
	
});


$("document").ready(function(){
	ValidateRawReceiverVM.init();
});