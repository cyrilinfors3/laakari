(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LproduitDialogController', LproduitDialogController);

    LproduitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lproduit', 'Ltransactions'];

    function LproduitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lproduit, Ltransactions) {
        var vm = this;

        vm.lproduit = entity;
        vm.clear = clear;
        vm.save = save;
        vm.ltransactions = Ltransactions.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lproduit.id !== null) {
                Lproduit.update(vm.lproduit, onSaveSuccess, onSaveError);
            } else {
                Lproduit.save(vm.lproduit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lproduitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
