<%@include file="../common/HeaderFile.jsp"%>

<script src="<c:url value='/static/resources/scripts/plain/kendoHelper.js'/>"></script>
<script src="<c:url value='/static/resources/scripts/model/hunterAdminClientHuntler.js'/>"></script>
<%-- <script src="<c:url value='/static/resources/scripts/model/regionVM.js'/>"></script> --%>

<div id="hunterUserGrid" style="width:98%;margin-left:1%;border : 1px solid : #E2EFF2;border-radius:4px;margin-top:0%;margin-bottom:0.5%;" >

</div>

<div id="expandGridButtonDiv" class="hidden" style="margin-bottom:2px;margin-left:1%;width:20%;" class="k-header"  >
	<table style="width:65%;">
		<tr>
			<td><span id="selectedUserNameSpan" style="background-color:#E1F1FD;border:1px solid #BCD6EB;padding:7px;border-radius:4px;" >Kip Langat</span></td>
			<td><button onClick="hunterAdminClientUserVM.expandHunterUserGrid()" style="float:left;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" class="k-button" >Show Grid</button></td>
		</tr>
	</table>
</div>

	<div id="hunterUserDetailsStrip" style="width:98%;margin-left:1%;margin-top:0%;">
		<ul style="margin-left:40%">
			<li class="k-state-active"  >Task Management</li>
		</ul>
		<div style="border:1px solid white;" id="taskGridHolder" >
      		
      		<div id="selectedUserClientTasks" style="width:100%;height:400px; border : 1px solid #9CD0E6; border-radius:4px;background=color:#FAFEFF;" >
      			<h3 id="noDataH3ForHunterClientDetails"  style="color:#620B01;padding-left:2%">To load user, please select user and click 'Load' button.</h3>
      		</div>
      		
      		
         </div>
         
	</div>
         
         
           <div id="taskMessageStrip" style="width:98%;margin-left:1%;height:400px; border : 1px solid #9CD0E6; border-radius:4px;background:color:#FAFEFF;display:none;padding-bottom:10px;padding-top:10px;">
           		<div id="selTaskMessageEditMode" class="k-block k-info-colored" style="width:98%; height:90%;margin-left:1%;border-radius:5px;border:1px solid #CFECEE;" >
           			<table style="float:left;width:40%;margin-right:3%;color:#0A2C2E;font-size:13px;border-right:1px solid #D4E1F2;padding-left:10px;" >
           				<tr>
           					<td style="with:40%;" >Send Date</td>
           					<td style="width:60%;">
           						<input 	data-role="datepicker"
           							   	id="selTaskMsgSendDatePicker"
           								name ="selTaskMsgSendDatePicker"
			                   	   		data-bind="visible: isEverVisible,
			                              enabled: isEverVisible,
			                              value: taskMsgSendDate,
			                              events: { change: onChangeTaskMsgSendDate }" 
			                              style="width: 180px"/>
           					</td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Message Task Type</td>
           					<td style="width:60%;">
           						<input 
           							type="text" 
           							id="msgMsgType" 
									name="msgMsgType" 
					                data-bind="value:taskMsgTypeVal"
					                data-value-field="value"
					                data-text-field="text"
					                data-source="HunterConstants.TASK_TYPES_ARRAY"
					                data-role="dropdownlist" 
					                style="width: 180px"/>
           					</td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Desired Receivers</td>
           					<td style="width:60%;">
           						 <input 
           						 		name="msgDesiredReceivers"
           						 		name="msgDesireddReceivers" 
           						 		data-role="numerictextbox"
					                    data-min="0"
					                    data-max="1000000"
					                    data-bind="visible: isEverVisible,
					                              enabled: isEverEnabled,
					                              value: desiredReceivers,
					                              events: { change: onChange }"
					                   style="width: 180px">
           					</td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Actual Receivers</td>
           					<td style="width:60%;">
           						 <input 
           						 		name="msgActualReceivers"
           						 		name="msgActualdReceivers" 
           						 		data-role="numerictextbox"
					                    data-min="0"
					                    data-max="1000000"
					                    data-bind="visible: isEverVisible,
					                              enabled: isEverEnabled,
					                              value: actualReceivers,
					                              events: { change: onChange }"
					                   style="width: 180px">
           					</td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Confirmed Receivers</td>
           					<td style="width:60%;">
           						 <input 
           						 		name="msgConfirmedReceivers"
           						 		name="msgConfirmedReceivers" 
           						 		data-role="numerictextbox"
					                    data-min="0"
					                    data-max="1000000"
					                    data-bind="visible: isEverVisible,
					                              enabled: isEverEnabled,
					                              value: confirmedReceivers,
					                              events: { change: onChange }"
					                   style="width: 180px">
           					</td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Service Provider</td>
           					<td style="width:60%;">
           						<input 
           							type="text" 
           							id="taskMessageProvider" 
									name="taskMessageProvider" 
					                data-bind="value:tskMsgProvider"
					                data-value-field="providerId"
					                data-text-field="providerName"
					                data-source="HunterConstants.SERVICE_PROVIDER_DATA"
					                data-role="dropdownlist" 
					                style="width: 180px"/>
           					</td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Message Owner</td>
           					<td style="width:60%;"><input type="text" value="Kip Langat" class="k-input k-textbox" style="width: 180px" /></td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Created On</td>
           					<td style="width:60%;"><input type="text" class="k-input k-textbox" data-bind="text : tskMsgLstUpdate"  style="width: 180px" /></td>
           				</tr>
           			</table>
           			<table style="float:left;margin-left:10px;color:#0A2C2E;font-size:13px;padding-left:10px;"  >
           				<tr>
           					<td style="with:40%;" >Created By</td>
           					<td style="width:60%;"><input type="text" value="Kip Langat" class="k-input k-textbox" style="width: 180px" /></td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Modified On</td>
           					<td style="width:60%;"><input type="text" value="05/12/2015" class="k-input k-textbox" style="width: 180px" /></td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Modified By</td>
           					<td style="width:60%;"><input type="text" value="Kip Langat" class="k-input k-textbox" style="width: 180px" /></td>
           				</tr>
           				<tr>
           					<td style="with:40%;" >Message Text</td>
           					<td style="width:60%;"><textarea style="background-color:#F2F9FF;border:1px solid #81A6AE;border-radius:4px;resize: none;" rows="5" cols="20" id="taskInfoTaskObjective" ></textarea></td>
           				</tr>
           			</table>
           		</div>
           			<table style="width:40%;margin-left:30%;margin-top:10px;margin-bottom:10px;" >
           				<tr>
           					<td style="width:100%;">
             				<button onClick='hunterAdminClientUserVM.saveCurrentEditMessageAndClose()' style="width:130px;margin-left:15%;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" class="k-button" >Save And Close</button>
             				<button onClick='hunterAdminClientUserVM.saveCurrentEditMessage()' style="width:100px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" class="k-button" >Save</button>
             				<button onClick='hunterAdminClientUserVM.canceltEditMessageAndClose()' style="width:100px;background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" class="k-button" >Cancel</button>
           					</td>
           				</tr>
           			</table>
      		</div>
             
             
             
             <div id="taskRegionStrip" style="width:90%;margin-left:5%;margin-top:20px;height:400px; background-color:#FAFEFF;display:none;" >
				<div id="taskRegionViewCover" style="width:22%;height:300px;float:left;border:1px solid #C7EAFF;padding:25px;border-radius:4px;-webkit-box-shadow: 0 0 5px 2px #E2F3FF; -moz-box-shadow: 0 0 5px 2px #E2F3FF; box-shadow: 0 0 5px 2px #E2F3FF;" >

					<table style="width:80%;">
						<tr>
							<td>Country :</td>
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
					                              visible: isCountryDropdownVisible,
					                              enabled: isCountryDropdownEnabled,
					                              events: {
					                                change: onChangeCountry
					                              }"
					            />
							</td>
						</tr>
						<tr>
							<td>County :</td>
							<td> 
							
								<input id="countyDropDownList"
								 	   data-role="dropdownlist"
					                   data-auto-bind="true"
					                   data-value-primitive="true"
					                   data-text-field="countyName"
					                   data-value-field="countyId"
					                   data-optionlable = "Select County"
					                   data-bind="value: selCounty,
					                              source: countyDS,
					                              visible: isCountyDropdownVisible,
					                              enabled: isCountyDropdownEnabled,
					                              events: {
					                                change: onChangeCounty
					                              }"
					            />
							</td>
						</tr>
						<tr>
							<td>Constituency :</td>
							<td>
								<input id="constituencyDropDownList"
								 	   data-role="dropdownlist"
					                   data-auto-bind="true"
					                   data-value-primitive="true"
					                   data-text-field="constituencyName"
					                   data-value-field="constituencyId"
					                   data-optionlable = "Select Consituency"
					                   data-bind="value: selConstituency,
					                              source: constituencyDS,
					                              visible: isConstituencyDropdownEnabled,
					                              enabled: isConstituencyDropdownVisible,
					                              events: {
					                                change: onChangeConstituency
					                              }"
					            />
							</td>
							
							
						</tr>
						<tr>
							<td>ConstituencyWard :</td>
							<td>
								<input id="constituencyWardDropDownList"
								 	   data-role="dropdownlist"
					                   data-auto-bind="true"
					                   data-value-primitive="true"
					                   data-text-field="constituencyWardName"
					                   data-value-field="constituencyWardId"
					                   data-optionLabel = "Select Ward"
					                   data-bind="value: selConstituencyWard,
					                              source: constituencyWardDS,
					                              visible: isConstituencyWardDropdownEnabled,
					                              enabled: isConstituencyWardDropdownVisible,
					                              events: {
					                                change: onChangeConsituencyWard,
					                                dataBound:onDataConsWardBound
					                              }"
					            />
							</td>
						
						
						
						
						</tr>
					</table> 
					<span style="margin:10px;" ></span>
					<table style="margin:0 auto;">
						<tr>
							<!-- <td><button onClick="hunterAdminClientUserVM.saveCurrentEditRegionAndClose()"  class="k-button" style="background-color:rgb(212,239,249);width:100%;border : 1px solid rgb(120,186,210);" >Save And Close</button></td> -->
							<td><button onClick="hunterAdminClientUserVM.saveCurrentEditRegion()"  class="k-button" style="background-color:rgb(212,239,249);width:100%;border : 1px solid rgb(120,186,210);" >Add Region</button></td>
					    	<td><button onClick="hunterAdminClientUserVM.canceltEditRegionAndClose()"  class="k-button" style="background-color:rgb(212,239,249);width:100%;border : 1px solid rgb(120,186,210);" >Go Back</button></td>
						</tr>
					</table>
					
					</div>
					
					<div id="taskRegionViewCover2" style="width:70%;height:300px;float:right;border:1px solid #C7EAFF;padding:25px;border-radius:4px;-webkit-box-shadow: 0 0 5px 2px #E2F3FF; -moz-box-shadow: 0 0 5px 2px #E2F3FF; box-shadow: 0 0 5px 2px #E2F3FF;" >
						<div 
							data-role="grid"
							data-height="280"
							id="selTaskRegionsGrid"
               				data-scrollable="true"
                				data-editable="false"
                				data-pageable="{
                						refresh:true
                					}"
                				data-columns="[
                						{ 'field': 'regionId', title : 'Region ID','width':'90px'  },
                                		{ 'field': 'country', title : 'Country'  },
                                		{ 'field': 'county', title : 'County' },
                                		{ 'field': 'constituency', title : 'Constituency'  },
                                		{ 'field': 'ward', title : 'Ward'  },
                                		{ 'field': 'receiverCount', title : 'Receivers','width':'90px'},
                                		{'field' : 'Delete', 'title':'Delete','width':'75px','template' : '#=getRegionsDeleteTemplate()#'}
                             		]"
                				data-bind="source: selTaskRegionsDS,
                           			visible: isEverVisible,
                           			events: { save: onSaveSelTaskRegion}"
                           			
                           			
						>
						</div>
						<div 
							class="totals" 
							style=" margin-top:2px; font-size:15px;color:#004ADF; 
							font-weight:bold; width:30%;
							float:right; text-align: right;
							padding:4px; border-radius:4px;" >	
							<span>Total Receivers: </span><span id="taskUniqueReceiversSpan" data-bind="text: taskUniqueReceivers" style="margin-right:43%;color:red;" ></span></div>
					</div>
      		</div>




