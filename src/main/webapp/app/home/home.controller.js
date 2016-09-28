(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        $scope.showPopover = function($modu) {
        	  $scope.popoverIsVisible = true; 
        	  $scope.popoverContent=$modu;
        	};

        	$scope.hidePopover = function () {
        	 // $scope.popoverIsVisible = false;
        	  $scope.popoverContent="oooooo";
        	  
        	};
        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
