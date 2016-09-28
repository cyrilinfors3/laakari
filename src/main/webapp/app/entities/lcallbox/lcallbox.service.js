(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lcallbox', Lcallbox);

    Lcallbox.$inject = ['$resource', 'DateUtils'];

    function Lcallbox ($resource, DateUtils) {
        var resourceUrl =  'api/lcallboxes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateofbirth = DateUtils.convertLocalDateFromServer(data.dateofbirth);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateofbirth = DateUtils.convertLocalDateToServer(data.dateofbirth);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateofbirth = DateUtils.convertLocalDateToServer(data.dateofbirth);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
