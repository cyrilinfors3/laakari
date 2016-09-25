(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LtransactionsDialogController', LtransactionsDialogController);

    LtransactionsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ltransactions', 'Luser', 'Lproduit', 'Lfacture'];

    function LtransactionsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Ltransactions, Luser, Lproduit, Lfacture) {
        var vm = this;

        vm.ltransactions = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.lusers = Luser.query();
        vm.lproduits = Lproduit.query();
        vm.lfactures = Lfacture.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ltransactions.id !== null) {
                Ltransactions.update(vm.ltransactions, onSaveSuccess, onSaveError);
            } else {
                Ltransactions.save(vm.ltransactions, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:ltransactionsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datet = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
