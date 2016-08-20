<%@include file="../common/HeaderFile.jsp"%>

<script src="/Hunter/static/resources/scripts/plain/validateRawReceivers.js"></script>

<style>
	#validateRawReceiversContainer{
		border : 1px solid #98CDDA;
		min-height : 500px;
		border-radius:5px;
		width : 96%;
		margin-left:2%;	
		background-color:#EAF2F5;	
	}
</style>

<div id="validateRawReceiversContainer" style='font-size:12px;'>

<div 
					style="height:300px;"
					data-role="grid"
					id="validateRawReceiverGrid"
             				data-scrollable="true"
             				data-height="280"
              				data-editable="false"
              				data-pageable="{
              						refresh:true
              					}"
              				data-columns="[
              						{ 'field': 'receiverContact', title : 'Contact','width':'150px'  },
                              		{ 'field': 'receiverType', title : 'Type'  },
                              		{ 'field': 'firstName', title : 'First Name' },
                              		{ 'field': 'lastName', title : 'Last Name'  },
                              		{ 'field': 'countryName', title : 'Country'  },
                              		{ 'field': 'countyName', title : 'County' },
                              		{ 'field': 'consName', title : 'Constituency'  },
                              		{ 'field': 'consWardName', title : 'Ward' },
                              		{ 'field': 'village', title : 'Village'  },
                              		{ 'field': 'verified', title : 'Verified'  },
                              		{ 'field': 'verifiedBy', title : 'Verified By' },
                              		{ 'field': 'createdBy', title : 'Created By'  },
                              		{ 'field': 'lastUpdate', title : 'Last Update' },
                              		{ 'field': 'cretDate', title : 'Created On'  },
                              		{ 'field': 'lastUpdatedBy', title : 'Updated By'  }
                           		]"
              				data-bind="source: rawReceiverDS"
                         			
                         			
				>
				</div>


</div>




















</body>
</html>