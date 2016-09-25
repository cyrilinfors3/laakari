(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LfactureDialogController', LfactureDialogController);

    LfactureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lfacture', 'Ltransactions'];

    function LfactureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lfacture, Ltransactions) {
        var vm = this;

        vm.lfacture = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.lfacture.id !== null) {
                Lfacture.update(vm.lfacture, onSaveSuccess, onSaveError);
            } else {
                Lfacture.save(vm.lfacture, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lfactureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fdate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
