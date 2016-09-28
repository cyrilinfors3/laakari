(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Dmanager', Dmanager);

    Dmanager.$inject = ['$resource'];

    function Dmanager ($resource) {
        var resourceUrl =  'api/dmanagers/:id';

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
