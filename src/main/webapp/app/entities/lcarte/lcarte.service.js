(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lcarte', Lcarte);

    Lcarte.$inject = ['$resource'];

    function Lcarte ($resource) {
        var resourceUrl =  'api/lcartes/:id';

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
