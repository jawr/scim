'use strict';

// definitions
angular.module('scimServices', ['ngResource']);
angular.module('scimControllers', ['scimServices', 'ui.bootstrap']);

var app = angular.module('scim', [
	'ngRoute',
 	'ui.bootstrap',
	'scimControllers',
	'scimDirectives',
	'scimServices'
]);

app.config(function($routeProvider) {
	$routeProvider
		.when('/groups', {
			templateUrl: 'partials/groups/index.html',
			controller: 'GroupsCtrl'
		})
		.when('/groups/:id', {
			templateUrl: 'partials/groups/edit.html',
			controller: 'GroupsEditCtrl'
		})
		.when('/users', {
			templateUrl: 'partials/users/index.html',
			controller: 'UsersCtrl'
		})
		.when('/users/:id', {
			templateUrl: 'partials/users/edit.html',
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
