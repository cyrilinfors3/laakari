(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LuserDialogController', LuserDialogController);

    LuserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Luser', 'Dmanager', 'Dealer', 'Sdealer', 'Delegue', 'Lzone'];

    function LuserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Luser, Dmanager, Dealer, Sdealer, Delegue, Lzone) {
        var vm = this;

        vm.luser = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dmanagers = Dmanager.query();
        vm.dealers = Dealer.query();
        vm.sdealers = Sdealer.query();
        vm.delegues = Delegue.query();
        vm.lzones = Lzone.query({filter: 'luser-is-null'});
        $q.all([vm.luser.$promise, vm.lzones.$promise]).then(function() {
            if (!vm.luser.lzone || !vm.luser.lzone.id) {
                return $q.reject();
            }
            return Lzone.get({id : vm.luser.lzone.id}).$promise;
        }).then(function(lzone) {
            vm.lzones.push(lzone);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.luser.id !== null) {
                Luser.update(vm.luser, onSaveSuccess, onSaveError);
            } else {
                Luser.save(vm.luser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:luserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
