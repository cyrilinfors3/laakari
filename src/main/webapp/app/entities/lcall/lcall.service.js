(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lcall', Lcall);

    Lcall.$inject = ['$resource', 'DateUtils'];

    function Lcall ($resource, DateUtils) {
        var resourceUrl =  'api/lcalls/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.calltime = DateUtils.convertDateTimeFromServer(data.calltime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
