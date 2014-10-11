'use strict';

var app = angular.module('scimControllers', []);

app.controller('UsersCtrl',
	function() {
	}
);

app.controller('UsersCreateCtrl',
	function($scope) {
		$scope.user = {};
		$scope.passwordMismatch = false;

		$scope.create = function(user) {
			$scope.passwordMismatch = (user.password !== user.password2);
			if ($scope.passwordMismatch) { return }
			$scope.$broadcast('show-errors-check-validity');
			if ($scope.form.$invalid) { return }

			console.log(user);
		};
	}
);
