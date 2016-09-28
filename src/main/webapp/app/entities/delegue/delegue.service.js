(function() {
    'use strict';
    angular
        .module('laakariApp')
        .factory('Delegue', Delegue);

    Delegue.$inject = ['$resource', 'DateUtils'];

    function Delegue ($resource, DateUtils) {
        var resourceUrl =  'api/delegues/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.issuedate = DateUtils.convertLocalDateFromServer(data.issuedate);
                        data.dateofbirth = DateUtils.convertLocalDateFromServer(data.dateofbirth);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.issuedate = DateUtils.convertLocalDateToServer(data.issuedate);
                    data.dateofbirth = DateUtils.convertLocalDateToServer(data.dateofbirth);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.issuedate = DateUtils.convertLocalDateToServer(data.issuedate);
                    data.dateofbirth = DateUtils.convertLocalDateToServer(data.dateofbirth);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
