'use strict';

describe('Controller Tests', function() {

    describe('Lprofil Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLprofil, MockDmanager, MockDealer, MockSdealer, MockDelegue;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLprofil = jasmine.createSpy('MockLprofil');
            MockDmanager = jasmine.createSpy('MockDmanager');
            MockDealer = jasmine.createSpy('MockDealer');
            MockSdealer = jasmine.createSpy('MockSdealer');
            MockDelegue = jasmine.createSpy('MockDelegue');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Lprofil': MockLprofil,
                'Dmanager': MockDmanager,
                'Dealer': MockDealer,
                'Sdealer': MockSdealer,
                'Delegue': MockDelegue
            };
            createController = function() {
                $injector.get('$controller')("LprofilDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:lprofilUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
