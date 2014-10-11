<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>SCIM</title>
		<r:require modules="bootstrap grails-angularjs"/>
	</head>
	<body>
		<div ng-app="scim">
			<nav class="navbar navbar-default" role="navigation" ng-controller="NavCtrl">
				<div class="container-fluid">
					<div class="navbar-header">
						<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
							<span class="sr-only">Toggle navigation</span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
						<a class="navbar-brand" href="#">SCIM</a>
					</div>
  
					<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
						<ul class="nav navbar-nav">
							<li ng-class="{ active: isActive('/users')}"><a href="#/users">Users</a></li>
							<li ng-class="{ active: isActive('/groups')}"><a href="#/groups">Groups</a></li>
						</ul>
					</div>
				</div>
			</nav>
			<div class="container">
				<ng-view></ng-view>
			</div>
		</div>
	</body>
</html>
