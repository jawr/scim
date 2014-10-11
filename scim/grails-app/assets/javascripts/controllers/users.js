'use strict';

var app = angular.module('scimControllers', ['ui.bootstrap']);

app.controller('UsersCtrl',
	function($scope, $modal, User) {
		User.query(function(result) {
			$scope.users = result;
		});
	}
);

app.controller('UsersCreateCtrl',
	function($scope, $routeParams, User) {
		if ($routeParams.id) {
			
		}
		$scope.user = new User();
		$scope.created = false;
		$scope.duplicate = false;
		$scope.passwordMismatch = false;

		$scope.create = function(user) {
			$scope.passwordMismatch = (user.password !== user.password2);
			if ($scope.passwordMismatch) { return }

			$scope.$broadcast('show-errors-check-validity');
			if ($scope.form.$invalid) { return }

			User.save(user,	
				function() {
					$scope.created = true;
				},
				function(data) {
					if (data.status === 409) {
						$scope.duplicate = true;
					} else {
						$scope.serverError = true;
						$scope.serverMsg = data.data;
					}
				}
			);
		};
	}
);

app.controller('UsersEditCtrl',
	function($scope, $routeParams, $modal, User) {
		if ($routeParams.id) {
			$scope.user = User.get({id: $routeParams.id});
		}

		$modal.open({
			templateUrl: 'users.edit.modal.html'
		});
	}
);
