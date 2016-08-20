
$("document").ready(function(){
	
	var hunterMainTabstrip_ = $("#taskConfigTab").kendoTabStrip({
        animation: { 
       	 open: { effects: "fadeIn"} 
        },
        contentUrls: [
                      '/Hunter/admin/action/templates/emailTemplatesObjTab',
                      '/Hunter/admin/action/templates/templateTextEditor'
                  ]
    }).data("kendoTabStrip");
		 setTimeout(function(){
			 hunterMainTabstrip_.select(0);
		 }, 300);
	
});