<style type="text/css">
.k-grid .k-grid-content {
 min-height: 100px;
 max-height: 280px;
}
	 .sunny, .cloudy, .rainy {
                    display: block;
                    margin: 30px auto 10px;
                    width: 128px;
                    height: 400px;
                }

                .cloudy{
                    background-position: -128px 0;
                }

                .rainy{
                    background-position: -256px 0;
                }

                .weather {
                    margin: 0 auto 30px;
                    text-align: center;
                }

                #tabstrip h2 {
                    font-weight: lighter;
                    font-size: 5em;
                    line-height: 1;
                    padding: 0 0 0 30px;
                    margin: 0;
                }

                #tabstrip h2 span {
                    background: none;
                    padding-left: 5px;
                    font-size: .3em;
                    vertical-align: top;
                }

                #tabstrip p {
                    margin: 0;
                    padding: 0;
                }
                
                #hunterUserClientDetailsTable td{
                	border-bottom:1px solid #E8F8FA;
                }
                
				#hunterUserClientTaskDetailsTable td { 
					border-bottom:1px solid #E8F8FA; 
				}
				#hunterUserClientTaskRegionTable td { 
					border-bottom:1px solid #E8F8FA; 
				}
				#hunterUserClientTaskMessageDetailsTable td { 
					border-bottom:1px solid #E8F8FA; 
				}
                
