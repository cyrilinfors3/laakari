(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LrouteDialogController', LrouteDialogController);

    LrouteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lroute', 'Lcallbox', 'Lzone'];

    function LrouteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lroute, Lcallbox, Lzone) {
        var vm = this;

        vm.lroute = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lcallboxes = Lcallbox.query();
        vm.lzones = Lzone.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lroute.id !== null) {
                Lroute.update(vm.lroute, onSaveSuccess, onSaveError);
            } else {
                Lroute.save(vm.lroute, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lrouteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
