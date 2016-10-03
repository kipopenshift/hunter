<%@include file="../common/HeaderFile.jsp"%>

<script src="/Hunter/static/resources/scripts/plain/socialRegions.js"></script>

<div id=hunterSocialRegionsContainer >

	<div 
			style="height:450px;"
			data-role="grid"
			data-height="800"
			id="hunterSocialRegionsGrid" data-scrollable="true"
	        data-editable="{'mode' : 'popup'}" 
	        data-toolbar='["create"]'
	        data-pageable="{
	         	refresh:true
	         }"
			data-columns="[
	         		{ 'field': 'regionId', 		title : 'Region ID', 'width':'70px'   },
	         		{ 'field': 'regionName', 	title : 'Region Name'  },
	         		{ 'field': 'assignedTo', 	title : 'Assigned To'  },
	         		{ 'field': 'countryName', 	title : 'Country'  },
	         		{ 'field': 'countyName', 	title : 'County'  },
	         		{ 'field': 'consName', 		title : 'Constituency'  },
	         		{ 'field': 'wardName', 		title : 'Ward Name', 'width':'70px'   },
	         		{ 'field': 'cretDate', 		title : 'Created On'  },
	         		{ 'field': 'createdBy', 	title : 'Created By'  },
					{ 'field': 'lastUpdatedBy', title : 'Updated By'  },
	         		{ 'field': 'lastUpdate', 	title : 'Updated On'  },
	         		
	         		{ 'field': '', title : 'Edit', template : '#=getSocialRegionsDelTemplate()#', 'width':'70px'  }
	         			  
	      		]"
	       data-bind="source:SocialRegionsDS_, visible:isEverVisible"
		>
		</div>

</div>




<%@include file="../common/pageCloser.jsp"%>