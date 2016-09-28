(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Ltransactions', Ltransactions);

    Ltransactions.$inject = ['$resource', 'DateUtils'];

    function Ltransactions ($resource, DateUtils) {
        var resourceUrl =  'api/ltransactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datet = DateUtils.convertDateTimeFromServer(data.datet);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
