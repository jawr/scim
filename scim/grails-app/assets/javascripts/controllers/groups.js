'use strict';

var app = angular.module('scimControllers');

app.controller('GroupsCtrl',
	function($rootScope, $scope, $modal, Group) {
        $scope.groups = Group.query();
        $rootScope.$on('update-groups', function(ev, args) {
            $scope.groups.push(args.group);
        });
	}
);

app.controller('GroupsCreateCtrl',
    function($rootScope, $scope, $routeParams, $timeout, Group) {
        $scope.group = new Group();
        $scope.created = false;
        $scope.duplicate = false;

		$scope.create = function(group) {
			$scope.$broadcast('show-errors-check-validity');
			if ($scope.form.$invalid) { return }

			Group.save(group,	
				function(data) {
					$scope.created = true;
                    $rootScope.$broadcast('update-groups', { group: data });
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

app.controller('GroupsEditCtrl',
	function($scope, $routeParams, $timeout, $location, Group) {
        $scope.updated = false;
        $scope.serverError = false;
		if ($routeParams.id) {
			$scope.group = Group.get({id: $routeParams.id});
		}

        $scope.delete = function(group) {
            $scope.serverError = false;
            group.$delete(function() {
                $scope.deleted = true;
                $timeout(function() {
                    $location.path('/groups');
                }, 3000);
            }, function(resp) {
                $scope.serverError = true;
                $scope.serverMsg = resp.data;
            });
        };
    }
);
