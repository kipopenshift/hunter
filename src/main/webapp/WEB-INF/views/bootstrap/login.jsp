
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Hunter</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" href="/Hunter/static/resources/bootstrapSolar/css/bootstrap.css" media="screen">
    <link rel="stylesheet" href="/Hunter/static/resources/bootstrapSolar/css/custom.min.css">
    <link rel="stylesheet" href="/Hunter/static/resources/bootstrapSolar/css/hunterBootstrap.css">
    <link rel="stylesheet" href="/Hunter/static/resources/bootstrapSolar/css/hunterLogin.css">
    
     <script>

     var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-23019901-1']);
      _gaq.push(['_setDomainName', "bootswatch.com"]);
        _gaq.push(['_setAllowLinker', true]);
      _gaq.push(['_trackPageview']);

     (function() {
       var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
       ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
     })();

    </script>    
  </head>
  <body>
    <div class="navbar navbar-expand-lg fixed-top navbar-dark bg-dark">
      <div class="container loginNavBar" >
        
      </div>
    </div>


	<div class="container">
		<div class="row" style="margin-bottom: 2rem;">
			<div class="col-md-offset-6 col-md-8">
				<div class="bs-component text-center">
					<ul class="nav nav-tabs">
						<li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#login">Login</a></li>
						<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#createAccount">Create Account</a></li>
					</ul>
					<div id="loginTabContent" class="tab-content">
						<div class="tab-pane fade in  active show " id="login">
							<div class="jumbotron">
							  <table class='loginTable'  style='width:40%;margin-left:30%;border-spacing: 5px;' >
							  	<tr>
							  		<td>User Name</td>
							  		<td><input type="text" class="hunterSolarInput" placeholder="Enter Username" id='userName' ></td>							  		
							  	</tr>
							  	<tr>
							  		<td></td>
							  		<td class='hunterBootstrapSolarErrors hidden' id='userNameError' >User name is required</td>
							  	</tr>
							  	<tr>
							  		<td>Password</td>
							  		<td><input type="password" class="hunterSolarInput" placeholder="Enter Password"  id='password'  ></td>
							  	</tr>
							  	<tr>
							  		<td></td>
							  		<td class='hunterBootstrapSolarErrors hidden' id='passwordError' >Password is required</td>
							  	</tr>
							  </table>
							  <p style='text-align:center; margin-top:2em;' >
							    <button type="button" class="btn btn-primary btn-lg" onClick='validateLoginForm()' >Login</button>
							  </p>							  
							</div>
						</div>
						<div class="tab-pane fade" id="createAccount">
							<div class="jumbotron">
							  <table class='loginTable'  style='width:40%;margin-left:30%;border-spacing: 5px;' >
							  	<tr>
							  		<td>User Name</td>
							  		<td><input type="text" class="hunterSolarInput" placeholder="Enter Username" id='userName' ></td>							  		
							  	</tr>
							  	<tr>
							  		<td></td>
							  		<td class='hunterBootstrapSolarErrors hidden' id='userNameError' >User name is required</td>
							  	</tr>
							  	<tr>
							  		<td>Password</td>
							  		<td><input type="password" class="hunterSolarInput" placeholder="Enter Password"  id='password'  ></td>
							  	</tr>
							  	<tr>
							  		<td></td>
							  		<td class='hunterBootstrapSolarErrors hidden' id='passwordError' >Password is required</td>
							  	</tr>
							  	<tr>
							  		<td>User Name</td>
							  		<td><input type="text" class="hunterSolarInput" placeholder="Enter Username" id='userName' ></td>							  		
							  	</tr>
							  	<tr>
							  		<td></td>
							  		<td class='hunterBootstrapSolarErrors hidden' id='userNameError' >User name is required</td>
							  	</tr>
							  	<tr>
							  		<td>Password</td>
							  		<td><input type="password" class="hunterSolarInput" placeholder="Enter Password"  id='password'  ></td>
							  	</tr>
							  	<tr>
							  		<td></td>
							  		<td class='hunterBootstrapSolarErrors hidden' id='passwordError' >Password is required</td>
							  	</tr>
							  	<tr>
							  		<td>User Name</td>
							  		<td><input type="text" class="hunterSolarInput" placeholder="Enter Username" id='userName' ></td>							  		
							  	</tr>
							  	<tr>
							  		<td></td>
							  		<td class='hunterBootstrapSolarErrors hidden' id='userNameError' >User name is required</td>
							  	</tr>
							  	<tr>
							  		<td>Password</td>
							  		<td><input type="password" class="hunterSolarInput" placeholder="Enter Password"  id='password'  ></td>
							  	</tr>
							  	<tr>
							  		<td></td>
							  		<td class='hunterBootstrapSolarErrors hidden' id='passwordError' >Password is required</td>
							  	</tr>
							  </table>
							  <p style='text-align:center; margin-top:2em;' >
							    <button type="button" class="btn btn-primary btn-lg" onClick='validateLoginForm()' >Sign Up</button>
							  </p>							  
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	<!-- Button trigger modal -->
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
  Launch demo modal
</button>

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        ...
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>




	
	
	<script src="/Hunter/static/resources/bootstrapSolar/js/jquery.min.js"></script>
    <script src="/Hunter/static/resources/bootstrapSolar/js/popper.min.js"></script>
    <script src="/Hunter/static/resources/bootstrapSolar/js/bootstrap.min.js"></script>
    <script src="/Hunter/static/resources/bootstrapSolar/js/custom.js"></script>
    <script src="/Hunter/static/resources/bootstrapSolar/js/hunterLogin.js"></script> 
    
    
  
    
  </body>
</html>