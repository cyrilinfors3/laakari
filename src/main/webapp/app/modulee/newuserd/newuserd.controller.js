(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('NewuserdController', NewuserdController);

    NewuserdController.$inject = ['$rootScope', 'Principal',  'Auth','$state','$http', 'Dealer', '$scope','Lprofil'];

    function NewuserdController($rootScope, Principal,  Auth, $state, $http,  Dealer, $scope, Lprofil) {
        var vm = this;
        vm.save = save;
        vm.registerAccount = {};
        vm.dealer={};
        vm.lprofil={};
        vm.emailexist="";
/* 
 * [ {
  "id" : 1,
  "shopcode" : "ee",
  "sigle" : "ffffffffff",
  "mastersim" : 1,
  "logo" : null,
  "logoContentType" : null,
  "luser" : null
} ]
 */
        function save () {
        	vm.dealer.mastersim= vm.mastersim;
        	vm.dealer.shopcode=vm.region;
        	vm.dealer.sigle=vm.sigle;
        	 $http.get("api/useree/"+vm.email)
        	    .then(function(response) {
        	        if(response.data==false){
        	        	vm.emailexist="";
        	        	Dealer.save(vm.dealer, onSaveSuccess, onSaveError);
        	        }else{
        	        	vm.emailexist="email exist";
        	        }
        	        
        	    }, function(response) {
        	        
        	        $scope.content = "Something went wrong";
        	    });
        	    	
        	
        }
        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:dmanagerUpdate', result);
            
	           vm.registerAccount.login=result.shopcode;
	     	   vm.registerAccount.email=vm.email;
	     	   vm.registerAccount.password=result.shopcode;
	     	   vm.registerAccount.activated=true;
	     	   vm.registerAccount.langKey = vm.langKey;
	     	  Auth.createAccount(vm.registerAccount).then(function () {
	               vm.success = 'OK';
	               vm.lprofil.login=vm.registerAccount.login;
	               vm.lprofil.pass=vm.registerAccount.password;
	               vm.lprofil.email=vm.email;
	               vm.lprofil.tel= vm.mastersim;
	               Lprofil.save(vm.lprofil, onSaveSuccess1, onSaveError1);
	           }).catch(function (response) {
	               vm.success = null;
	               if (response.status === 400 && response.data === 'login already in use') {
	                   vm.errorUserExists = 'ERROR';
	               } else if (response.status === 400 && response.data === 'e-mail address already in use') {
	                   vm.errorEmailExists = 'ERROR';
	               } else {
	                   vm.error = 'ERROR';
	               }
	           });
            vm.isSaving = false;
            
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        function onSaveSuccess1 (result) {
            $scope.$emit('laakariApp:lprofilUpdate', result);
            
            vm.isSaving = false;
        }

        function onSaveError1 () {
            vm.isSaving = false;
        }
    }
})();
