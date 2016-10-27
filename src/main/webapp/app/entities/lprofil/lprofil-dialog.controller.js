(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LprofilDialogController', LprofilDialogController);

    LprofilDialogController.$inject = ['$translate', 'Auth','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Lprofil', 'Dmanager', 'Dealer', 'Sdealer', 'Delegue'];

    function LprofilDialogController ($translate, Auth, $timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Lprofil, Dmanager, Dealer, Sdealer, Delegue) {
        var vm = this;

        vm.lprofil = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.dmanagers = Dmanager.query();
        vm.dealers = Dealer.query();
        vm.sdealers = Sdealer.query();
        vm.delegues = Delegue.query();
        vm.registerAccount = {};

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lprofil.id !== null) {
                Lprofil.update(vm.lprofil, onSaveSuccess, onSaveError);
               
            } else {
                Lprofil.save(vm.lprofil, onSaveSuccess, onSaveError);
                register();
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lprofilUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

       function register(){
    	   vm.registerAccount.login=vm.lprofil.login;
    	   vm.registerAccount.email=vm.lprofil.email;
    	   vm.registerAccount.password=vm.lprofil.pass;
    	   vm.registerAccount.activated=true;
    	   vm.registerAccount.langKey = $translate.use();
    	   Auth.createAccount(vm.registerAccount).then(function () {
               vm.success = 'OK';
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
       }
  
        vm.setPic = function ($file, lprofil) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        lprofil.pic = base64Data;
                        lprofil.picContentType = $file.type;
                    });
                });
            }
        };

    }
})();
