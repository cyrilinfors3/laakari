(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lprofil', Lprofil);

    Lprofil.$inject = ['$resource'];

    function Lprofil ($resource) {
        var resourceUrl =  'api/lprofils/:id';

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
