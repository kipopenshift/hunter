
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Hunter</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" href="/Hunter/static/resources/bootstrapSolar/css/bootstrap.css" media="screen">
    <link rel="stylesheet" href="/Hunter/static/resources/bootstrapSolar/css/custom.min.css">
    <link rel="stylesheet" href="/Hunter/static/resources/bootstrapSolar/css/login.css">
    <link rel="stylesheet" href="/Hunter/static/resources/bootstrapSolar/css/hunterSolar.css">
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
    


    <div class="container">

      <div class='row'>
      	<div class='col-md-8 col-md-offset-4' style="float: none; margin: 0 auto;">
      		<div class="jumbotron">
		    	<form class="form-signin">       
		      		<h2 class="form-signin-heading">Please login</h2>
		      		<input type="text" class="form-control" name="username" id="username" placeholder="Email Address" required="true" autofocus="true" />
		      		<input type="password" class="form-control" name="password" id="password"  placeholder="Password" required="true" />      
		      		<label class="checkbox">
		        		<input type="checkbox" value="remember-me" id="rememberMe" name="rememberMe"> Remember me
		      		</label>
		      		<button class="btn btn-lg btn-primary btn-block" type="submit" onSubmit='validateAndSubmit()' >Login</button>   
		    	</form>
		  </div>
      	</div>
      </div>

	 <span id='errors' style='color:red;font-size: 1.3em;border-top:20px;' ></span>

    </div>


    <script src="/Hunter/static/resources/bootstrapSolar/js/jquery.min.js"></script>
    <script src="/Hunter/static/resources/bootstrapSolar/js/popper.min.js"></script>
    <script src="/Hunter/static/resources/bootstrapSolar/js/bootstrap.min.js"></script>
    <script src="/Hunter/static/resources/bootstrapSolar/js/custom.js"></script>
    <script src="/Hunter/static/resources/bootstrapSolar/js/login.js"></script>
  </body>
</html>