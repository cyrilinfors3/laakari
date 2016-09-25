(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LcallboxDialogController', LcallboxDialogController);

    LcallboxDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lcallbox', 'Lvisit', 'Lpoint', 'Lroute'];

    function LcallboxDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lcallbox, Lvisit, Lpoint, Lroute) {
        var vm = this;

        vm.lcallbox = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.lvisits = Lvisit.query();
        vm.lpoints = Lpoint.query();
        vm.lroutes = Lroute.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lcallbox.id !== null) {
                Lcallbox.update(vm.lcallbox, onSaveSuccess, onSaveError);
            } else {
                Lcallbox.save(vm.lcallbox, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lcallboxUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateofbirth = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
