'use strict';

var app = angular.module('scimServices', ['ngResource']);

app.factory('User', function($resource) {
	return $resource('/scim/user/:id');
});
