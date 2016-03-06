<%@include file="../common/HeaderFile.jsp"%>

<script src="<c:url value='/static/resources/scripts/plain/kendoHelper.js'/>"></script>
<script src="<c:url value='http://localhost:8080/Hunter/static/resources/scripts/model/regionHierarchy.js'/>"></script>

<div style="width:80%;margin-left:10%;" id="regionHierarchyTreeListVMContainer"  >

<div id="countriesDropdownList" >
	<table style="width:10%;">
		<tr>
			<td>
				 <input id="countryDropDownList"
				 	   data-role="dropdownlist"
	                   data-auto-bind="true"
	                   data-value-primitive="true"
	                   data-text-field="countryName"
	                   data-value-field="countryId"
	                   placeholder = "Select Country"
	                   data-bind="value: selCountry,
	                              source: countryDS,
	                              events: {
	                                change: onChangeCountry,
	                              }"
	            />
			</td>
		</tr>
	</table>
</div>

<div id="regionHierarchyTreeList"  >
	
</div>

<div id="uploadReceiversContainer" style="margin-top:3%;display:none; with:200px; margin:0 auto;" >
<input name="files"
    type="file"
    placeholder = "Select File"
    data-role="upload"
    data-multiple="false"
    data-async="{ saveUrl: 'http://localhost:8080/Hunter/messageReceiver/action/import/receivers/post/save', 
    			  removeUrl: 'http://localhost:8080/Hunter/messageReceiver/action/import/receivers/post/save', 
    			  autoUpload: true 
    			}"
    data-bind="visible: isUploadReceiversVisible,
               enabled: isUploadReceiversEnabled,
               events: { select: onSelect,
               			 complete : onComplete,
               			 error: onError
               			}"
  >
<p style="text-align: left;margin-top:10px;" >
	<button style="background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" onClick="RegionHierarchyVM.closeUploadDivAndShowGrid()" class="k-button k-button-icontext k-grid-add"><span class="k-icon k-i-arrowhead-w"></span>Go Back</button>
</p>
</div>

<script type="text/javascript">
	$("document").ready(function(){
		$("#uploadReceiversContainer").css("width", "400px");
		$("#uploadReceiversContainer").css("float", "left");
	});
</script>

</div>

	

</body>

<script type="text/x-kendo-template" id="regionHierarchyToolBarTemplate">
<div class="toolbar" >
	<button style="background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" onClick="RegionHierarchyVM.openSectionToUploadReceivers()" class="k-button k-button-icontext k-grid-add"><span class="k-icon k-i-seek-n"></span>Receivers</button>
</div>
</script>

<script type="text/x-kendo-template" id="regionHierarchyToolBarTemplate">
<input id="countryDropDownList"
				 	   data-role="dropdownlist"
	                   data-auto-bind="true"
	                   data-value-primitive="true"
	                   data-text-field="countryName"
	                   data-value-field="countryId"
	                   placeholder = "Select Country"
	                   data-bind="value: selCountry,
	                              source: countryDS,
	                              events: {
	                                change: onChangeCountry,
	                              }"
</script>

<script type="text/javascript">
	$("document").ready(function(){
		RegionHierarchyVM.init();
	});	
</script>

</html>