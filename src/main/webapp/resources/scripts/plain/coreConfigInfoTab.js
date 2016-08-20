
$("document").ready(function(){
	
	var hunterMainTabstrip_ = $("#hunterMainCoreConfigTab").kendoTabStrip({
        animation: { 
       	 open: { effects: "fadeIn"} 
        },
        contentUrls: [
                      '/Hunter/admin/action/templates/roles',
                      '/Hunter/admin/action/templates/msgAttachments'
                  ]
    }).data("kendoTabStrip");
		 
		 setTimeout(function(){
			 hunterMainTabstrip_.select(0);
		 }, 300);
	
});