(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Lmessage', Lmessage);

    Lmessage.$inject = ['$resource', 'DateUtils'];

    function Lmessage ($resource, DateUtils) {
        var resourceUrl =  'api/lmessages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.sentdate = DateUtils.convertLocalDateFromServer(data.sentdate);
                        data.senthours = DateUtils.convertDateTimeFromServer(data.senthours);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.sentdate = DateUtils.convertLocalDateToServer(data.sentdate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.sentdate = DateUtils.convertLocalDateToServer(data.sentdate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
