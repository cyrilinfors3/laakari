'use strict';

describe('Controller Tests', function() {

    describe('Ltransactions Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLtransactions, MockLuser, MockLproduit, MockLfacture;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLtransactions = jasmine.createSpy('MockLtransactions');
            MockLuser = jasmine.createSpy('MockLuser');
            MockLproduit = jasmine.createSpy('MockLproduit');
            MockLfacture = jasmine.createSpy('MockLfacture');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Ltransactions': MockLtransactions,
                'Luser': MockLuser,
                'Lproduit': MockLproduit,
                'Lfacture': MockLfacture
            };
            createController = function() {
                $injector.get('$controller')("LtransactionsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:ltransactionsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
