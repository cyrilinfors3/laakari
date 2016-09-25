'use strict';

describe('Controller Tests', function() {

    describe('Dealer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDealer, MockLprofil, MockLuser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDealer = jasmine.createSpy('MockDealer');
            MockLprofil = jasmine.createSpy('MockLprofil');
            MockLuser = jasmine.createSpy('MockLuser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Dealer': MockDealer,
                'Lprofil': MockLprofil,
                'Luser': MockLuser
            };
            createController = function() {
                $injector.get('$controller')("DealerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:dealerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