</style>



















	<!--  :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::      Below Here are all templates         :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: -->




















<script type="text/x-kendo-template" id="hunterUserToolBar">
<div class="toolbar" >
	<button data-command="create" style="background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" class="k-button k-button-icontext k-grid-add"><span class="k-icon k-add"></span>New</button>
	<button id="loadHunterUserDetails" style="background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" class="item-group k-button" style="width:150px" onClick="hunterAdminClientUserVM.loadSelUserDetails()"><span class='k-icon k-i-search' ></span>Load</button>
</div>
</script>

<script type="text/x-kendo-template" id="hunterClientTasksToolBar">
<div class="toolbar" >
	<button data-command="create" style="background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" class="k-button k-button-icontext k-grid-add"><span class="k-icon k-add"></span>New Task</button>
</div>
</script>

<script type="text/javascript">
	$("document").ready(function(){
		hunterAdminClientUserVM.init();
	});
</script>


<div id="hunterUserEditTemplate" style="display:none;">
	<div class="k-edit-form-container">
		<div class="k-edit-label">
			<label for="firstName">First Name</label>
		</div>
		<div data-container-for="firstName" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="firstName"
				required="required" data-bind="value:firstName">
		</div>
		<div class="k-edit-label">
			<label for="lastName">Last Name</label>
		</div>
		<div data-container-for="lastName" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="lastName"
				required="required" data-bind="value:lastName">
		</div>
		<div class="k-edit-label">
			<label for="middleName">Middle Name</label>
		</div>
		<div data-container-for="middleName" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="middleName"
				required="required" data-bind="value:middleName">
		</div>
		<div class="k-edit-label">
			<label for="email">Email</label>
		</div>
		<div data-container-for="email" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="email"
				required="required" data-bind="value:email">
		</div>
		<div class="k-edit-label">
			<label for="phoneNumber">Phone Number</label>
		</div>
		<div data-container-for="phoneNumber" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="phoneNumber"
				required="required" data-bind="value:phoneNumber">
		</div>
		<div class="k-edit-label">
			<label for="userType">User Type</label>
		</div>
		<div data-container-for="userType" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="userType"
				required="required" data-bind="value:userType">
		</div>
		<div class="k-edit-label">
			<label for="userName">User Name</label>
		</div>
		<div data-container-for="userName" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="userName"
				required="required" data-bind="value:userName">
		</div>
		<div class="k-edit-label">
			<label for="userName">Password</label>
		</div>
		<div data-container-for="password" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="password"
				required="required" data-bind="value:password">
		</div>
	</div>
