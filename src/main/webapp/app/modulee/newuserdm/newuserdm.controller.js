(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('NewuserdmController', NewuserdmController);

    NewuserdmController.$inject = ['$rootScope', 'Principal',  'Auth','$state','$http', 'Dmanager', '$scope','Lprofil'];

    function NewuserdmController($rootScope, Principal,  Auth, $state, $http,  Dmanager, $scope, Lprofil) {
        var vm = this;
        vm.savetest = savetest;
        vm.registerAccount = {};
        vm.dmanager={};
        vm.lprofil={};
        vm.emailexist="";
          
        function savetest(){
        	if(!vm.id){
        		savetestcompt ();
        	}else{
        		savetestSouscompt ();
        	}
        	
        }
        function savetestcompt () {
        	vm.dmanager.tel= vm.tel;
        	vm.dmanager.agentcode=vm.region;
        	 $http.get("/api/dmanagersfull/"+vm.tel+"/"+vm.region+"/"+vm.email+"/"+vm.langKey)
        	    .then(function(response) {
        	    	console.log(response);
        	        
        	    }, function(response) {
        	        
        	    	console.log(response);
        	    });
        	    	
        	
        }
        function savetestSouscompt () {
        	vm.dmanager.tel= vm.tel;
        	vm.dmanager.agentcode=vm.region;
        	 $http.get("/api/dmanagerssc/"+vm.id+"/"+vm.tel+"/"+vm.region+"/"+vm.email+"/"+vm.langKey)
        	    .then(function(response) {
        	    	console.log(response);
        	        
        	    }, function(response) {
        	        
        	    	console.log(response);
        	    });
        	    	
        	
        }
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
