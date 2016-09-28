(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('SdealerDialogController', SdealerDialogController);

    SdealerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Sdealer', 'Lprofil', 'Luser'];

    function SdealerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Sdealer, Lprofil, Luser) {
        var vm = this;

        vm.sdealer = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.lprofils = Lprofil.query();
        vm.lusers = Luser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sdealer.id !== null) {
                Sdealer.update(vm.sdealer, onSaveSuccess, onSaveError);
            } else {
                Sdealer.save(vm.sdealer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:sdealerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setLogo = function ($file, sdealer) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        sdealer.logo = base64Data;
                        sdealer.logoContentType = $file.type;
                    });
                });
            }
        };

    }
})();
