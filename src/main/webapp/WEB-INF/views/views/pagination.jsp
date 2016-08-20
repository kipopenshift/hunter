<%@include file="../common/JSTL_tags.jsp"%>
<!DOCTYPE html>
<html style="font-size:13px;" >
<head>

<link href="http://cdn.datatables.net/1.10.3/css/jquery.dataTables.css" rel="stylesheet"  type="text/css">
<link href="http://datatables.net/release-datatables/extensions/ColVis/css/dataTables.colVis.css" rel="stylesheet" type="text/css">
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="http://cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
<script src="http://datatables.net/release-datatables/extensions/ColVis/js/dataTables.colVis.js" ></script>
<script src="http://jquery-datatables-column-filter.googlecode.com/svn/trunk/media/js/jquery.dataTables.columnFilter.js"></script>

<script src="<c:url value='/static/resources/scripts/plain/pagination.js'/>"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/custom-datatable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugin/fnStandingRedraw.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugin/fnSetFilteringDelay.js"></script>

<title>Hunter Field Contacts</title>

<script type="text/javascript">



</script>

</head>

<body>

<div id="fieldProfileContainer" style="margin-top:1%;"  > 


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



</body>
</html>