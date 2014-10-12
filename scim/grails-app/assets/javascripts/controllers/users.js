'use strict';

var app = angular.module('scimControllers');

app.controller('UsersCtrl',
	function($rootScope, $scope, User) {
        $scope.users = User.query();
        $rootScope.$on('update-users', function(ev, args) {
            $scope.users.push(args.user);
        });
	}
);

app.controller('UsersCreateCtrl',
	function($rootScope, $scope, $routeParams, $timeout, User) {
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
					$scope.created = true;
                                        $scope.user = data;
                                        $rootScope.$broadcast('update-users', { user: data });
                                        $timeout(function() {
                                                $scope.created = false;
                                        }, 3000);
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
	function($scope, $routeParams, $timeout, $location, User, Group) {
        $scope.updated = false;
        $scope.serverError = false;
		if ($routeParams.id) {
			$scope.user = User.get({id: $routeParams.id}, function() {
                // other init
                $scope.user.password2 = $scope.user.password;
            });
		}
    
        $scope.update = function(user) {
			$scope.$broadcast('show-errors-check-validity');
			if ($scope.form.$invalid) { return }

            $scope.serverError = false;
            user.$update(function() {
                $scope.updated = true;
                $scope.user.password2 = $scope.user.password;
                $timeout(function() {
                    $scope.updated = false;
                }, 3000);
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

        $scope.getGroup = function(val) {
            return Group.query({displayName: val}).$promise.then(
                function(resp) {
                    return resp;
                }
            );
        };

        $scope.addGroup = function(user, group) {
            if (!user.groups) { user.groups = [] }
            user.groups.push({ value: group.id, display: group.displayName });
        };

        $scope.removeGroup = function(user, group) {
            var idx = user.groups.indexOf(group);
            if (idx >= 0) {
                user.groups.splice(idx, 1);
            }
        };
	}
);
