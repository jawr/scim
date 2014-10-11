'use strict';

var app = angular.module('scim', [
	'ngRoute',
 	'ui.bootstrap',
	'scimControllers',
	'scimDirectives'
]);

app.config(function($routeProvider) {
	$routeProvider
		.when('/users', {
			templateUrl: 'partials/users.html',
			controller: 'UsersCtrl'
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
