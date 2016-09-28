(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Luser', Luser);

    Luser.$inject = ['$resource'];

    function Luser ($resource) {
        var resourceUrl =  'api/lusers/:id';

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
