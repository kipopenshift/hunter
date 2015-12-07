<%@include file="JSTL_tags.jsp"%>
<!DOCTYPE html>
<html style="font-size:13px;" >
<head>

<meta charset="utf-8">

<link rel="stylesheet" href="http://cdn.kendostatic.com/2015.3.930/styles/kendo.common.min.css">
<link rel="stylesheet" href="http://cdn.kendostatic.com/2015.3.930/styles/kendo.default.min.css">
<link rel="stylesheet" href="http://cdn.kendostatic.com/2015.3.930/styles/kendo.dataviz.min.css">
<link rel="stylesheet" href="http://cdn.kendostatic.com/2015.3.930/styles/kendo.dataviz.default.min.css">

<link rel="stylesheet" href="http://localhost:8080/Hunter/static/resources/kendo/kendo.custom.css">

<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://cdn.kendostatic.com/2015.3.930/js/kendo.all.min.js"></script>



<%-- <script src="<c:url value='/static/resources/scripts/fancyScript.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/static/resources/css/fancyCss.css'/>">
 --%>
 <script src="<c:url value='/static/resources/scripts/plain/hunter.js'/>"></script>
 <script src="<c:url value='/static/resources/scripts/plain/hunterConstants.js'/>"></script>
 <link rel="stylesheet" href="<c:url value='/static/resources/css/hunter.css'/>">
 <link rel="stylesheet" href="<c:url value='/static/resources/css/kendoGen.css'/>">






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
			if(text != null && text === "Clients"){
				navigate("/client/action/mainPage");  
			}else if(text != null && text === "Messages"){
				navigate("/message/action/mainPage");
			}else if(text != null && text === "Addresses"){
				navigate("/address/action/view");
			}else if(text != null && text === "Weather"){
				navigate("/kendo/util/weather");
			}else if(text != null && text === "Calculator"){
				navigate("/kendo/services/cal/view");
			}else if(text != null && text === "Workflow Configs"){
				navigate("/workflow/action/config/home");
			}else if(text != null && text === "Combobox"){
				navigate("/workflow/action/text/cascadeComboBox");
			}else if(text != null && text === "Home"){
				navigate("");
			}else{
				//kendoKipHelperInstance.showSimplePopup("Kendo", text);
			}
		}

		function navigate(url){
			window.location.href = HunterConstants.HUNTER_BASE_URL + url;
		}
				   
	});
</script>



<style type="text/css" > 
		/*style the main menu*/
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
	}f
	
	.myMenu li a:hover {
		background-color:#E4F3F6;
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
		background-color:#E4F3F6;	
	}
	
	/* Notification template */
     .k-notification-upload-success.k-group {
         background: rgba(0%,60%,0%,.7);
         color: #fff;
     }
     .upload-success {
         width: 240px;
         height: 100px;
         line-height: 100px;
     }
     .upload-success h3 {
         font-size: 1.7em;
         font-weight: normal;
         display: inline-block;
         vertical-align: middle;
     }
     .upload-success img {
         display: inline-block;
         vertical-align: middle;
         margin-right: 10px;
     }
</style>


</head>
<body>


<div id="hunterTopDiv" >

	<ul class="myMenu" id="menuUlId" style="z-index:1;margin-left:40%;color:white;">
		<li class="underLined" ><a href="#" id="homeTabHome" class="homeNavTabClass"  class="homeCurrentTab"   >Home</a></li>
	    <li class="underLined" ><a href="#"  id="homeTabClients"  class="homeNavTabClass" >Clients</a>
	    	<ul class="rightAndLeftBottom" >
	        	<li><a href="#" >Clients</a></li>
	        	<li><a href="#" >Users</a></li>
	        </ul>
	    </li>
	    <li class="underLined" ><a href="#"  id="homeTabMessages" class="homeNavTabClass" >Messages</a>
	       <ul class="rightAndLeftBottom" >
	        	<li><a href="#">Received</a></li>
	            <li><a href="#">Sent</a></li>
	            <li><a href="#">Notifications</a></li>
	        </ul>
	    </li>
	    <li class="underLined" ><a href="#"  id="homeTabTasks"  class="homeNavTabClass" >Tasks</a>
	       <ul class="rightAndLeftBottom" >
	        	<li><a href="#">Text Tasks</a></li>
	            <li><a href="#">Voice Mail Tasks</a></li>
	            <li><a href="#">Call Tasks</a></li>
	            <li><a href="#">Email Tasks</a></li>
	        </ul>
	    </li>
	</ul>
</div>


















