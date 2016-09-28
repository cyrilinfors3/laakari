(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LcallDialogController', LcallDialogController);

    LcallDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lcall', 'Luser'];

    function LcallDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lcall, Luser) {
        var vm = this;

        vm.lcall = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.lusers = Luser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lcall.id !== null) {
                Lcall.update(vm.lcall, onSaveSuccess, onSaveError);
            } else {
                Lcall.save(vm.lcall, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lcallUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.calltime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
