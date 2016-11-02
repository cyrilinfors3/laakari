(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('NewuserdmscController', NewuserdmscController);

    NewuserdmscController.$inject = ['$rootScope', 'Principal',  'Auth','$state','$http', 'Dmanager', '$scope','Lprofil','$stateParams'];

    function NewuserdmscController($rootScope, Principal,  Auth, $state, $http,  Dmanager, $scope, Lprofil, $stateParams) {
        var vm = this;
        vm.save = save;
        vm.codep=$stateParams.codep;
        vm.registerAccount = {};
        vm.dmanager={};
        vm.lprofil={};
        vm.emailexist="";

        function save () {
        	vm.dmanager.tel= vm.tel;
        	vm.dmanager.agentcode=vm.region;
        	 $http.get("api/useree/"+vm.email)
        	    .then(function(response) {
        	        if(response.data==false){
        	        	vm.emailexist="";
        	        	Dmanager.save(vm.dmanager, onSaveSuccess, onSaveError);
        	        }else{
        	        	vm.emailexist="email exist";
        	        }
        	        
        	    }, function(response) {
        	        
        	        $scope.content = "Something went wrong";
        	    });
        	    	
        	
        }
        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:dmanagerUpdate', result);
            
	           vm.registerAccount.login=result.agentcode;
	     	   vm.registerAccount.email=vm.email;
	     	   vm.registerAccount.password=result.agentcode;
	     	   vm.registerAccount.activated=true;
	     	   vm.registerAccount.langKey = vm.langKey;
	     	  Auth.createAccount(vm.registerAccount).then(function () {
	               vm.success = 'OK';
	               vm.lprofil.login=result.agentcode;
	               vm.lprofil.pass=result.agentcode;
	               vm.lprofil.email=vm.email;
	               vm.lprofil.tel= vm.tel;
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
