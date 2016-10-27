(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('AccmController', AccmController);

    AccmController.$inject = ['$rootScope', 'Principal', 'LoginService', 'Auth','$state'];

    function AccmController($rootScope, Principal, LoginService, Auth, $state) {
        var vm = this;

       
         
    }
})();
