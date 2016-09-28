(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lproduit', Lproduit);

    Lproduit.$inject = ['$resource'];

    function Lproduit ($resource) {
        var resourceUrl =  'api/lproduits/:id';

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
