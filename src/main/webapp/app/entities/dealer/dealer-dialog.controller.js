(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('DealerDialogController', DealerDialogController);

    DealerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Dealer', 'Lprofil', 'Luser'];

    function DealerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Dealer, Lprofil, Luser) {
        var vm = this;

        vm.dealer = entity;
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
            if (vm.dealer.id !== null) {
                Dealer.update(vm.dealer, onSaveSuccess, onSaveError);
            } else {
                Dealer.save(vm.dealer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:dealerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setLogo = function ($file, dealer) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        dealer.logo = base64Data;
                        dealer.logoContentType = $file.type;
                    });
                });
            }
        };

    }
})();