</div>



<div id="hunterClientTasksPopupEditTemplate" style="display:none;">
	<div class="k-edit-form-container">
		<div class="k-edit-label">
			<label for="taskName">Task Name</label>
		</div>
		<div data-container-for="taskName" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="taskName"
				required="required" data-bind="value:taskName">
		</div>
		<div class="k-edit-label">
			<label for="description">Task Description</label>
		</div>
		<div data-container-for="description" class="k-edit-field">
			<input type="text" class="k-input k-textbox" name="description"
				required="required" data-bind="value:description">
		</div>
		<div class="k-edit-label">
			<label for="taskType">Task Type</label>
		</div>
		<div data-container-for="taskType" class="k-edit-field">
			<input type="text" id="taskType" 
				name="taskType" 
				id="taskTypeInput" 
                data-bind="value:taskType"
                data-value-field="value"
                data-text-field="text"
                data-source="HunterConstants.TASK_TYPES_ARRAY"
                data-role="dropdownlist" />
		</div>
		<div class="k-edit-label">
			<label for="taskType">Gateway Client</label>
		</div>
		<div data-container-for="gateWayClient" class="k-edit-field">
			<input type="text" id="gateWayClient" 
				name="gateWayClient" 
				id="gateWayClient" 
                data-bind="value:gateWayClient"
                data-value-field="value"
                data-text-field="text"
                data-source="HunterConstants.GATE_WAY_CLIENT_ARRAY"
                data-role="dropdownlist" />
		</div>
		<div class="k-edit-label">
			<label for="taskObjective">Task Objective</label>
		</div>
		<div data-container-for="taskObjective" class="k-edit-field">
			<textarea data-bind="value:taskObjective" cols="27" rows="5" style="background-color:rgb(242,249,255);border:1px solid rgb(129,166,174);" name="taskObjective" class="k-input k-textarea" ></textarea> 
		</div>
		<div class="k-edit-label">
			<label for="recurrentTask">Recurrent Task</label>
		</div>
		<div data-container-for="recurrentTask" class="k-edit-field">
			<input 
				type="checkbox" 
				class="k-input" 
				name="recurrentTask"
				data-bind="value:recurrentTask">
		</div>
		<div class="k-edit-label">
			<label for="taskBudget">Task Budget</label>
		</div>
		<div data-container-for="taskBudget" class="k-edit-field">
			<input name="taskBudget" id="taskTaskBudgetNumericInput"
				required="required" data-bind="value:taskBudget">
		</div>
		<div class="k-edit-label">
			<label for="taskCost">Task Cost</label>
		</div>
		<div data-container-for="taskCost" class="k-edit-field">
			<input  name="taskCost" id="taskTaskCostNumericInput"
				required="required" data-bind="value:taskCost">
		</div>
		<div class="k-edit-label">
			<label for="desiredReceiverCount">Desired Receivers</label>
		</div>
		<div data-container-for="desiredReceiverCount" class="k-edit-field">
			<input name="desiredReceiverCount" id="desiredReceiverCountInput"
				required="required" data-bind="value:desiredReceiverCount">
		</div>
	</div>
