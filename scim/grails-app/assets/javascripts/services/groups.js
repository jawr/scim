'use strict';

var app = angular.module('scimServices');

app.factory('Group', function($resource) {
	return $resource('/scim/v1/scimGroup/:id', { id: '@id' },
        {
            update: { method: 'PUT' }
        }
    );
});
