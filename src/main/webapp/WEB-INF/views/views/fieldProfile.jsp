<%@include file="../common/JSTL_tags.jsp"%>
<!DOCTYPE html>
<html style="font-size:13px;" >
<head>


<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="<c:url value='/static/resources/scripts/plain/fieldProfile.js'/>"></script>
<script src="<c:url value='/static/resources/scripts/plain/notify.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/static/resources/css/fieldProfile.css'/>" >
<script src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css" />
 
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

<script src="<c:url value='/static/resources/scripts/plain/jqueryForm.js'/>"></script>
<script src="<c:url value='/static/resources/scripts/plain/jQueryValidate.js'/>"></script>

<title>Hunter Officer Profile</title>
</head>

<div id="fieldProfileContainer" style="margin-top:1%;"  > 

<div style="height:20%;width:100%;text-align: center;" >
<span style="margin-right:-1050px;margin-top:-10%;" ><a style="color:white;" id="logoutId" href="//hunter/login/logout" >Logout</a></span>
<table style="table-layout: fixed;width:98%;margin-left:1%;" >
	<tr>
		<td  style="width:34%;">
			<div class="profileDetDivs" >
				<table style="font-size:16px;" class="profileDetTables"  >
					<tr>
						<td>Total Contacts  </td>
						<td id="fieldUserTotalContacts" ></td>
					</tr>
					<tr>
						<td>Verified Contacts  </td>
						<td id="fieldUserVerifiedContacts" ></td>
					</tr>
					<tr>
						<td>Total Payout  </td>
						<td>Ksh <span id="fieldUserTotalPayout" ></span></td>
					</tr>
					<tr>
						<td>Available Payout  </td>
						<td> Ksh <span id="fieldUserAvailablePayout" ></span></td>
					</tr>
					<tr>
						<td>Email </td>
						<td  id="fieldUserEmail"  ></td>
					</tr>
				</table>
			</div>
		</td>
		<td  style="width:28%;" >
			<div class="profileDetDivs" style="padding-bottom:10px;" >
				<table style="table-layout: fixed; margin: 0 auto;" >
					<tr>
						<td>
							<img 
								id="fielProfilePic" class="fielProfilePicClass" 
								src="https://ikonmania.files.wordpress.com/2014/03/profile-pictures209.jpg" 
								alt="Profile Pic" 
								width="100" 
								height="100"
								style="margin:0 auto;"  
							>
						</td>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<td>
										<span href="#" style="font-size: 18px;text-shadow: 2px 2px #009AB2;" id="fieldUserFullName">Kip Langat</span>
									</td>
									<td><center><img width="30px" onclick="populatePopupForParams('0_EditUserProfile')" height="30px" style="border-radis:50%;cursor:pointer" src="http://cdn.mysitemyway.com/etc-mysitemyway/icons/legacy-previews/icons/black-white-pearls-icons-business/078108-black-white-pearl-icon-business-pencil7-sc49.png"  /></center></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</td>
		<td style="width:34%;">
			<div class="profileDetDivs" style="width:100%;"  >
				<table style="font-size:16px;height: 100%;" class="profileDetTables"  >
					<tr>
						<td>Phone </td>
						<td  id="fieldUserPhone"  ></td>
					</tr>
					<tr>
						<td>Country </td>
						<td  id="fieldUserCountry"  ></td>
					</tr>
					<tr>
						<td>County </td>
						<td  id="fieldUserCounty"  ></td>
					</tr>
					<tr>
						<td>Constituency </td>
						<td  id="fieldUserConstituency"  ></td>
					</tr>
					<tr>
						<td>Ward </td>
						<td  id="fieldUserWard"  ></td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
</table>
</div>


<div id="fieldProfileRawReceiversDiv" style="position:absolute;max-height: 80%;width:98%;">

<table id="fieldProfileDataTable" class="display" style="color:black;border-radius:4px;width:96%;" >
	<thead>
            <tr>
                <th>ID</th>
                <th>Contact</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Country</th>
                <th>County</th>
                <th>Constituency</th>
                <th>Ward</th>
                <th>Verfied</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
        </thead>
</table>


</div>





</div>
















