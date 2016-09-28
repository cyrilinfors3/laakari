(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lvisit', Lvisit);

    Lvisit.$inject = ['$resource', 'DateUtils'];

    function Lvisit ($resource, DateUtils) {
        var resourceUrl =  'api/lvisits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
