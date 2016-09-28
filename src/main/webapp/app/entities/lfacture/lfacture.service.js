(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lfacture', Lfacture);

    Lfacture.$inject = ['$resource', 'DateUtils'];

    function Lfacture ($resource, DateUtils) {
        var resourceUrl =  'api/lfactures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fdate = DateUtils.convertDateTimeFromServer(data.fdate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
