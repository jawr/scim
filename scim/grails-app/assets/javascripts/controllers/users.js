'use strict';

var app = angular.module('scimControllers', ['ui.bootstrap']);

app.controller('UsersCtrl',
	function($rootScope, $scope, $modal, User) {
        $scope.users = User.query();
        $rootScope.$on('update-users', function(ev, args) {
            $scope.users.push(args.user);
        });
	}
);

app.controller('UsersCreateCtrl',
	function($rootScope, $scope, $routeParams, User) {
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
				function(data) {
                    console.log(data);
					$scope.created = true;
                    $rootScope.$broadcast('update-users', { user: data });
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
	function($scope, $routeParams, $timeout, $location, User) {
        $scope.updated = false;
        $scope.serverError = false;
		if ($routeParams.id) {
			$scope.user = User.get({id: $routeParams.id}, function() {
                // other init
                $scope.user.password2 = $scope.user.password;
            });
		}
    
        $scope.update = function(user) {
            $scope.serverError = false;
            user.$update(function() {
                $scope.updated = true;
                $timeout(function() {
                    $scope.updated = false;
                }, 5000);
            }, function(resp) {
                $scope.serverError = true;
                $scope.serverMsg = resp.data;
            });
        };

        $scope.delete = function(user) {
            $scope.serverError = false;
            user.$delete(function() {
                $scope.deleted = true;
                $timeout(function() {
                    $location.path('/users');
                }, 3000);
            }, function(resp) {
                $scope.serverError = true;
                $scope.serverMsg = resp.data;
            });
        };
	}
);
