(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lpoint', Lpoint);

    Lpoint.$inject = ['$resource'];

    function Lpoint ($resource) {
        var resourceUrl =  'api/lpoints/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
