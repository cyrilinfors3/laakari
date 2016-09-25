(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lzone', Lzone);

    Lzone.$inject = ['$resource', 'DateUtils'];

    function Lzone ($resource, DateUtils) {
        var resourceUrl =  'api/lzones/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datecreation = DateUtils.convertDateTimeFromServer(data.datecreation);
                        data.datemodif = DateUtils.convertDateTimeFromServer(data.datemodif);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
