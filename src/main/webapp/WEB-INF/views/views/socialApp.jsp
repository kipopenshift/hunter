<%@include file="../common/HeaderFile.jsp"%>

<script src="/Hunter/static/resources/scripts/plain/socialApp.js"></script>

<div id="hunterSocialAppContainer" >

	<div 
			style="height:450px;"
			data-role="grid"
			data-height="800"
			id="hunterSocialAppGrid" data-scrollable="true"
	        data-editable="{'mode' : 'popup'}" 
	        data-toolbar='["create"]'
	        data-pageable="{
	         	refresh:true
	         }"
			data-columns="[
	         		{ 'field': 'appId', title : 'App ID', 'width':'70px'   },
	         		{ 'field': 'appName', title : 'App Name'  },
	         		{ 'field': 'appDesc', title : 'App Description'  },
	         		{ 'field': 'extrnlId', title : 'External ID'  },
	         		{ 'field': 'socialType', title : 'Social Type'  },
	         		{ 'field': 'createdBy', title : 'Created By'  },
	         		{ 'field': 'lastUpdatedBy', title : 'Last Updated By'  },
	         		{ 'field': 'cretDate', title : 'Created On'  },
	         		{ 'field': 'lastUpdate', title : 'Updated On'  },
	         		{ 'field': '', title : 'Edit', template : '#=getSocialAppDelTemplate()#', 'width':'70px'  }	  
	      		]"
	       data-bind="source:SocialAppDS_, visible:isEverVisible"
		>
		</div>

</div>




<%@include file="../common/pageCloser.jsp"%>