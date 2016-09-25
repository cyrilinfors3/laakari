(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lville', Lville);

    Lville.$inject = ['$resource'];

    function Lville ($resource) {
        var resourceUrl =  'api/lvilles/:id';

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
