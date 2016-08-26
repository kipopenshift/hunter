<%@include file="JSTL_tags.jsp"%>
<!DOCTYPE html>
<html style="font-size:13px;" >
<head>

<meta charset="utf-8">

<link rel="stylesheet" href="http://cdn.kendostatic.com/2015.3.1111/styles/kendo.common.min.css">
<link rel="stylesheet" href="http://cdn.kendostatic.com/2015.3.1111/styles/kendo.default.min.css">
<link rel="stylesheet" href="http://cdn.kendostatic.com/2015.3.1111/styles/kendo.dataviz.min.css">
<link rel="stylesheet" href="http://cdn.kendostatic.com/2015.3.1111/styles/kendo.dataviz.default.min.css">

<link rel="stylesheet" href="http://localhost:8080/Hunter/static/resources/kendo/kendo.custom.css">

<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://cdn.kendostatic.com/2015.3.1111/js/kendo.all.min.js"></script>


<%-- <script src="<c:url value='/static/resources/scripts/fancyScript.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/static/resources/css/fancyCss.css'/>">
 --%>
 <script src="<c:url value='/static/resources/scripts/plain/kendoHelper.js'/>"></script>
 <script src="<c:url value='/static/resources/scripts/plain/hunter.js'/>"></script>
 <script src="<c:url value='/static/resources/scripts/plain/hunterConstants.js'/>"></script>
 <link rel="stylesheet" href="<c:url value='/static/resources/css/hunter.css'/>">
 <link rel="stylesheet" href="<c:url value='/static/resources/css/kendoGen.css'/>">


<!-- This is for jQuery icons -->
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">

<!-- This is for Code Mirror -->
<script src="<c:url value='/static/resources/scripts/codeMirror/lib/codemirror.js'/>"></script>
<script src="<c:url value='/static/resources/scripts/codeMirror/mode/javascript/javascript.js'/>"></script>
<script src="<c:url value='/static/resources/scripts/codeMirror/mode/xml/xml.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/static/resources/scripts/codeMirror/lib/codemirror.css'/>">
<script src="<c:url value='/static/resources/scripts/codeMirror/mode/htmlmixed/htmlmixed.js'/>"></script>
<script src="<c:url value='/static/resources/scripts/codeMirror/mode/xmlpure/xmlpure.js'/>"></script>
<script src="<c:url value='/static/resources/scripts/codeMirror/lib/formatting.js'/>"></script>


<title>Hunter</title> 

<script type="text/javascript">
	$("document").ready(function() {
		
		$('.myMenu > li').bind('mouseover', openSubMenu);
		$('.myMenu > li').bind('mouseout', closeSubMenu);
		$('.myMenu > li a').bind('click', loadClickedMenuActions);
		
		function openSubMenu() {
			$(this).find('ul').css('visibility', 'visible');
		};
		function closeSubMenu() {
			$(this).find('ul').css('visibility', 'hidden');	
		};
		
		function loadClickedMenuActions(){
			var text = $(this).text().trim();
			console.log(text);
			if(text != null && text === "Client Tasks"){
				navigate("/login/postlogin");  
			}else if(text != null && text === "Addresses"){
				navigate("/address/action/view");
			}else if(text != null && text === "Weather"){
				navigate("/kendo/util/weather");
			}else if(text != null && text === "Sign Out"){
				navigate("/login/logout");
			}else if(text != null && text === "Workflow Configs"){
				navigate("/workflow/action/config/home");
			}else if(text != null && text === "Combobox"){
				navigate("/workflow/action/text/cascadeComboBox");
			}else if(text != null && text === "Home"){
				navigate("/hunter/login/after");
			}else if(text != null && text === "Regions Hierarchies"){
				navigate("/region/action/regions/hierarchies/action/home");
			}else if(text != null && text === "Receiver Groups"){
				navigate("/messageReceiver/action/group/home");
			}else if(text != null && text === "My Account"){
				navigate("/hunteruser/action/user/profile/home");
			}else if(text != null && text === "Hunter Admin"){
				navigate("/admin/action/admin/main");
			}else if(text != null && text === "Hunter Field Profile"){
				navigate("/admin/action/fieldProfile");
			}else if(text != null && text === "Contacts Pagination") {
				navigate("/admin/action/raw/pagination");
			}else if(text != null && text === "Receiver Tasks") {
				navigate("/hunter/tasks/home");
			}else if(text != null && text === "Social Tasks") {
				navigate("/hunter/tasks/home");
			}else if(text != null && text === "Validate Raw Receivers") {
				navigate("/admin/action/raw/validateReceivers"); 
			}
		}

		function navigate(url){
			window.location.href = HunterConstants.HUNTER_BASE_URL + url;
		}
				   
	});
</script>



<style type="text/css" > 
		/*style the main menu*/
	.k-tooltip-validation k-invalid-msg {
		background-color: #FF9191;
	}
		
	.myMenu {
		padding:0;
		position:relative;
	}
	
	.myMenu li {
		list-style:none;
		float:left;
		font:12px Arial, Helvetica, sans-serif #111;
		background-color:#E4F3F6;
	}
	
	.myMenu li a:link, .myMenu li a:visited {
		display:block;
		text-decoration:none;
		padding: 0.5em 2em;
		margin:0;
		color:black;
	}
	
	.myMenu li a:hover {
		background-color:#CDE7EE;
		color:black;
		font-weight: bolder;
	}
	
	/*style the sub menu*/
	.myMenu li ul {
		position:absolute;
		visibility:hidden;
		margin:0;
		padding:0;
	}
	
	.myMenu li ul li {
		display:inline;
		float:none;
	}
	
	.myMenu li ul li a:link, .myMenu li ul li a:visited {
		background-color:#E4F3F6;
		width:auto;
	}		
	
	.myMenu li ul li a:hover {
		background-color:#D4EDF3;
		border-top:1px solid #C8E3E9;
		border-bottom:1px solid #C8E3E9;	
	}
	
	#closeButtonDiv:hover{
		background-color: #B21E01;
	}
	
	/* Notification template */
	
	
</style>


</head>
<body>


<div id="hunterTopDiv" >

	<ul class="myMenu" id="menuUlId" style="z-index:10000;margin-left:39%;color:white;">
		<li style="border-left : 3px solid #93B1B7;" class="underLined" ><a href="#" id="homeTabHome" class="homeNavTabClass"  class="homeCurrentTab"   >Home</a>
			<ul class="rightAndLeftBottom" style='margin-left:-3px;' >
				<li><a href="#">Receiver Tasks</a></li>
				<li><a href="#">Social Tasks</a></li>
			</ul>
		</li>
	    <li class="underLined" ><a href="#"  id="homeTabTasks"  class="homeNavTabClass" >Regions</a>
	       <ul class="rightAndLeftBottom"  >
	       		<li><a href="#">Receiver Groups</a></li>
	        	<li><a href="#">Regions Hierarchies</a></li>
	        	<li><a href="#">Validate Raw Receivers</a></li>
	        </ul>
	    </li>
	    <li  style="border-right : 3px solid #93B1B7;"  class="underLined" ><a href="#"  id="homeTabTasks"  class="homeNavTabClass" >My Hunter</a>
	       <ul class="rightAndLeftBottom" >
	        	<li><a href="#">My Account</a></li>
	        	<li><a href="#">Hunter Admin</a></li>
	        	<li><a href="#">Hunter Field Profile</a></li>
	        	<li><a href="#">Contacts Pagination</a></li>
	            <li><a href="<c:url value="${pageContext.request.contextPath}/j_spring_security_logout" />" >Logout</a></li>
	        </ul>
	    </li>
	</ul>
</div>


