<div id="hunterFieldTemplatesContainer" style="display:none;" >

	<div id="hunterFieldPopupButts" >
		<table style="width:20%;margin-left:35%;" >
			<tr>
				<td id="affirmPopupAction" ></td>
				<td><center><a href="#" onClick="closeHunterPopup1()" class="ui-btn ui-icon-forbidden ui-btn-icon-left" style="border:1px solid #216365;border-radius:5px;color:#0E4244;" >Close</a></center></td>
			</tr>
		</table>
	</div>
	<div id="hunterFieldEditTemplateContainer" >
		<table style="width:80%;margin-top:30px;margin-bottom:30px;margin-left:10%;table-layout: fixed;font-size: 13px;color:#11484E;font-weight: bold;" >
			<tr data-tr='contactType' >
				<td>Contact Type</td>
				<td>
					<select data-name="contactTypeInput"  class="hunterInputText" style="width:90%;" >
					  <option value="text">Phone Number</option>
					  <option value="email">Email Address</option>
					</select>
				</td>
			</tr>
			<tr data-tr='contact' >
				<td>Contact*</td>
				<td><input data-name="contactInput" class='hunterInputText' placeholder="Enter Contact"  type="text"  /> </td> 
			</tr>
			<tr data-tr='firstName' >
				<td>First Name*</td>
				<td><input data-name="firstName" class='hunterInputText' placeholder="Enter First Name"  type="text"  /> </td>
			</tr>
			<tr data-tr='lastName' >
				<td>Last Name</td>
				<td><input data-name="lastName" class='hunterInputText' placeholder="Enter Last Name"  type="text"  /> </td>
			</tr>
			<tr>
				<td>Country*</td>
				<td>
					<select data-name="countryInput" class="hunterInputText" style="width:90%;" >
					  <option value="1">Kenya</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>County*</td>
				<td>
					<select data-name="countyInput" class="hunterInputText" style="width:90%;" >
					</select>
				</td>
			</tr>
			<tr>
				<td>Constituency*</td>
				<td>
					<select data-name="consInput" class="hunterInputText" style="width:90%;" >
					</select>
				</td>
			</tr>
			<tr>
				<td>Ward*</td>
				<td>
					<select data-name="wardInput" class="hunterInputText" style="width:90%;" >
					</select>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="notificationContainer" >
		<div id="hunterFieldNotification" >
			
		</div>
	</div>
	
	<div id="importNewContainerTmplt" >
		<div id="importNewContainer"  style="width:80%;margin-left:10%;" >
			<form id="importNewContactForm" action="${pageContext.request.contextPath}/rawReceiver/action/rawReceiver/import/rawReceiver" method="POST" style="width:98%;margin-left:1%;"  enctype="multipart/form-data"  >
				<h3 style="width:80%;margin-left:10%;" >Please upload your file below.</h3>
				<h5 style="width:80%;margin-left:10%;color:brown;margin-bottom:30px;" >Note : Only .xlsx and .xls files are allowed.</h5> 
				<input style="width:80%;margin-left:10%;margin-bottom:30px;border:1px solid #84BDC6;padding:15px;border-radius:5px;" onChange="updateFileSelected" id="importNewContactFile"  name="importNewContactFile" type="file" >
				<table style="table-layout: fixed;width:100%;margin-bottom:30px;" >
					<tr>
						<td><button id="importNewContainerSubButt" type="submit" class="ui-btn ui-icon-carat-u ui-btn-icon-left" style="width:90%;margin-left:5%;border-radius:5px;color:#0089FF;background-color: #9BF3FF;border:1px solid #93CADB;" >Submit</button></td>
						<td><a  id="importNewContainerCloseButt" style="width:90%;margin-left:5%;border-radius:5px;background-color: #9BF3FF;border:1px solid #93CADB;color:#0089FF;" href="#" onClick="closeHunterPopup1()" class="ui-btn ui-icon-forbidden ui-btn-icon-left">Close</a></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
</div>

<div class="popup" data-popup="popup-1">
    <div class="popup-inner">
        <a id="popup-close-button" class="popup-close" data-popup-close="popup-1" href="#">x</a>
    </div>
</div>

<div id="hunterEditUserProfileCont" style="display:none;" > 
	<div id="hunterEditUserProfileDiv"  style="width:60%;margin-left:20%;" >
		<table style="width:100%;margin-top:30px;margin-bottom:30px;table-layout: fixed;font-size: 16px;color:#11484E;font-weight: bold;" >
			<tr>
				<td style="text-align: center;">
					<img 
						id="fielProfilePic_" class="fielProfilePicClass"
						src="https://ikonmania.files.wordpress.com/2014/03/profile-pictures209.jpg" 
						alt="Profile Pic" 
						width="100" 
						height="100"
						style="margin:0 auto;"  
					>
				</td>
			</tr>
			<tr>
				<td  style="text-align: center;" >
					<form action="${rpageContext.request.contextPath}" id="editProfilePhotoForm" method="POST" style="margin-top:20px;width:98%;margin-left:1%;" >
  						<table style="width:100%;">
  							<tr><td><input id="editProfilePhotoInput" name="editProfilePhotoInput" type="file" onChange="readURL(this)" style="width:98%;border:1px solid #1DB6C7;border-radius:3px;" ></td></tr>
  							<tr><td data-name="filePhotoErrMsg" style="color:#C10000;font-size:13px;"></td></tr>
  						</table>
					</form>
				</td>
			</tr>
		</table>
	</div>
</div>


<div id="hunterMessageTemplateContainer" >
	<div id="hunterMessageTemplate" >
		
	</div>
</div>



</body>
</html>