</div>

<div id="hunterClientTasksPopupViewTemplate" style="display:none;">
	<div class="k-header" style="border:1px solid rgb(187,207,230); border-radius:4px;padding:10px;"> 
        <table id="hunterClientTasksPopupViewTemplateTable" style="width:94%;margin-left:3%;border : 1px solid rgb(218,239,242); border-radius:4px;">
        	<tr>
        		<td>Task ID</td>
        		<td>#= taskId #</td>
        	</tr>
        	<tr>
        		<td>Task Type:</td>
        		<td>#= taskType #</td>
        	</tr>
        	<tr>
        		<td>Task Name:</td>
        		<td>#= taskName #</td>
        	</tr>
        	<tr>
        		<td>Task Objective:</td>
        		<td>#= taskObjective #</td>
        	</tr>
        	<tr>
        		<td>Task Description:</td>
        		<td>#= description #</td>
        	</tr>
        	<tr>
        		<td>Task Budget:</td>
        		<td>#= taskBudget #</td>
        	</tr>
        	<tr>
        		<td>Task Cost:</td>
        		<td>#= taskCost #</td>
        	</tr>
        	<tr>
        		<td>Task Recurrent :</td>
        		<td>#= recurrentTask #</td>
        	</tr>
        	<tr>
        		<td>Task Date Line:</td>
        		<td>#= taskDateline #</td>
        	</tr>
        	<tr>
        		<td>Task Life Status:</td>
        		<td>#= taskLifeStatus #</td>
        	</tr>
        	<tr>
        		<td>Task Delivery Status:</td>
        		<td>#= taskDeliveryStatus #</td>
        	</tr>
        	<tr>
        		<td >Task Approved:</td>
        		<td>#= taskApproved #</td>
        	</tr>
        	<tr>
        		<td >Task Approver:</td>
        		<td>#= taskApprover #</td>
        	</tr>
        	<tr>
        		<td>Created On:</td>
        		<td>#= cretDate #</td>
        	</tr>
        	<tr>
        		<td>Created By:</td>
        		<td>#= createdBy #</td>
        	</tr>
        	<tr>
        		<td >Modified On:</td>
        		<td>#= lastUpdate #</td>
        	</tr>
        	<tr>
        		<td >Modified By:</td>
        		<td>#= lastUpdatedBy #</td>
        	</tr>
        	<tr>
        		<td >Task Message:</td>
        		<td>
        			#if(data.taskMessage != null){#
        			   #= taskMessage.text #
        			#}else{#
        				No Message!
        			#}#
        			
        		</td>
        	</tr>
        	<tr>
        		<td class="bold" >Task Region:</td>
        		<td>
        			#if(data.taskRegion != null){#
        			   #= taskRegion.country #
        			#}else{#
        				No Region!
        			#}#
        		</td>
        	</tr>
        </table>
        <div style="text-align: center;margin-top:20px;"><button class="k-button" onClick="kendoKipHelperInstance.closeMeasuredWindow()" style='background-color:rgb(212,239,249);width:15%;'>OK</button></div>
        </div>
    </div>
    
    <div id="clientDetailsTemplateDiv"  style="display:none;"> 
    	<div id="clientDetailsContainer"  style="border:1px solid rgb(187,207,230); border-radius:4px;padding:10px;" class="k-header">
    			<table style="width:100%;">
    				<tr>
    					<td>Client ID: </td>
    					<td><span id="hunterClientCreatedOnTd" >#=clientId#</span></td>
    				</tr>
    				<tr>
    					<td>Total Budget: </td>
    					<td style="height:20px;" ><span>Ksh </span><span style="height:20px;" >#=clientTotalBudget#</span></td>
    				</tr>
    				<tr>
    					<td>Is Receiver</td>
    					<td>#=receiver#</td>
    				</tr>
    				<tr>
    					<td>Created On</td>
    					<td><span id="hunterClientCreatedOnTd" >#=cretDate#</span></td>
    				</tr>
    				<tr>
    					<td>Created By</td>
    					<td id="createdBy" >#=createdBy#</td>
    				</tr>
    				<tr>
    					<td>Last Update</td>
    					<td id="updatedOn" >#=lastUpdate#</td>
    				</tr>
    				<tr>
    					<td>Updated By</td>
    					<td id="updatedBy" >#=lastUpdatedBy#</td>
    				</tr>
    			</table>
    			<table style="width:30%;margin-left:35%;">
    				<tr>
    					<td><button onClick="hunterAdminClientUserVM.saveDraftClientChanges()"  class="k-button" style="background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" >Update</button></td>
    					<td><button onClick="kendoKipHelperInstance.closeMeasuredWindowBackUp()"  class="k-button" style="background-color:rgb(212,239,249);border : 1px solid rgb(120,186,210);" >Cancel</button></td>
    				</tr>
    			</table>
    	</div>
    </div>





























</body>
</html>
