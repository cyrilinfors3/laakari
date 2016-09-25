(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Dealer', Dealer);

    Dealer.$inject = ['$resource'];

    function Dealer ($resource) {
        var resourceUrl =  'api/dealers/:id';

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
