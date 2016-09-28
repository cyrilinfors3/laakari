(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lregion', Lregion);

    Lregion.$inject = ['$resource'];

    function Lregion ($resource) {
        var resourceUrl =  'api/lregions/:id';

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
