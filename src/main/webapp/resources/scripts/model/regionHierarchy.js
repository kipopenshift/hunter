var kendoWindow;
var kendoKipHelperInstance;


var RegionHierarchyModel = kendo.data.TreeListModel.define({
	id:"id",
	expanded : false,
	fields : {
		"id" : {
			type : "number", validation : {required : true},editable:false, defaultValue:"0"
		},
		"beanId" : {
			type : "number", validation : {required : true},editable:false, defaultValue:"0"
		},
		"parent" : {
			type : "number", validation : {required : false},editable:false,nullable : true
		},
		"genParent" : {
			type : "number", validation : {required : false},editable:false,nullable : true
		},
		"name" : {
			type : "string",editable:true, validation: { required: false }
		},
		"population" : {
			type : "number", validation : {required : false},editable:false
		},
		"hunterPopuplation" : {
			type : "number", validation : {required : false},editable:false
		},
		"mapDots" : {
			type : "string", validation : {required : false},editable:false
		},
		"levelType" : {
			type : "string", validation : {required : false},editable:false
		},
		"regionCode" : {
			type : "string", validation : {required : false},editable:false
		},
		"city" : {
			type : "string", validation : {required : false},editable:false
		},
		"hasState" : {
			type : "boolean", validation : {required : false},editable:false
		},
		"cretDate" : {
			type : "date", validation : {required : false},editable:false
		},
		"createdBy" : {
			type : "string", validation : {required : false},editable:false
		},
		"lastUpdate" : {
			type : "date", validation : {required : false},editable:false
		},
		"lastUpdatedBy" : {
			type : "string", validation : {required : false},editable:false
		},
		"parentId": { 
			field: "genParent", nullable: true
		}
	},
	getEditHierarchyTemplate : function(){
		return kendoKipHelperInstance.createContextEditButton(false);
	}
});

function getSelCountry(){
	if(RegionHierarchyVM == null){
		console.log("Current selected country >> ");
		return "Kenya";
	}else{
		var selCountry = RegionHierarchyVM.get("selCountry");
		selCountry = selCountry == null ? "/Kenya" : "/" + selCountry;
		console.log("Current Selected country >> " + selCountry);
		return selCountry;
	}
}

var RegionHierarchyTreeListDS = new kendo.data.TreeListDataSource({
	  
	transport: {
	    read:  {
	      url: function(){
	    	  var url =  "http://localhost:8080/Hunter/region/action/regions/hierarchies/action/read/post" + getSelCountry();
	    	  console.log("URL >> " + url);
	    	  return url;
	      },
	      dataType: "json",
	      method: "POST"
	    },
	    create: {
	        url: "http://localhost:8080/Hunter/region/action/regions/hierarchies/action/create",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST",
	        success: function(result) {
	            kendoKipHelperInstance.popupWarning('', JSON.stringify(result), "Success");
	         }
	    },
	    update: {
	        url: "http://localhost:8080/Hunter/region/action/regions/hierarchies/action/update",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    destroy: {
	        url: "http://localhost:8080/Hunter/region/action/regions/hierarchies/action/destroy",
	        dataType: "json", 
	        contentType:"application/json",
	        method:"POST"
	    },
	    parameterMap: function(options, operation) {
	          if (operation == "read") {
	        	  console.log("Reading data...");
	          }else if(operation === "destroy" || operation === "update" || operation === "create"){ 
	        	  var json1 = kendo.stringify(options);
	        	  console.log(json1);
	        	  return json1;
	          }else{
	        	  kendoKipHelperInstance.popupWarning("Bad operation >> " + operation,"Error");
	          }
	    }
	  },
	  schema: {
	    	model:RegionHierarchyModel
	  },
	  error: function (e) {
	        kendoKipHelperInstance.popupWarning(e.status, e.errorThrown, "Error");
	  },
	  pageSize:2000
});

var RegionHierarchyVM = kendo.observable({
	
	treeList : null,
	selCountry : null,
	navDir : null,
	isUploadReceiversEnabled: true,
    isUploadReceiversVisible: true,
    receiverUploadErrors : false,
	
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
	beforeInit : function(){
		kendoKipHelperInstance = new kendoKipHelper();
		kendoKipHelperInstance.init();
		console.log("Preparing to initialized RegionHierarchyVM...");
	},
	init : function(){
		console.log("Initializing RegionHierarchyVM...");
		this.beforeInit();
		kendo.bind($("#regionHierarchyTreeListVMContainer"), RegionHierarchyVM);
		this.createKendoTreeList();
		this.afterInit();
	},
	afterInit : function(){
		console.log("RegionHierarchyVM successfully initialized!!");
	},
	onChangeCountry : function(){
		console.log("Selected country >> " + selCountry); 
	},
	openSectionToUploadReceivers : function(){
		$("#regionHierarchyTreeList").slideUp(1000, function(){
			$("#uploadReceiversContainer").slideDown(1000);
		});
	},
	cancelUploadPopup : function(){
		kendo.destroy($("#receiverFilesInput"));
		kendoKipHelperInstance.closeMeasuredWindow();
	},
	onSelect: function(e) {
        var message = $.map(e.files, function(file) { return file.name; }).join(", ");
        console.log("event :: select (" + message + ")");
        RegionHierarchyVM.set("receiverUploadErrors", false);
    },
    onError : function(){
    	RegionHierarchyVM.set("receiverUploadErrors", true);
    },
    onComplete : function(){
    	var isError = RegionHierarchyVM.get("receiverUploadErrors");
    	if(isError){
    		kendoKipHelperInstance.popupWarning("Error!!", "Error");
    	}else{
    		kendoKipHelperInstance.popupWarning("Uploaded successfully!!", "Error");
    	}
    },
    closeUploadDivAndShowGrid : function(){
    	$("#uploadReceiversContainer").slideUp(1000, function(){
        	$("#regionHierarchyTreeList").slideDown(1000);
        });
    },
	createKendoTreeList : function(){
		console.log("Creating RegionHierarchyTreeList list...");
		$("#regionHierarchyTreeList").kendoTreeList({
			toolbar : kendo.template($("#regionHierarchyToolBarTemplate").html()),
			dataSource : RegionHierarchyTreeListDS,
			height: 700,
			 messages: {
				 loading: "Loading region hierarchies..."
			 },
			 filterable: {
			     extra: false
			 },
			 pageable :false,
	        columns: [	
						{ field:'name', title:'Name', filterable: true },
						{ field:'population', title:'Population', filterable: true,width:140 },
						{ field:'hunterPopuplation', title:'Hunter Population', width:140, filterable: true },
						{ field:'city', title:'City',  filterable: true, width:200 },
						{ field:'levelType', title:'Type', filterable: true,width:110  },
						{ field:'regionCode', title:'Code', filterable: true,width:110 },
						{ field:'hasState', title:'Has State',  filterable: true,width:110},
						{ field:'receivers', title:'Receivers',  filterable: true,width:110 },
						{ field: "editTask", title : "Edit", width:60, template : "#=getEditHierarchyTemplate()#"}
	         ],
	          editable: "inline"
		});
		console.log("Successfully created RegionHierarchyTreeList");
	}
});

