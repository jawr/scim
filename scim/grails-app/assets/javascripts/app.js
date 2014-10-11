'use strict';

var app = angular.module('scim', [
	'ngRoute',
 	'ui.bootstrap',
	'scimControllers',
	'scimDirectives',
	'scimServices'
]);

app.config(function($routeProvider) {
	$routeProvider
		.when('/users', {
			templateUrl: 'partials/users.html',
			controller: 'UsersCtrl'
		})
		.when('/users/:id', {
			templateUrl: 'partials/users.edit.html',
			controller: 'UsersEditCtrl'
		})
		.when('/', {
			templateUrl: 'partials/index.html'
		})
		.otherwise({redirectTo: '/'});
});

// required at bootstrap
app.controller('NavCtrl',
	function($scope, $location) {
		$scope.isActive = function (viewLocation) { 
        	return viewLocation === $location.path();
    	};
	}
);
