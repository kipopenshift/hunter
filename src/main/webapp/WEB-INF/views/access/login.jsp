<%@include file="../common/HeaderFile.jsp"%>

<div id="loginCover">
	
	<form id="hunterLoginForm" method="POST" action="/" >
		<table>
			<tr>
				<td>User Name : </td>
				<td><input class="hunterInput" placeHolder="Enter user name..." id="hunterUserName" name="userName" type="text"/></td>
				<td id="hunterUserNameError" class="errors" ></td>
			</tr>
			<tr>
				<td>Password   : </td>
				<td><input class="hunterInput" placeHolder="Enter Password..." id="hunterPassword" name="password" type="password"/></td>
				<td id="hunterPasswordError" class="errors" ></td>
			</tr>
		</table>
		<br/>
		<table>
			<tr>
				<td> </td>
				<td><button class="button" name="submit" type="submit" name="hunterLoginPassword">Login</button></td> 
			</tr>
		</table>
	</form>
	
</div>

</body>
</html>
