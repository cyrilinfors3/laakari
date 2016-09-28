(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Larrondissement', Larrondissement);

    Larrondissement.$inject = ['$resource'];

    function Larrondissement ($resource) {
        var resourceUrl =  'api/larrondissements/:id';

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
