
(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'Auth','$state'];

    function HomeController ($scope, Principal, LoginService, Auth, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });
        
        $scope.showPopover = function($modu) {
        	  $scope.popoverIsVisible = true; 
        	  $scope.popoverContent=$modu;
        	 switch($modu)
        	 {
        	 case 1:
	        	{
        		 $scope.popoverContent=$scope.accountManagement;
        		 break;
	        	}
        	 case 2:
	        	{
     		 $scope.popoverContent=$scope.zoneManagement;
     		 break;
	        	}
        	 case 3:
	        	{
		  		 $scope.popoverContent=$scope.accountingManagement;
		  		 break;
		        	}
        	 case 4:
	        	{
		  		 $scope.popoverContent=$scope.communication;
		  		 break;
		        	}
	         }
        	 
        	 
        	 // if($modu==2){
        		//  $state.go('accm');
        	  //}
        	};
               
        	 $scope.showModule = function($modu) {	 
            	 if($modu==2){
            		  $state.go('accm');
            	 }
            	};
        	$scope.hidePopover = function () {
        	 // $scope.popoverIsVisible = false;
        	  $scope.popoverContent="oooooo";
        	  
        	};
        function testIfisAuth(){
        	if(  !Principal.isAuthenticated){
            	$state.go('login');
        	} 
        }
        getAccount();	
        testIfisAuth();
      
        
        $scope.accountManagement="Account Management";
        $scope.zoneManagement="Zone Management";
        $scope.accountingManagement="Accounting Management";
        $scope.communication="Communication";

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        
        $scope.createuser=function($n){
          switch($n)
          {
          case 1:
        	  {
        	  console.log('newuserdm');
        	  $state.go('newuserdm');
        	  break;
        	  }
          case 11:
    	  {
    	  console.log('newuserdmsc');
    	  $state.go('newuserdmsc',{codep:'33333333333333333'});
    	  break;
    	  }
          case 2:
	    	  {
	    	  $state.go('newuserd');
	    	  break;
	    	  }
          case 3:
    	  {
    	  $state.go('newuser');
    	  break;
    	  }
          }
        	
        }
         
    }
})();
